-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: localhost
-- Üretim Zamanı: 03 Haz 2026, 20:41:38
-- Sunucu sürümü: 10.4.32-MariaDB
-- PHP Sürümü: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `firin_db`
--
CREATE DATABASE IF NOT EXISTS firin_db;
USE firin_db;

DELIMITER $$
--
-- Yordamlar
--
CREATE PROCEDURE `BayiEkle` (IN `p_ad` VARCHAR(100), IN `p_yetkili` VARCHAR(100), IN `p_tel` VARCHAR(15), IN `p_adres` VARCHAR(250))   BEGIN
    INSERT INTO bayiler (bayi_ad, yetkili, tel, adres) VALUES (p_ad, p_yetkili, p_tel, p_adres);
END$$

CREATE PROCEDURE `BayiGuncelle` (IN `p_id` INT, IN `p_ad` VARCHAR(100), IN `p_yetkili` VARCHAR(100), IN `p_tel` VARCHAR(15), IN `p_adres` VARCHAR(250))   BEGIN
    UPDATE bayiler SET bayi_ad=p_ad, yetkili=p_yetkili, tel=p_tel, adres=p_adres WHERE bayi_id=p_id;
END$$

CREATE PROCEDURE `BayiListele` ()   BEGIN
    SELECT * FROM bayiler;
END$$

CREATE PROCEDURE `BayiSil` (IN `p_id` INT)   BEGIN
    DELETE FROM bayiler WHERE bayi_id=p_id;
END$$

CREATE PROCEDURE `TeslimatYap` (IN `p_bayi_id` INT, IN `p_urun_id` INT, IN `p_adet` INT)   BEGIN
    DECLARE p_fiyat FLOAT;
    SELECT fiyat INTO p_fiyat FROM urunler WHERE urun_id = p_urun_id;
    INSERT INTO teslimatlar (bayi_id, urun_id, adet, toplam_fiyat) VALUES (p_bayi_id, p_urun_id, p_adet, p_fiyat * p_adet);
END$$

CREATE PROCEDURE `UrunEkle` (IN `p_ad` VARCHAR(50), IN `p_fiyat` FLOAT, IN `p_stok` INT)   BEGIN
    INSERT INTO urunler (urun_ad, fiyat, stok) VALUES (p_ad, p_fiyat, p_stok);
END$$

CREATE PROCEDURE `UrunListele` ()   BEGIN
    SELECT * FROM urunler;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `bayiler`
--

CREATE TABLE `bayiler` (
  `bayi_id` int(11) NOT NULL,
  `bayi_ad` varchar(100) NOT NULL,
  `yetkili` varchar(100) NOT NULL,
  `tel` varchar(15) NOT NULL,
  `adres` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `bayiler`
--

INSERT INTO `bayiler` (`bayi_id`, `bayi_ad`, `yetkili`, `tel`, `adres`) VALUES
(1, 'yepas1', 'Cengiz Sahin', '0555 555 55 55', 'sanayi sitesi kaporta sok.');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `teslimatlar`
--

CREATE TABLE `teslimatlar` (
  `teslimat_id` int(11) NOT NULL,
  `bayi_id` int(11) NOT NULL,
  `urun_id` int(11) NOT NULL,
  `adet` int(11) NOT NULL,
  `toplam_fiyat` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `teslimatlar`
--

INSERT INTO `teslimatlar` (`teslimat_id`, `bayi_id`, `urun_id`, `adet`, `toplam_fiyat`) VALUES
(1, 1, 2, 100, 5000);

--
-- Tetikleyiciler `teslimatlar`
--
DELIMITER $$
CREATE TRIGGER `trg_stok_dus` AFTER INSERT ON `teslimatlar` FOR EACH ROW BEGIN
    UPDATE urunler SET stok = stok - NEW.adet WHERE urun_id = NEW.urun_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_stok_kontrol` BEFORE INSERT ON `teslimatlar` FOR EACH ROW BEGIN
    DECLARE mevcut_stok INT;
    SELECT stok INTO mevcut_stok FROM urunler WHERE urun_id = NEW.urun_id;
    IF NEW.adet > mevcut_stok THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Yetersiz stok! Teslimat yapılamaz.';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `urunler`
--

CREATE TABLE `urunler` (
  `urun_id` int(11) NOT NULL,
  `urun_ad` varchar(50) NOT NULL,
  `fiyat` float NOT NULL,
  `stok` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `urunler`
--

INSERT INTO `urunler` (`urun_id`, `urun_ad`, `fiyat`, `stok`) VALUES
(1, 'Somun Ekmek', 10, 10000),
(2, 'Çavdar Ekmeği', 15, 9900);

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `bayiler`
--
ALTER TABLE `bayiler`
  ADD PRIMARY KEY (`bayi_id`);

--
-- Tablo için indeksler `teslimatlar`
--
ALTER TABLE `teslimatlar`
  ADD PRIMARY KEY (`teslimat_id`),
  ADD KEY `bayi_id` (`bayi_id`),
  ADD KEY `urun_id` (`urun_id`);

--
-- Tablo için indeksler `urunler`
--
ALTER TABLE `urunler`
  ADD PRIMARY KEY (`urun_id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `bayiler`
--
ALTER TABLE `bayiler`
  MODIFY `bayi_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Tablo için AUTO_INCREMENT değeri `teslimatlar`
--
ALTER TABLE `teslimatlar`
  MODIFY `teslimat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Tablo için AUTO_INCREMENT değeri `urunler`
--
ALTER TABLE `urunler`
  MODIFY `urun_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `teslimatlar`
--
ALTER TABLE `teslimatlar`
  ADD CONSTRAINT `teslimatlar_ibfk_1` FOREIGN KEY (`bayi_id`) REFERENCES `bayiler` (`bayi_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `teslimatlar_ibfk_2` FOREIGN KEY (`urun_id`) REFERENCES `urunler` (`urun_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
