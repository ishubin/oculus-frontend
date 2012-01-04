-- MySQL dump 10.10
--
-- Host: localhost    Database: oculus
-- ------------------------------------------------------
-- Server version	5.0.19-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `builds`
--

DROP TABLE IF EXISTS `builds`;
CREATE TABLE `builds` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL default '',
  `description` text NOT NULL,
  `date` timestamp NOT NULL default '0000-00-00 00:00:00',
  `project_id` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_name` (`name`,`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `charts`
--

DROP TABLE IF EXISTS `charts`;
CREATE TABLE `charts` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `type` enum('pie','line','bar') default NULL,
  `title` varchar(64) NOT NULL default '',
  `x_axis_name` varchar(64) NOT NULL default '',
  `x_axis_type` enum('int','float','date') default NULL,
  `y_axis_name` varchar(64) NOT NULL default '',
  `y_axis_type` enum('int','float','date') default NULL,
  `data` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `dashboards`
--

DROP TABLE IF EXISTS `dashboards`;
CREATE TABLE `dashboards` (
  `project_id` bigint(20) unsigned NOT NULL default '0',
  `runner_id` bigint(20) unsigned NOT NULL default '0',
  `days_period` int(10) unsigned NOT NULL default '16',
  `day_start` smallint(5) unsigned NOT NULL default '0',
  `summary_statistics` tinyint(1) NOT NULL default '1',
  `health_chart` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `documents`
--

DROP TABLE IF EXISTS `documents`;
CREATE TABLE `documents` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(256) NOT NULL default '',
  `content` text,
  `type` enum('testcase','file') NOT NULL default 'testcase',
  `folder_id` bigint(20) unsigned NOT NULL default '0',
  `date` timestamp NOT NULL default '0000-00-00 00:00:00',
  `user_id` bigint(20) unsigned NOT NULL default '0',
  `project_id` bigint(20) unsigned NOT NULL default '0',
  `description` text,
  `branch` text,
  `type_extended` varchar(16) default NULL,
  `size` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `filters`
--

DROP TABLE IF EXISTS `filters`;
CREATE TABLE `filters` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(64) NOT NULL default '',
  `description` mediumtext NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL default '0',
  `date` timestamp NOT NULL default '0000-00-00 00:00:00',
  `filter` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `folders`
--

DROP TABLE IF EXISTS `folders`;
CREATE TABLE `folders` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `project_id` bigint(20) unsigned NOT NULL default '0',
  `parent_id` bigint(20) unsigned NOT NULL default '0',
  `name` varchar(128) NOT NULL default '',
  `description` text,
  `user_id` bigint(20) unsigned NOT NULL default '0',
  `children` bigint(20) unsigned NOT NULL default '0',
  `branch` text,
  `documents` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `projects`
--

DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL default '',
  `description` mediumtext,
  `path` varchar(45) NOT NULL default '',
  `parent_id` bigint(20) unsigned NOT NULL default '0',
  `subprojects_count` bigint(20) unsigned NOT NULL default '0',
  `tests_count` bigint(20) unsigned NOT NULL default '0',
  `icon` varchar(96) default NULL,
  `author_id` bigint(20) unsigned NOT NULL default '0',
  `date` timestamp NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_name` (`name`,`parent_id`),
  KEY `unique_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!50003 SET @OLD_SQL_MODE=@@SQL_MODE*/;
DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `oculus_dashboard_init` AFTER INSERT ON `projects` FOR EACH ROW call create_dashboard(new.id) */;;

DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;

--
-- Table structure for table `saved_runs`
--

DROP TABLE IF EXISTS `saved_runs`;
CREATE TABLE `saved_runs` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `date` timestamp NOT NULL default '0000-00-00 00:00:00',
  `user_id` bigint(20) unsigned NOT NULL default '0',
  `name` varchar(128) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `suite_runs`
--

DROP TABLE IF EXISTS `suite_runs`;
CREATE TABLE `suite_runs` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `start_time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `end_time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `name` varchar(45) default NULL COMMENT 'This value will be used in case if project_id==''0''',
  `runner_id` bigint(20) unsigned NOT NULL default '0',
  `parameters` mediumtext NOT NULL,
  `agent_name` varchar(64) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `suite_statistics`
--

DROP TABLE IF EXISTS `suite_statistics`;
CREATE TABLE `suite_statistics` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `suite_run_id` bigint(20) unsigned NOT NULL default '0',
  `project_id` bigint(20) unsigned NOT NULL default '0',
  `total` int(10) unsigned NOT NULL default '0',
  `failed` int(10) unsigned NOT NULL default '0',
  `passed` int(10) unsigned NOT NULL default '0',
  `warning` int(10) unsigned NOT NULL default '0',
  `runner_id` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `test_cases`
--

DROP TABLE IF EXISTS `test_cases`;
CREATE TABLE `test_cases` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(128) NOT NULL default '',
  `project_id` bigint(20) unsigned NOT NULL default '0',
  `description` text,
  `parameters` text,
  `user_id` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `test_parameters`
--

DROP TABLE IF EXISTS `test_parameters`;
CREATE TABLE `test_parameters` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `type` enum('input','output') NOT NULL default 'input',
  `name` varchar(45) NOT NULL default '',
  `description` varchar(256) default NULL,
  `control_type` enum('list','text','boolean','undefined') NOT NULL default 'text',
  `default_value` varchar(256) default NULL,
  `possible_values` text,
  `test_id` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_name` (`name`,`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `test_runs`
--

DROP TABLE IF EXISTS `test_runs`;
CREATE TABLE `test_runs` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `suite_run_id` bigint(20) unsigned NOT NULL default '0',
  `test_id` bigint(20) unsigned NOT NULL default '0',
  `start_time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `end_time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `reasons` text,
  `report` text,
  `name` varchar(128) default NULL,
  `status` enum('PASSED','WARNING','FAILED') NOT NULL default 'PASSED',
  `project_id` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!50003 SET @OLD_SQL_MODE=@@SQL_MODE*/;
DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `test_runs_uss` AFTER INSERT ON `test_runs` FOR EACH ROW call update_suite_statistics(new.id, new.suite_run_id, new.project_id, new.status) */;;

DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;

--
-- Table structure for table `test_supporters`
--

DROP TABLE IF EXISTS `test_supporters`;
CREATE TABLE `test_supporters` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `test_id` bigint(20) unsigned NOT NULL default '0',
  `user_id` bigint(20) unsigned NOT NULL default '0',
  `description` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `tests`
--

DROP TABLE IF EXISTS `tests`;
CREATE TABLE `tests` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL default '',
  `description` mediumtext,
  `project_id` bigint(20) unsigned NOT NULL default '0',
  `author_id` bigint(20) unsigned NOT NULL default '0',
  `supporters` bigint(20) unsigned NOT NULL default '0',
  `date` timestamp NOT NULL default '0000-00-00 00:00:00',
  `mapping` varchar(256) default NULL,
  `test_case_id` bigint(20) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `trm_properties`
--

DROP TABLE IF EXISTS `trm_properties`;
CREATE TABLE `trm_properties` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL default '',
  `description` text,
  `type` enum('suite_parameter') NOT NULL default 'suite_parameter',
  `value` text,
  `subtype` varchar(45) default NULL,
  `project_id` bigint(20) unsigned default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_name` (`name`,`type`,`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `trm_task_suites`
--

DROP TABLE IF EXISTS `trm_task_suites`;
CREATE TABLE `trm_task_suites` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `task_id` bigint(20) unsigned NOT NULL default '0',
  `name` varchar(128) NOT NULL default '',
  `description` mediumtext,
  `suiteData` text,
  `project_id` bigint(20) unsigned NOT NULL default '0',
  `parameters` mediumtext,
  `unite_tests` tinyint(3) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `trm_tasks`
--

DROP TABLE IF EXISTS `trm_tasks`;
CREATE TABLE `trm_tasks` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(128) NOT NULL default '',
  `description` text,
  `user_id` bigint(20) unsigned NOT NULL default '0',
  `date` timestamp NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `trm_tasks_completed`
--

DROP TABLE IF EXISTS `trm_tasks_completed`;
CREATE TABLE `trm_tasks_completed` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL default '',
  `description` mediumtext NOT NULL,
  `date` timestamp NOT NULL default '0000-00-00 00:00:00',
  `suiteIds` varchar(256) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL default '',
  `login` varchar(45) NOT NULL default '',
  `password` varchar(45) NOT NULL default '',
  `email` varchar(128) NOT NULL default '',
  `permissions` varchar(45) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `unique_name` (`name`),
  UNIQUE KEY `unique_login` (`login`),
  KEY `unique_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping routines for database 'oculus'
--
DELIMITER ;;
/*!50003 DROP PROCEDURE IF EXISTS `create_dashboard` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE PROCEDURE `create_dashboard`(in p_id bigint)
BEGIN

  declare parent_id bigint;
  select p.parent_id into parent_id from projects p where p.id = p_id;

  if parent_id =0 then
  insert into dashboards (project_id) values(p_id);
  end if;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `test_fill_statistics` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE PROCEDURE `test_fill_statistics`(in days integer, in start_date timestamp)
BEGIN
   #this procedure is used only for testing the dashboard and will be deleted in the release
  declare cdate timestamp;
  declare d timestamp;
  declare dh timestamp;
  declare sid bigint;
  declare i integer;
  declare ptotal integer;
  declare total integer;
  declare test_passed integer;
  declare test_warning integer;
  declare test_failed integer;
  declare test_status varchar(16);
  declare j integer;

--  truncate suite_runs;
--  truncate test_runs;
  truncate suite_statistics;



  set i = 5;
  while i<8 do
    set cdate = start_date;
    set d = subdate(cDate, interval days day);
    set ptotal = (FLOOR( 10 + RAND( ) *20 ));
    while d <= cdate do
      set dh = d;


      set dh = adddate(dh, interval 1 hour);
      insert into suite_runs (start_time, end_time, name, runner_id) values (dh, adddate(dh, interval 1 hour), 'suite 1', 2);
      set sid = last_insert_id();

      set total = ptotal;
      set test_passed = 0;
      set test_warning = 0;
      set test_failed = 0;
      while total>0 do
        set j = (FLOOR( 0 + RAND( ) *3 ));

        if j = 0 then set test_failed = test_failed + 1;
           elseif j = 1 then set test_warning = test_warning+1;
           else set test_passed = test_passed+1;
        end if;

        set total = total - 1;
      end while;
      insert into suite_statistics (suite_run_id, project_id, total, failed, passed, warning, runner_id) values (sid, i, ptotal, test_failed, test_passed, test_warning, 0);
      set d = adddate(d, interval 1 day);
    end while;


    set i = i +1;
  end while;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `test_fill_suites` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE PROCEDURE `test_fill_suites`(in days integer, in start_date timestamp)
BEGIN
  #this procedure is used only for testing the dashboard and will be deleted in the release
  declare cdate timestamp;
  declare d timestamp;
  declare dh timestamp;
  declare sid bigint;
  declare i integer;
  declare ptotal integer;
  declare total integer;
  declare test_status varchar(16);
  declare j integer;

  truncate suite_runs;
  truncate test_runs;
  truncate suite_statistics;



  set i = 5;
  while i<8 do
    set cdate = start_date;
    set d = subdate(cDate, interval days day);
    set ptotal = (FLOOR( 10 + RAND( ) *20 ));
    while d <= cdate do
      set dh = d;


      set dh = adddate(dh, interval 1 hour);
      insert into suite_runs (start_time, end_time, name, runner_id) values (dh, adddate(dh, interval 1 hour), 'suite 1', 2);
      set sid = last_insert_id();

      set total = ptotal;
      while total>0 do
        set j = (FLOOR( 0 + RAND( ) *3 ));

        if j = 0 then set test_status = 'FAILED';
        elseif j = 1 then set test_status = 'WARNING';
        else set test_status = 'PASSED';
        end if;

        #todo
        insert into test_runs (suite_run_id, test_id, start_time, end_time, report, name, status, project_id)
          values (sid, 0, current_timestamp(), current_timestamp(), '','test2134',test_status, i);

        set total = total - 1;
      end while;
      set d = adddate(d, interval 1 day);
    end while;


    set i = i +1;
  end while;

END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `update_suite_statistics` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE PROCEDURE `update_suite_statistics`(
  in tr_id bigint,
  in sr_id bigint,
  in p_id   bigint,
  in tr_status enum('PASSED', 'WARNING', 'FAILED')
)
BEGIN
  declare ss_id bigint;
  declare r_id bigint;


  DECLARE CONTINUE HANDLER FOR 1329 SET ss_id = 0, r_id = 0;

  #fetching runner id from suite_runs table
  select sr.runner_id into r_id from suite_runs sr where sr.id = sr_id;

  select ss.id into ss_id from suite_statistics ss where ss.suite_run_id = sr_id and ss.project_id = p_id and ss.runner_id = r_id;

  if ss_id = 0 then
    insert into suite_statistics (suite_run_id, project_id, runner_id, total, failed, passed, warning) values (sr_id, p_id, r_id, 0, 0, 0, 0);
    set ss_id = last_insert_id();
  end if;

  if tr_status = 'PASSED' then
    update suite_statistics ss set ss.total = ss.total+1, ss.passed = ss.passed+1 where ss.id = ss_id;
  elseif tr_status = 'WARNING' then
    update suite_statistics ss set ss.total = ss.total+1, ss.warning = ss.warning+1 where ss.id = ss_id;
  elseif tr_status = 'FAILED' then
    update suite_statistics ss set ss.total = ss.total+1, ss.failed = ss.failed+1 where ss.id = ss_id;
  end if;

END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
DELIMITER ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

