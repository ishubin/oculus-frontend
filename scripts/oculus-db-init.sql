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
DROP TABLE IF EXISTS `builds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `builds` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `project_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`name`,`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `unit_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `unit` enum('project','test','issue','document','build') NOT NULL DEFAULT 'project',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `cron_ict_test_runs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cron_ict_test_runs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `test_run_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `test_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `suite_run_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `reason` mediumtext NOT NULL,
  `suite_run_parameters` mediumtext,
  `issue_collation_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `test_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `customization_possible_values`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customization_possible_values` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `customization_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `possible_value` varchar(128) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `customizations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customizations` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '',
  `description` mediumtext NOT NULL,
  `unit` enum('project','test','test-case','issue','build') NOT NULL DEFAULT 'project',
  `project_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `subtype` enum('drop-down','list') DEFAULT NULL,
  `type` enum('text','assignee','list','checkbox','checklist') NOT NULL DEFAULT 'text',
  `group_name` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `filters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filters` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '',
  `description` mediumtext NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `filter` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `issue_collation_conditions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issue_collation_conditions` (
  `issue_collation_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `trm_property_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `value` varchar(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`trm_property_id`,`issue_collation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `issue_collation_tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issue_collation_tests` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `issue_collation_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `test_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `test_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_ict` (`issue_collation_id`,`test_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `issue_collations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issue_collations` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `reason_pattern` mediumtext NOT NULL,
  `issue_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `issues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issues` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `link` varchar(128) NOT NULL DEFAULT '',
  `summary` varchar(256) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  `author_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `fixed` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `fixed_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `project_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `subproject_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `dependent_tests` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projects` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `description` mediumtext,
  `path` varchar(45) NOT NULL DEFAULT '',
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `subprojects_count` bigint(20) unsigned NOT NULL DEFAULT '0',
  `tests_count` bigint(20) unsigned NOT NULL DEFAULT '0',
  `icon` varchar(96) DEFAULT NULL,
  `author_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`name`,`parent_id`),
  KEY `unique_path` (`path`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `saved_runs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `saved_runs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `name` varchar(128) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `suite_runs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `suite_runs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `start_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `name` varchar(128) DEFAULT NULL COMMENT 'This value will be used in case if project_id==''0''',
  `runner_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `parameters` mediumtext NOT NULL,
  `agent_name` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `suite_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `suite_statistics` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `suite_run_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `project_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `total` int(10) unsigned NOT NULL DEFAULT '0',
  `failed` int(10) unsigned NOT NULL DEFAULT '0',
  `passed` int(10) unsigned NOT NULL DEFAULT '0',
  `warning` int(10) unsigned NOT NULL DEFAULT '0',
  `runner_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `test_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_groups` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT '',
  `description` mediumtext NOT NULL,
  `project_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `test_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_parameters` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` enum('input','output') NOT NULL DEFAULT 'input',
  `name` varchar(45) NOT NULL DEFAULT '',
  `description` varchar(1024) DEFAULT NULL,
  `control_type` enum('list','text','boolean','undefined') NOT NULL DEFAULT 'text',
  `default_value` varchar(256) DEFAULT NULL,
  `possible_values` text,
  `test_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `sortindex` int(10) unsigned NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`name`,`test_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `test_run_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_run_parameters` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT '',
  `value` text,
  `type` enum('input','output') NOT NULL DEFAULT 'input',
  `test_run_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `test_runs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_runs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `suite_run_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `test_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `start_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `reasons` text,
  `report` longtext,
  `name` varchar(128) DEFAULT NULL,
  `status` enum('PASSED','WARNING','FAILED') NOT NULL DEFAULT 'PASSED',
  `project_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `issue_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tr_test_runs` AFTER INSERT ON `test_runs` FOR EACH ROW begin
   call check_test_run_for_ict(new.id, new.test_id, new.name, new.suite_run_id, new.reasons);
   call update_suite_statistics(new.id, new.suite_run_id, new.project_id, new.status);
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
DROP TABLE IF EXISTS `tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tests` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT '',
  `description` mediumtext,
  `project_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `author_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `supporters` bigint(20) unsigned NOT NULL DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mapping` varchar(256) DEFAULT NULL,
  `group_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `content` text,
  `automated` int(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `trm_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trm_properties` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `description` text,
  `type` enum('suite_parameter') NOT NULL DEFAULT 'suite_parameter',
  `value` text,
  `subtype` varchar(45) DEFAULT NULL,
  `project_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`name`,`type`,`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `trm_task_dependencies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trm_task_dependencies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NOT NULL,
  `ref_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `trm_task_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trm_task_properties` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) unsigned DEFAULT NULL,
  `property_id` bigint(20) unsigned DEFAULT NULL,
  `value` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `trm_task_suites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trm_task_suites` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `name` varchar(128) NOT NULL DEFAULT '',
  `description` mediumtext,
  `suiteData` text,
  `parameters` mediumtext,
  `enabled` tinyint(3) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `trm_tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trm_tasks` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT '',
  `description` text,
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `shared` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `project_id` bigint(20) NOT NULL,
  `agents_filter` text,
  `build` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `trm_tasks_completed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trm_tasks_completed` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `description` mediumtext NOT NULL,
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `suiteIds` varchar(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `unit_customization_values`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit_customization_values` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `unit_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `customization_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `value` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `login` varchar(45) NOT NULL DEFAULT '',
  `password` varchar(45) NOT NULL DEFAULT '',
  `email` varchar(128) NOT NULL DEFAULT '',
  `permissions` varchar(45) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`name`),
  UNIQUE KEY `unique_login` (`login`),
  KEY `unique_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 DROP PROCEDURE IF EXISTS `check_test_run_for_ict` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `check_test_run_for_ict`(in p_test_run_id bigint, in p_test_id bigint, in p_test_run_name text, in p_suite_run_id bigint, in p_test_run_reason text)
BEGIN
	declare sr_parameters mediumtext;
	declare ic_id bigint;
  DECLARE CONTINUE HANDLER FOR 1329 SET ic_id = 0;
	
	select sr.parameters into sr_parameters from suite_runs sr where sr.id = p_suite_run_id;
	set ic_id = 1;
  select ic.id into ic_id from issue_collations ic, issue_collation_tests ict where ict.issue_collation_id = ic.id and ict.test_id = p_test_id limit 0, 1;
	if ic_id>0 then
		insert into cron_ict_test_runs (test_run_id, test_id, suite_run_id, reason, suite_run_parameters, issue_collation_id)
			values (p_test_run_id, p_test_id, p_suite_run_id, p_test_run_reason, sr_parameters, ic_id);
	end if;
  select ic.id into ic_id from issue_collations ic, issue_collation_tests ict where ict.issue_collation_id = ic.id and ict.test_name = p_test_run_name limit 0, 1;
	if ic_id>0 then
		insert into cron_ict_test_runs (test_run_id, test_id, suite_run_id, reason, suite_run_parameters, issue_collation_id, test_name)
			values (p_test_run_id, p_test_id, p_suite_run_id, p_test_run_reason, sr_parameters, ic_id, p_test_run_name);
	end if;
	
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `test_fill_statistics` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `test_fill_statistics`(in days integer, in start_date timestamp)
BEGIN
   
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
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `test_fill_suites` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `test_fill_suites`(in days integer, in start_date timestamp)
BEGIN
  
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
        
        insert into test_runs (suite_run_id, test_id, start_time, end_time, report, name, status, project_id)
          values (sid, 0, current_timestamp(), current_timestamp(), '','test2134',test_status, i);
        set total = total - 1;
      end while;
      set d = adddate(d, interval 1 day);
    end while;
    set i = i +1;
  end while;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_suite_statistics` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `update_suite_statistics`(
  in tr_id bigint,
  in sr_id bigint,
  in p_id   bigint,
  in tr_status enum('PASSED', 'WARNING', 'FAILED')
)
BEGIN
  declare ss_id bigint;
  declare r_id bigint;
  DECLARE CONTINUE HANDLER FOR 1329 SET ss_id = 0, r_id = 0;
  
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
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
insert into users (name, login, password, email, permissions) values ('admin', 'admin', 'admin', 'no-email@localhost', 'fffffffffff');
