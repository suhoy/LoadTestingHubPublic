--
-- BEST SO FAR
-- cd /d C:\PostgreSQL\14\bin
-- pg_dump.exe -U postgres -s clean
--


--
-- PostgreSQL database dump
--

-- Dumped from database version 14.1
-- Dumped by pg_dump version 14.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pg_stat_statements; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_stat_statements WITH SCHEMA public;


--
-- Name: EXTENSION pg_stat_statements; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION pg_stat_statements IS 'track planning and execution statistics of all SQL statements executed';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: attach; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.attach (
    id bigint NOT NULL,
    data bytea,
    name character varying(255),
    tag character varying(255),
    type character varying(255),
    report_id bigint,
    run_id bigint
);


ALTER TABLE public.attach OWNER TO postgres;

--
-- Name: attach_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.attach_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.attach_id_seq OWNER TO postgres;

--
-- Name: graph; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.graph (
    id bigint NOT NULL,
    about character varying(255),
    data bytea,
    name character varying(255),
    tag character varying(255),
    type character varying(255),
    run_id bigint
);


ALTER TABLE public.graph OWNER TO postgres;

--
-- Name: graph_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.graph_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.graph_id_seq OWNER TO postgres;

--
-- Name: info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.info (
    id bigint NOT NULL,
    data text,
    tag character varying(255),
    report_id bigint,
    run_id bigint
);


ALTER TABLE public.info OWNER TO postgres;

--
-- Name: info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.info_id_seq OWNER TO postgres;

--
-- Name: report; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report (
    id bigint NOT NULL,
    date_created date,
    name character varying(255),
    status character varying(255),
    visible boolean DEFAULT false,
    system_id bigint,
    user_id bigint
);


ALTER TABLE public.report OWNER TO postgres;

--
-- Name: report_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.report_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.report_id_seq OWNER TO postgres;

--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id bigint NOT NULL,
    role character varying(255)
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_id_seq OWNER TO postgres;

--
-- Name: run; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.run (
    id bigint NOT NULL,
    name character varying(255),
    status character varying(255),
    time_finish timestamp without time zone,
    time_start timestamp without time zone,
    visible boolean DEFAULT false,
    report_id bigint,
    system_id bigint,
    user_id bigint
);


ALTER TABLE public.run OWNER TO postgres;

--
-- Name: run_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.run_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.run_id_seq OWNER TO postgres;

--
-- Name: stat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stat (
    id bigint NOT NULL,
    fail_count integer,
    fail_time double precision,
    pass_count integer,
    pass_time double precision,
    profile integer,
    script character varying(255),
    sla double precision,
    tag character varying(255),
    run_id bigint
);


ALTER TABLE public.stat OWNER TO postgres;

--
-- Name: stat_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.stat_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.stat_id_seq OWNER TO postgres;

--
-- Name: system; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.system (
    id bigint NOT NULL,
    about text,
    name character varying(255),
    reports_about text,
    runs_about text
);


ALTER TABLE public.system OWNER TO postgres;

--
-- Name: system_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.system_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.system_id_seq OWNER TO postgres;

--
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."user" (
    id bigint NOT NULL,
    active boolean,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255)
);


ALTER TABLE public."user" OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_id_seq OWNER TO postgres;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- Name: attach attach_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attach
    ADD CONSTRAINT attach_pkey PRIMARY KEY (id);


--
-- Name: graph graph_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.graph
    ADD CONSTRAINT graph_pkey PRIMARY KEY (id);


--
-- Name: info info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.info
    ADD CONSTRAINT info_pkey PRIMARY KEY (id);


--
-- Name: report report_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report
    ADD CONSTRAINT report_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: run run_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.run
    ADD CONSTRAINT run_pkey PRIMARY KEY (id);


--
-- Name: stat stat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stat
    ADD CONSTRAINT stat_pkey PRIMARY KEY (id);


--
-- Name: system system_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.system
    ADD CONSTRAINT system_pkey PRIMARY KEY (id);


--
-- Name: role uk_bjxn5ii7v7ygwx39et0wawu0q; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT uk_bjxn5ii7v7ygwx39et0wawu0q UNIQUE (role);


--
-- Name: system uk_bkiyetavdyvy7tmo38h3ssd74; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.system
    ADD CONSTRAINT uk_bkiyetavdyvy7tmo38h3ssd74 UNIQUE (name);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: attach_report_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX attach_report_id_index ON public.attach USING btree (report_id);


--
-- Name: attach_run_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX attach_run_id_index ON public.attach USING btree (run_id);


--
-- Name: graph_run_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX graph_run_id_index ON public.graph USING btree (run_id);


--
-- Name: info_report_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX info_report_id_index ON public.info USING btree (report_id);


--
-- Name: info_run_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX info_run_id_index ON public.info USING btree (run_id);


--
-- Name: report_date_created_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX report_date_created_index ON public.report USING btree (date_created);


--
-- Name: report_system_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX report_system_id_index ON public.report USING btree (system_id);


--
-- Name: run_report_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX run_report_id_index ON public.run USING btree (report_id);


--
-- Name: run_system_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX run_system_id_index ON public.run USING btree (system_id);


--
-- Name: run_time_finish_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX run_time_finish_index ON public.run USING btree (time_finish);


--
-- Name: run_time_start_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX run_time_start_index ON public.run USING btree (time_start);


--
-- Name: stat_run_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX stat_run_id_index ON public.stat USING btree (run_id);


--
-- Name: user_email_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX user_email_index ON public."user" USING btree (email);


--
-- Name: graph fk4wf096heghpwci3m1u2hjxxa9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.graph
    ADD CONSTRAINT fk4wf096heghpwci3m1u2hjxxa9 FOREIGN KEY (run_id) REFERENCES public.run(id);


--
-- Name: stat fk5h6io9y628h1p47l6gqd2gkrg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stat
    ADD CONSTRAINT fk5h6io9y628h1p47l6gqd2gkrg FOREIGN KEY (run_id) REFERENCES public.run(id);


--
-- Name: user_roles fk7ivp84f52aa3vd7ndq0oh0279; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk7ivp84f52aa3vd7ndq0oh0279 FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: run fk8g9iiftkqx120if39e23r3so4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.run
    ADD CONSTRAINT fk8g9iiftkqx120if39e23r3so4 FOREIGN KEY (system_id) REFERENCES public.system(id);


--
-- Name: info fk8md9mmd8lllj50ky2ejyj4wfp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.info
    ADD CONSTRAINT fk8md9mmd8lllj50ky2ejyj4wfp FOREIGN KEY (report_id) REFERENCES public.report(id);


--
-- Name: report fk9pvf8vn93iaypxrx9gvtni9ui; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report
    ADD CONSTRAINT fk9pvf8vn93iaypxrx9gvtni9ui FOREIGN KEY (system_id) REFERENCES public.system(id);


--
-- Name: report fkdoglh6l5tru2i6rwho12ja3hr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report
    ADD CONSTRAINT fkdoglh6l5tru2i6rwho12ja3hr FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: attach fkea20j9e21jsh8uyjemfkx3h65; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attach
    ADD CONSTRAINT fkea20j9e21jsh8uyjemfkx3h65 FOREIGN KEY (report_id) REFERENCES public.report(id);


--
-- Name: run fkerdbq387dbch83k45h1w7w1c1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.run
    ADD CONSTRAINT fkerdbq387dbch83k45h1w7w1c1 FOREIGN KEY (report_id) REFERENCES public.report(id);


--
-- Name: attach fkotmx7npioc52a9nrmmyxrafuw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attach
    ADD CONSTRAINT fkotmx7npioc52a9nrmmyxrafuw FOREIGN KEY (run_id) REFERENCES public.run(id);


--
-- Name: run fkqvk1fvpush1v4ll8opfr848ji; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.run
    ADD CONSTRAINT fkqvk1fvpush1v4ll8opfr848ji FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: user_roles fkrhfovtciq1l558cw6udg0h0d3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkrhfovtciq1l558cw6udg0h0d3 FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: info fktalqvwx2d5pdpvgler4ti5cpy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.info
    ADD CONSTRAINT fktalqvwx2d5pdpvgler4ti5cpy FOREIGN KEY (run_id) REFERENCES public.run(id);


--------------------------------------
-- HOW TO CREATE POSTGRESQL DUMP
--------------------------------------

-- pg_dump.exe -U postgres -s clean


--------------------------------------
-- HOW TO TAKE POSTGRESQL STAT
--------------------------------------
/*
CREATE EXTENSION pg_stat_statements;

SELECT pg_stat_statements_reset();

SELECT * FROM pg_stat_statements;

SELECT
  (total_exec_time / 1000 / 60) as total_min,
  mean_exec_time as avg_ms,
  calls,
  query
FROM pg_stat_statements
ORDER BY 1 DESC
LIMIT 20;
*/



