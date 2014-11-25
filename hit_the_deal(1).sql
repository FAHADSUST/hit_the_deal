-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Nov 25, 2014 at 02:45 PM
-- Server version: 5.5.34
-- PHP Version: 5.4.22

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `hit_the_deal`
--

-- --------------------------------------------------------

--
-- Table structure for table `creator_type`
--

CREATE TABLE IF NOT EXISTS `creator_type` (
  `creator_type_id` int(5) NOT NULL AUTO_INCREMENT,
  `creator_type_name` varchar(20) NOT NULL,
  PRIMARY KEY (`creator_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `creator_type`
--

INSERT INTO `creator_type` (`creator_type_id`, `creator_type_name`) VALUES
(1, 'local_business'),
(2, 'reataurant'),
(3, 'cause'),
(4, 'cultural'),
(5, 'Other');

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

CREATE TABLE IF NOT EXISTS `events` (
  `event_id` int(11) NOT NULL AUTO_INCREMENT,
  `creator_id` int(11) NOT NULL,
  `event_name` varchar(100) NOT NULL,
  `event_description` varchar(200) NOT NULL,
  `start_date` bigint(20) NOT NULL,
  `end_date` bigint(20) NOT NULL,
  `longitude` double NOT NULL,
  `latitude` double NOT NULL,
  `event_img` varchar(200) NOT NULL,
  `event_url` varchar(250) NOT NULL,
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `events`
--

INSERT INTO `events` (`event_id`, `creator_id`, `event_name`, `event_description`, `start_date`, `end_date`, `longitude`, `latitude`, `event_img`, `event_url`) VALUES
(1, 4, 'Kotta morce', 'varsity gate', 1, 1, 91.831705, 24.913861, 'http://api.androidhive.info/feed/img/cosmos.jpg', 'http://api.androidhive.info/feed/img/cosmos.jpg'),
(2, 2, 'Kotta morce2', 'subid bazar', 1, 1, 91.828699, 24.912046, 'http://api.androidhive.info/feed/img/cosmos.jpg', 'http://api.androidhive.info/feed/img/cosmos.jpg'),
(3, 3, 'Kotta morce3', 'amberkhana', 1, 1, 91.848179, 24.910299, 'http://api.androidhive.info/feed/img/cosmos.jpg', 'http://api.androidhive.info/feed/img/cosmos.jpg'),
(6, 3, 'Kotta morce4', 'khamu n tor ki', 1, 1, 91.828699, 24.910299, 'http://api.androidhive.info/feed/img/cosmos.jpg', 'http://api.androidhive.info/feed/img/cosmos.jpg'),
(8, 3, 'Kotta morce5', 'khaite jamu khida lagche tor khida lage nai.ami to khudai moira jamu', 121, 1, 91.831705, 24.910299, 'http://api.androidhive.info/feed/img/cosmos.jpg', 'http://api.androidhive.info/feed/img/cosmos.jpg'),
(9, 1, 'Consert', 'Miles consert', 19520023, 19520023, 91.831705, 24.9104, 'http://api.androidhive.info/feed/img/time_best.jpg', 'http://api.androidhive.info/feed/img/nav_drawer.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `favourite`
--

CREATE TABLE IF NOT EXISTS `favourite` (
  `fav_id` int(20) NOT NULL AUTO_INCREMENT,
  `viewer_id` int(20) NOT NULL,
  `creator_id` int(20) NOT NULL,
  PRIMARY KEY (`fav_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `favourite`
--

INSERT INTO `favourite` (`fav_id`, `viewer_id`, `creator_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 1),
(4, 1, 3),
(5, 2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE IF NOT EXISTS `feedback` (
  `feedback_id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `viewer_id` int(11) NOT NULL,
  `feedback` varchar(600) NOT NULL,
  `date` bigint(20) NOT NULL,
  PRIMARY KEY (`feedback_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=36 ;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`feedback_id`, `event_id`, `viewer_id`, `feedback`, `date`) VALUES
(1, 1, 2, 'sdsdsd', 3432),
(2, 1, 1, 'sdsfdfd', 1),
(3, 1, 1, 'sdsfdfd', 1),
(4, 1, 1, 'sdsfdfd', 1),
(5, 1, 1, 'Send', 1317427200),
(6, 1, 1, 'Send', 1317427200),
(7, 1, 1, 'hhu', 1317427200),
(8, 1, 1, 'fahad', 1317427200),
(9, 1, 1, 'hasan', 1317427200),
(10, 1, 1, 'fajad', 1317427200),
(11, 1, 1, 'rail', 1317427200),
(12, 1, 1, 'kothaff', 1317427200),
(13, 1, 1, 'high', 1317427200),
(14, 1, 1, 'khamu', 1317427200),
(15, 1, 1, 'fahad1', 1317427200),
(16, 1, 4, 'rabit', 1317427200),
(17, 1, 4, 'koi', 1317427200),
(18, 1, 4, 'kll', 1317427200),
(19, 1, 4, 'boilam', 1317427200),
(20, 1, 1, 'sdsf dfddf  dfdf', 1),
(21, 1, 1, 'sdsf dsd dsfdf dfd dfddf  dfdf', 1),
(22, 1, 1, 'sdsf dsd dsfdsd sdf dfd dfddf  dfdf', 1),
(23, 1, 1, '''sdsf dsd dsfdsd sdf dfd dfddf  dfdf''', 1),
(24, 1, 1, 'kotta', 1317427200),
(25, 1, 1, 'dhon', 1317427200),
(26, 1, 1, 'tot', 1317427200000),
(27, 3, 1, 'ttyy', 1317427200000),
(28, 3, 1, 'yyhhh', 1317427200000),
(29, 3, 1, 'gyhy', 1317427200000),
(30, 1, 1, 'dfgg', 1317427200000),
(31, 9, 1, 'gghh', 1317427200000),
(32, 9, 1, 'fghhhjkkkkkkkhh', 1317427200000),
(33, 9, 1, 'srrgghjjjvhjkklgfas', 1317427200000),
(34, 9, 1, 'ghuughh', 1317427200000),
(35, 8, 1, 'hhjj', 1317427200000);

-- --------------------------------------------------------

--
-- Table structure for table `rating`
--

CREATE TABLE IF NOT EXISTS `rating` (
  `rating_id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `viewer_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  PRIMARY KEY (`rating_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Dumping data for table `rating`
--

INSERT INTO `rating` (`rating_id`, `event_id`, `viewer_id`, `rating`) VALUES
(1, 1, 2, 5),
(2, 3, 2, 3),
(3, 3, 3, 3),
(4, 3, 3, 3),
(5, 4, 1, 5),
(6, 3, 3, 3),
(7, 3, 3, 3),
(8, 3, 3, 1),
(9, 5, 1, 5),
(10, 5, 1, 5),
(11, 1, 1, 2),
(12, 1, 4, 3),
(13, 3, 1, 5),
(14, 2, 1, 3),
(15, 9, 1, 3),
(16, 8, 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_type_id` int(5) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `email` varchar(30) NOT NULL,
  `phn_no` varchar(20) NOT NULL,
  `date_of_creation` bigint(20) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `image_url` varchar(200) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `creator_type_id` int(5) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `user_type_id`, `user_name`, `address`, `email`, `phn_no`, `date_of_creation`, `latitude`, `longitude`, `image_url`, `password`, `creator_type_id`) VALUES
(1, 2, 'Fahad', 'mirpur', 'fahad@gmail.com', '23234', 1234, 0, 0, 'http://api.androidhive.info/feed/img/life.jpg', '1', 1),
(2, 1, 'ratul', 'dsd', 'ratul@gmail.com', '122', 323, 24.8997635, 91.8619037, 'http://api.androidhive.info/feed/img/life.jpg', '1', 2),
(3, 1, 'jamal', 'adsd', 'sds', '24254', 1210, 3.1452, 5.454545, 'http://api.androidhive.info/feed/img/life.jpg', '1', 1),
(4, 2, 'rabit', 'mirpur', 'rabit@gmail.com', '12345', 12354, 3.58989, 6.65656, 'http://api.androidhive.info/feed/img/life.jpg', '1', 1),
(7, 1, 'shawon', 'mirpur', 'shawon@email.com', '12345', 12354, 3.58989, 6.65656, 'fdf', 'dfdf', 1),
(8, 1, 'miraz', 'bari', 'm@g.com', '211', 1317427200, 24.906635144884678, 91.83839309960604, 'miraz.jpg', 'q', 2),
(9, 1, 'trmp', 'ffg', 'yyy@g.h', '211', 1317427200, 24.905779725906903, 91.84047751128674, 'trmp.jpg', 'gg', 3),
(10, 1, 'fahaf', 'yyuuuuuuu', 'fg@g.j', '211', 1317427200, 24.906401904677836, 91.84059150516987, 'fahaf.jpg', 'h', 2),
(11, 1, 'dragon', 'yyuuuuuuu', 'fg@g.j', '211', 1317427200, 24.906401904677836, 91.84059150516987, 'dragon.jpg', 'h', 2),
(12, 1, 'rabit', 'mirpur', 'rabitfg@email.com', '12345', 12354, 3.58989, 6.65656, 'fdf', 'dfdf', 1),
(13, 1, 'derf', 'dfff', 'fff@fff.c', '211', 1317427200, 24.906816384688884, 91.83630801737309, 'Empty', 'fff', 2),
(14, 1, 'gshhd', 'djdj', 'hshdh@hd.d', '211', 1317427200, 24.906220360169975, 91.83984819799662, 'Empty', 'hdhhd', 3),
(15, 1, 'vcx', 'xhxhx', 'gsgs@gd.f', '211', 1317427200, 24.91049707639945, 91.83547988533974, 'Empty', 'fcc', 21),
(16, 2, 'fahaf', '', 'marakha@g.x', '0', 1317427200, 0, 0, 'Empty', 's', 1),
(17, 2, 'pora', '', 'khao@h.x', '0', 1317427200, 0, 0, 'Empty', '1', 1),
(18, 2, 'moitajamu', '', 'jamugamoira@g.com', '0', 1317427200, 0, 0, 'jamugamoira@g.com.jpg', 'a', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_type`
--

CREATE TABLE IF NOT EXISTS `user_type` (
  `user_type_id` int(5) NOT NULL AUTO_INCREMENT,
  `user_type_name` varchar(10) NOT NULL,
  PRIMARY KEY (`user_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `user_type`
--

INSERT INTO `user_type` (`user_type_id`, `user_type_name`) VALUES
(1, 'creator'),
(2, 'viewer');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
