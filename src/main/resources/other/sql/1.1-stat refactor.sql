---------------------------
---- 1.1 STAT REFACTOR ----
---------------------------

--------------------
--Включение предков
--------------------

--изменение таблицы статистики
ALTER TABLE public.stat
ADD COLUMN parent_id bigint,
ADD COLUMN child_list_id bigint;

--добавление индексов
CREATE INDEX stat_parent_id_index ON public.stat USING btree (parent_id);
CREATE INDEX stat_child_list_id_index ON public.stat USING btree (child_list_id);

--------------------------------------------------
--Выделение времени с тегами в отдельную сущность
--------------------------------------------------

--создать таблицу
CREATE TABLE public.stat_time (
    id bigint NOT NULL,
    fail_time double precision,
    pass_time double precision,
    tag character varying(255),
    stat_id bigint
);
ALTER TABLE public.stat_time OWNER TO postgres;

--создать секвенции
CREATE SEQUENCE public.stat_time_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.stat_time_id_seq OWNER TO postgres;

--создать ключи
ALTER TABLE ONLY public.stat_time
    ADD CONSTRAINT stat_time_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.stat_time
    ADD CONSTRAINT stat_time_foreign_stat_id FOREIGN KEY (stat_id) REFERENCES public.stat(id);

--создать индексы
CREATE INDEX stat_time_id_index ON public.stat_time USING btree (id);
CREATE INDEX stat_time_stat_id_index ON public.stat_time USING btree (stat_id);

--------------------------------------------------
--Выделение периодов времени в отдельную сущность
--------------------------------------------------

--создать таблицу периодов
CREATE TABLE public.period_stat (
    id bigint NOT NULL,
    run_id bigint,
    profile double precision,
    tps double precision,
    about character varying(255),
    time_start timestamp without time zone,
    time_finish timestamp without time zone
);
ALTER TABLE public.period_stat OWNER TO postgres;

--создать секвенции
CREATE SEQUENCE public.period_stat_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.period_stat_id_seq OWNER TO postgres;

--создать ключи
ALTER TABLE ONLY public.period_stat
    ADD CONSTRAINT period_stat_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.period_stat
    ADD CONSTRAINT period_stat_foreign_run_id FOREIGN KEY (run_id) REFERENCES public.run(id);


--добавление индексов
CREATE INDEX period_stat_id_index ON public.period_stat USING btree (id);
CREATE INDEX period_stat_run_id_index ON public.period_stat USING btree (run_id);
CREATE INDEX period_stat_time_start_index ON public.period_stat USING btree (time_start);
CREATE INDEX period_stat_time_finish_index ON public.period_stat USING btree (time_finish);

--изменение таблицы статистики
ALTER TABLE public.stat
ADD COLUMN period_stat_id bigint;
--индекс на таблицы статистики
CREATE INDEX period_stat_stat_id_index ON public.stat USING btree (period_stat_id);


--------------------------------------------------
--Миграция данных
--------------------------------------------------

--заполняем периоды
INSERT INTO public.period_stat("id","run_id", "profile", "time_start", "time_finish")
select
	nextval('public.period_stat_id_seq'),
	run_id_strings as run_id,
	cast(profile_string as double precision) as profile,
	TO_TIMESTAMP(start_time_string,'HH24:MI DD.MM.YY')::timestamp without time zone as start_time,
	TO_TIMESTAMP(finish_time_string,'HH24:MI DD.MM.YY')::timestamp without time zone as finish_time
from (
	select
		run_id as run_id_strings,
		substring(tag,0,position('% - ' in tag)) as profile_string,
		substring(tag,position('период ' in tag)+7,14) as start_time_string,
		substring(tag,position('22 - ' in tag)+5,14) as finish_time_string
	FROM
		public.stat
) as strings
group by run_id, profile, start_time, finish_time;

--заполняем стат с id периодов
UPDATE
	public.stat SET period_stat_id = periods.id
FROM
	public.period_stat as periods
WHERE
periods.run_id = stat.run_id AND
periods.profile::text = substring(stat.tag,0,position('% - ' in stat.tag)) /* as profile_string*/ AND
to_char(periods.time_start, 'HH24:MI DD.MM.YY') = substring(stat.tag,position('период ' in stat.tag)+7,14) /* as start_time_string */ AND
to_char(periods.time_finish, 'HH24:MI DD.MM.YY') = substring(stat.tag,position('22 - ' in stat.tag)+5,14) /* as finish_time_string */;

--вставляем уникальные статы
INSERT INTO public.stat_time("id", "stat_id", "fail_time", "pass_time", "tag")
select
	nextval('public.stat_time_id_seq'),
	stat.id,
	stat.fail_time,
	stat.pass_time,
	substring(stat.tag,position(' , ' in stat.tag)+3,position('% ' in stat.tag)) as tag
FROM
	public.stat
group by id, fail_time, pass_time, tag;


--заполняем stat_time и удаляем дубликаты из stat
with
keeper as (select id, pass_count,fail_count, script, period_stat_id, min(id) over(partition by pass_count,fail_count, script, period_stat_id) mid from public.stat)
,
updater as (update public.stat_time st set stat_id = (select mid from keeper where keeper.id = st.stat_id))
delete
from public.stat d
where d.id not in (select mid from keeper);

--удалить столбцы из старой таблицы
ALTER TABLE public.stat
DROP COLUMN run_id,
DROP COLUMN pass_time,
DROP COLUMN fail_time,
DROP COLUMN tag;

--ну и чтобы заработало
update public.period_stat set tps = 0.0;
update public.period_stat set about = '';

--и внешний ключ забыл
ALTER TABLE ONLY public.stat
    ADD CONSTRAINT stat_foreign_period_stat_id FOREIGN KEY (period_stat_id) REFERENCES public.period_stat(id);