SET NAMES utf8;
SET foreign_key_checks = 0;

--INSERT INTO `page` (`id`, `url`, `title`) VALUES
  --(1, 'homepage', 'Homepage'),
 -- (2, 'about', 'About'),
--  (3, 'contacts', 'Contacts');

--INSERT INTO `page_text` (`id`, `content`, `page_id`, `identity`, `type`) VALUES
  --(1, 'Список систем', 1, 'systems', 'TEXT_BOX'),
  --(2, 'Homepage about', 1, 'homepage-about', 'CK_EDITOR'),
  --(3, 'Мы команда НТ и мы любим автоматизацию.', 2, 'about-about-orig', 'CK_EDITOR'),
  --(4, 'Contacts about', 3, 'contacts-about', 'CK_EDITOR');

--INSERT INTO `page_image` (`id`, `file_name`, `identity`, `path`, `page_id`) VALUES
--(1,	'dummy1.png',	'about-data',	NULL,	2);

--INSERT INTO `product` (`id`, `price`, `title`) VALUES
--(1,	129,	'Product1'),
--(2,	299,	'Product2');


-----------------------------------------------
-----------------------------------------------


INSERT INTO `role` (`id`, `role`) VALUES
  (1, 'ROLE_VIEWER'),
  (2, 'ROLE_USER'),
  (3, 'ROLE_DEVELOPER'),
  (4, 'ROLE_ADMIN');


-- webconfig and https://bcrypt-generator.com
INSERT INTO `user` (`id`, `active`, `email`, `first_name`, `last_name`, `password`) VALUES
  (1, 1, 'viewer@admin.com', 'Vi', 'Ewer', '$2a$10$GWKjQLlQVHjsJFE2jwvI2e0wEpevG5lJ.4aaLGpOmq4efhYLXi7j6'),
  (2, 1, 'user@admin.com', 'Us', 'Er', '$2a$10$VM14b50d4SmPUDyIjLQoVu9ROSt0ng6W0RdiB3c1xmHU6I44jbAv.'),
  (3, 1, 'developer@admin.com', 'Dev', 'Eloper', '$2a$10$Jg/n0bwaYZxJBhTPVhWuDO989e3xmLbUPnB/IXNde/LfbCHjPPt8.'),
  (4, 1, 'admin@admin.com', 'Ad', 'Min', '$2a$10$Jg/n0bwaYZxJBhTPVhWuDO989e3xmLbUPnB/IXNde/LfbCHjPPt8.');


INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
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

  -----------------------------------------------

  INSERT INTO `system` (`id`, `name`, `about`, `runs_about`, `reports_about`) VALUES
  (1, 'SmartBio', 'Описание страницы календаря SmartBio', 'Описание страницы запусков SmartBio', 'Описание страницы отчётов SmartBio'),
  (2, 'Liveness', 'Описание страницы календаря Liveness', 'Описание страницы запусков Liveness', 'Описание страницы отчётов Liveness');



  INSERT INTO `report` (`id`, `visible`, `name`, `status`, `date_created`, `user_id`, `system_id`) VALUES
  (1, true, 'М8 Отчёт НТ', 'Создан', '2021-12-12',1,1);

  --YYYY-MM-DD
  INSERT INTO `run` (`id`, `visible`, `name`, `system_id`, `status`, `time_start`, `time_finish`, `user_id`, `report_id`) VALUES
  (1, true, 'M8 Надёжность 24ч', 1, 'Успешно', '2021-12-07 00:16:01', '2021-12-08 16:00:01',1,1),
  (2, true, 'M8 Максимум 2',   1,  'Неуспешно', '2021-12-18 00:00:01', '2021-12-19 16:00:01',1,1),
  (3, true, 'M8 Максимум',   2, 'Неуспешно', '2021-12-19 00:00:01', '2021-12-20 00:00:01',1,null),
  (4, true, 'M8 Отказоустойчивость',   1, 'Надо разбираться', '2021-12-27 01:01:01', '2021-12-28 16:00:01',1,null);


  INSERT INTO `info` (`id`, `run_id`, `tag`, `data`) VALUES
  (1, 1,'Описание теста','Тест прошёл успешно. и ещё много текста.' ),
  (2, 2,'Описание теста','Тест вроде был, а вроде и нет.' ),
  (7, 4,'Описание теста','Тест прошёл ужасно!!!' );


  INSERT INTO `info` (`id`, `report_id`, `tag`, `data`) VALUES
  (3, 1,'Результаты тестирования','Фича успешно протестирована' ),
  (4, 1,'Дефекты','Допустим были проблемы, вот их описание' ),
  (5, 1,'Рекомендации','Рекомендуем пойти в отпуск' ),
  (6, 1,'Выводы','Можно внедрять, наверное' );


  INSERT INTO `attach` (`id`, `run_id`, `tag`, `name`,  `type`, `data`) VALUES
  (1, 1,'Важный график','cpu.png','data/png', FILE_READ('./work/image/cpu.png')),
  (2, 1,'Конфиг сервера','config.txt','data/txt', FILE_READ('./work/other/config.txt'));

  INSERT INTO `attach` (`id`, `report_id`, `tag`, `name`,  `type`, `data`) VALUES
  (3, 1,'Конфиг сервера','config.txt','data/txt', FILE_READ('./work/other/config.txt'));


   INSERT INTO `stat` (`id`, `run_id`, `tag`, `script`, `sla`, `profile` , `pass_count`, `fail_count`, `pass_time`, `fail_time`) VALUES
   (1, 1, 'Время отклика 99.5% операций', 'script1', 1.0, 4600, 4200, 400, 0.57, 0.3),
   (2, 1, 'Время отклика 99.5% операций', 'script2', 2.0, 1200, 1200, 0, 3.68, 0),
   (3, 1, 'Время отклика 99.9% операций', 'script1', 1.0, 4600, 4200, 400, 0.87, 0.3),
   (4, 1, 'Время отклика 99.9% операций', 'script2', 2.0, 1200, 1200, 0, 4.89, 0),
   (5, 2, 'Время отклика 90% операций', 'script5', 1.0, 6000, 6000, 0, 0.89, 0),
   (6, 2, 'Время отклика 90% операций', 'script6', 1.0, 7000, 7000, 0, 0.75, 0),
   (7, 3, 'Время отклика 99% операций', 'script0', 0.2, 1000, 1000, 0, 0.15, 0);

   INSERT INTO `graph` (`id`, `run_id`, `tag`, `about`,`name`,  `type`, `data`) VALUES
   (1, 1, '1. Утилизация ресурсов','Утилизация CPU', 'cpu.png','data/png', FILE_READ('./work/image/cpu.png')),
   (3, 1, '1. Утилизация ресурсов','Утилизация RAM', 'ram.png','data/png', FILE_READ('./work/image/ram.png')),
   (2, 1, '2. Бизнес метрики', 'Бизнес график', 'time.png','data/png', FILE_READ('./work/image/time.png')),
   (4, 2, 'Другое', 'Другой график', 'diff.png','data/png', FILE_READ('./work/image/diff.png')),
   (5, 2, 'Другое2', 'Другой график2', 'time.png','data/png', FILE_READ('./work/image/time.png')),
   (6, 3, 'Другое3', 'Другой график3', 'time.png','data/png', FILE_READ('./work/image/time.png'));

