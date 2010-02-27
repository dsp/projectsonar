-- phpMyAdmin SQL Dump
-- version 2.11.7.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 24. Januar 2010 um 16:41
-- Server Version: 5.0.41
-- PHP-Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `hibernatetest`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Edge`
--

CREATE TABLE `Edge` (
  `weight` float NOT NULL,
  `createTime` int(11) NOT NULL,
  `incomingId` int(11) NOT NULL,
  `outgoingId` int(11) NOT NULL,
  PRIMARY KEY  (`incomingId`,`outgoingId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `Edge`
--

INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.323, 53, 0, 1);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.34, 54, 0, 3);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.445, 52, 1, 0);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.2745, 58, 1, 2);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.87, 53, 1, 3);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.34, 56, 2, 1);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.666, 55, 2, 3);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.263, 53, 2, 4);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.403, 55, 3, 0);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.55554, 54, 3, 2);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.232, 56, 3, 4);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.224, 60, 5, 3);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.34, 61, 5, 8);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.564, 58, 6, 5);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.234, 58, 7, 3);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.835, 59, 7, 5);
INSERT INTO `Edge` (`weight`, `createTime`, `incomingId`, `outgoingId`) VALUES(0.0001, 60, 8, 6);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Node`
--

CREATE TABLE `Node` (
  `nodeId` int(11) NOT NULL,
  `nodeName` varchar(45) NOT NULL,
  `weight` float NOT NULL,
  `createTime` int(11) NOT NULL,
  PRIMARY KEY  (`nodeId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `Node`
--

INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(0, 'n0', 0.23, 55);
INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(1, 'n1', 0.5, 50);
INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(2, 'n2', 0.2, 51);
INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(3, 'n3', 0.93, 52);
INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(4, 'n4', 0.33, 53);
INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(5, 'n5', 0.34, 54);
INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(6, 'n6', 0.4, 58);
INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(7, 'n7', 0.2, 57);
INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(8, 'n8', 0.33, 58);
INSERT INTO `Node` (`nodeId`, `nodeName`, `weight`, `createTime`) VALUES(15, 'n15!!awesome', 3.444, 56);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `User`
--

CREATE TABLE `User` (
  `id` smallint(6) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `User`
--

INSERT INTO `User` (`id`, `username`, `password`) VALUES(1, 'admin', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO `User` (`id`, `username`, `password`) VALUES(2, 'martin', '925d7518fc597af0e43f5606f9a51512');
INSERT INTO `User` (`id`, `username`, `password`) VALUES(3, 'foobar', '3858f62230ac3c915f300c664312c63f');
