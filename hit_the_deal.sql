-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 13, 2014 at 05:52 PM
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
(1, 'Business'),
(2, 'Restaurant'),
(3, 'Cause'),
(4, 'Cultural'),
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=24 ;

--
-- Dumping data for table `events`
--

INSERT INTO `events` (`event_id`, `creator_id`, `event_name`, `event_description`, `start_date`, `end_date`, `longitude`, `latitude`, `event_img`, `event_url`) VALUES
(21, 39, '50% discount', 'All our software.there have 50%discount.stock limited.', 1418460360821, 1423817160838, 90.38730159401894, 23.79207027371964, 'c@g.com1418460592147.jpg', 'about:blank'),
(22, 40, '20% discount e bufe dinner', 'bufe khao.ja icha tai.1500 takar bufe matro 1000 takai', 1418467920063, 1423824720767, 90.40264952927828, 23.79833861662597, 'radison@gmail.com1418468002478.jpg', ''),
(23, 2, 'Consert', 'this is shironamhin consert.will be held in golshan 1 no.', 1418988420542, 1419074820578, 90.40857452899218, 23.791083656309294, 'ratul@gmail.com1418470140441.jpg', '');

-- --------------------------------------------------------

--
-- Table structure for table `favourite`
--

CREATE TABLE IF NOT EXISTS `favourite` (
  `fav_id` int(20) NOT NULL AUTO_INCREMENT,
  `viewer_id` int(20) NOT NULL,
  `creator_id` int(20) NOT NULL,
  PRIMARY KEY (`fav_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=21 ;

--
-- Dumping data for table `favourite`
--

INSERT INTO `favourite` (`fav_id`, `viewer_id`, `creator_id`) VALUES
(16, 1, 1),
(17, 1, 2),
(19, 41, 39),
(20, 41, 2);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=171 ;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`feedback_id`, `event_id`, `viewer_id`, `feedback`, `date`) VALUES
(159, 22, 1, 'helli i want to go', 1418468252627),
(160, 21, 1, 'i am the boy', 1418468308908),
(161, 22, 41, 'hello i also want to go with my brother.', 1418468455652),
(162, 21, 41, 'koi tumi', 1418468570220),
(163, 22, 41, 'hello', 1418468671667),
(164, 21, 41, 'hello', 1418469088415),
(165, 21, 41, 'hei', 1418469415717),
(166, 21, 41, 'how are you', 1418469423872),
(167, 21, 41, 'how are you', 1418469483128),
(168, 21, 41, 'ddd', 1418469492014),
(169, 21, 41, 'rrrr', 1418469497491),
(170, 23, 2, 'hei this is shironamhin consert.', 1418470198138);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=36 ;

--
-- Dumping data for table `rating`
--

INSERT INTO `rating` (`rating_id`, `event_id`, `viewer_id`, `rating`) VALUES
(31, 22, 1, 4),
(32, 21, 1, 4),
(33, 22, 41, 4),
(34, 21, 41, 2),
(35, 23, 41, 4);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=42 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `user_type_id`, `user_name`, `address`, `email`, `phn_no`, `date_of_creation`, `latitude`, `longitude`, `image_url`, `password`, `creator_type_id`) VALUES
(1, 2, 'Fahad', 'mirpur 14', 'fahad@gmail.com', '016883637839', 1234, 0, 0, 'fahad@gmail.com_profic_1418460694949.jpg', '1', 2),
(2, 1, 'ratul', 'akhalia shurma', 'ratul@gmail.com', '01925819967', 323, 24.8997635, 91.8619037, 'ratul@gmail.com.jpg', '1', 1),
(39, 1, 'Cuadrolab', 'Uttara dhaka', 'c@g.com', '0', 1418460265103, 23.797582737935226, 90.3840534389019, 'c@g.com_profic_1418460296283.jpg', '1', 2),
(40, 1, 'Redison hotel', 'mes.bonani.dhaka', 'radison@gmail.com', '01675906629', 1418467797969, 23.7982364628229, 90.38331113755703, 'radison@gmail.com_profic_1418468097622.jpg', '1', 4),
(41, 2, 'Touhid', 'Bonani ,Dhaka', 'touhid@gmail.com', '01675906629', 1418468392366, 0, 0, 'touhid@gmail.com_profic_1418468392174.jpg', '1', 1);

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
