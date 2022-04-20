INSERT INTO role (id, role) VALUES
  (nextval('role_id_seq'),'ROLE_VIEWER'),
  (nextval('role_id_seq'),'ROLE_USER'),
  (nextval('role_id_seq'),'ROLE_DEVELOPER'),
  (nextval('role_id_seq'),'ROLE_ADMIN');

INSERT INTO "user" (id, active, email, first_name, last_name, password) VALUES
  (nextval('user_id_seq'), true, 'viewer@admin.com', 'Vi', 'Ewer', '$2a$10$GWKjQLlQVHjsJFE2jwvI2e0wEpevG5lJ.4aaLGpOmq4efhYLXi7j6'),
  (nextval('user_id_seq'), true, 'user@admin.com', 'Us', 'Er', '$2a$10$VM14b50d4SmPUDyIjLQoVu9ROSt0ng6W0RdiB3c1xmHU6I44jbAv.'),
  (nextval('user_id_seq'), true, 'developer@admin.com', 'Dev', 'Eloper', '$2a$10$Jg/n0bwaYZxJBhTPVhWuDO989e3xmLbUPnB/IXNde/LfbCHjPPt8.'),
  (nextval('user_id_seq'), true, 'admin@admin.com', 'Ad', 'Min', '$2a$10$Jg/n0bwaYZxJBhTPVhWuDO989e3xmLbUPnB/IXNde/LfbCHjPPt8.');

INSERT INTO user_roles (user_id, role_id) VALUES
  (1, 1),
  (2, 1),
  (2, 2),
  (3, 1),
  (3, 2),
  (3, 3),
  (4, 1),
  (4, 2),
  (4, 3),
  (4, 4);


  INSERT INTO system (id, name, about, runs_about, reports_about) VALUES
  (nextval('system_id_seq'), 'SmartBio', 'Описание страницы календаря SmartBio', 'Описание страницы запусков SmartBio', 'Описание страницы отчётов SmartBio'),
  (nextval('system_id_seq'), 'Liveness', 'Описание страницы календаря Liveness', 'Описание страницы запусков Liveness', 'Описание страницы отчётов Liveness');

  INSERT INTO report (id, visible, name, status, date_created, user_id, system_id) VALUES
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-01',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-02',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-03',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-04',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-05',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-06',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-07',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-08',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-09',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-10',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-11',1,1),
  (nextval('report_id_seq'), true, 'М8 Отчёт НТ', 'Создан', '2022-01-12',1,1);

  INSERT INTO run (id, visible, name, system_id, status, time_start, time_finish, user_id, report_id) VALUES
  (nextval('run_id_seq'), true, 'M8 Надёжность 24ч', 1, 'Успешно', '2022-01-05 00:16:01', '2022-01-06 16:00:01',1,1),
  (nextval('run_id_seq'), true, 'M8 Максимум 2',   1,  'Неуспешно', '2022-01-09 00:00:01', '2022-01-10 16:00:01',1,1),
  (nextval('run_id_seq'), true, 'M8 Максимум',   2, 'Неуспешно', '2022-01-08 00:00:01', '2022-01-09 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null),
  (nextval('run_id_seq'), true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2022-01-03 00:00:01', '2022-01-04 00:00:01',1,null);


/*
  INSERT INTO info (id, run_id, tag, data) VALUES
  (1, 1,'Описание теста','Тест прошёл успешно. и ещё много текста.' ),
  (2, 2,'Описание теста','Тест вроде был, а вроде и нет.' ),
  (7, 4,'Описание теста','Тест прошёл ужасно!!!' );


  INSERT INTO info (id, report_id, tag, data) VALUES
  (3, 1,'Результаты тестирования','Фича успешно протестирована' ),
  (4, 1,'Дефекты','Допустим были проблемы, вот их описание' ),
  (5, 1,'Рекомендации','Рекомендуем пойти в отпуск' ),
  (6, 1,'Выводы','Можно внедрять, наверное' );


  INSERT INTO attach (id, run_id, tag, name,  type, data) VALUES
  (1, 1,'Важный график','cpu.png','data/png', FILE_READ('./work/image/cpu.png')),
  (2, 1,'Конфиг сервера','config.txt','data/txt', FILE_READ('./work/other/config.txt'));

  INSERT INTO attach (id, report_id, tag, name,  type, data) VALUES
  (3, 1,'Конфиг сервера','config.txt','data/txt', FILE_READ('./work/other/config.txt'));


   INSERT INTO stat (id, run_id, tag, script, sla, profile , pass_count, fail_count, pass_time, fail_time) VALUES
   (1, 1, 'Время отклика 99.5% операций', 'script1', 1.0, 4600, 4200, 400, 0.57, 0.3),
   (2, 1, 'Время отклика 99.5% операций', 'script2', 2.0, 1200, 1200, 0, 3.68, 0),
   (3, 1, 'Время отклика 99.9% операций', 'script1', 1.0, 4600, 4200, 400, 0.87, 0.3),
   (4, 1, 'Время отклика 99.9% операций', 'script2', 2.0, 1200, 1200, 0, 4.89, 0),
   (5, 2, 'Время отклика 90% операций', 'script5', 1.0, 6000, 6000, 0, 0.89, 0),
   (6, 2, 'Время отклика 90% операций', 'script6', 1.0, 7000, 7000, 0, 0.75, 0),
   (7, 3, 'Время отклика 99% операций', 'script0', 0.2, 1000, 1000, 0, 0.15, 0);

   INSERT INTO graph (id, run_id, tag, about,name,  type, data) VALUES
   (1, 1, '1. Утилизация ресурсов','Утилизация CPU', 'cpu.png','data/png', FILE_READ('./work/image/cpu.png')),
   (3, 1, '1. Утилизация ресурсов','Утилизация RAM', 'ram.png','data/png', FILE_READ('./work/image/ram.png')),
   (2, 1, '2. Бизнес метрики', 'Бизнес график', 'time.png','data/png', FILE_READ('./work/image/time.png')),
   (4, 2, 'Другое', 'Другой график', 'diff.png','data/png', FILE_READ('./work/image/diff.png')),
   (5, 2, 'Другое2', 'Другой график2', 'time.png','data/png', FILE_READ('./work/image/time.png')),
   (6, 3, 'Другое3', 'Другой график3', 'time.png','data/png', FILE_READ('./work/image/time.png'));

*/