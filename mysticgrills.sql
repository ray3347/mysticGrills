-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 12, 2023 at 10:09 PM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mysticgrills`
--

-- --------------------------------------------------------

--
-- Table structure for table `menuitem`
--

CREATE TABLE `menuitem` (
  `menuItemId` int(11) NOT NULL,
  `menuItemName` varchar(100) DEFAULT NULL,
  `menuItemDescription` varchar(225) DEFAULT NULL,
  `menuItemPrice` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `menuitem`
--

INSERT INTO `menuitem` (`menuItemId`, `menuItemName`, `menuItemDescription`, `menuItemPrice`) VALUES
(1, 'Chicken Burger', 'Grilled chicken burger with lettuce and mayo', 8),
(2, 'Vegetarian Pizza', 'Vegetarian pizza with tomatoes, olives, and mushrooms', 12),
(3, 'Spaghetti Bolognese', 'Classic spaghetti with meat sauce', 10),
(4, 'Caesar Salad', 'Fresh salad with romaine lettuce, croutons, and Caesar dressing', 6),
(5, 'Chocolate Brownie Sundae', 'Warm chocolate brownie topped with vanilla ice cream', 7);

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `orderId` int(11) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `orderStatus` varchar(10) DEFAULT NULL,
  `orderDate` datetime DEFAULT NULL,
  `orderTotal` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `order`
--

INSERT INTO `order` (`orderId`, `userId`, `orderStatus`, `orderDate`, `orderTotal`) VALUES
(6, 1, 'Pending', '2023-12-12 19:16:14', 56),
(7, 1, 'Paid', '2023-12-12 21:29:51', 118),
(8, 1, 'Paid', '2023-12-12 21:30:42', 86),
(9, 1, 'Paid', '2023-12-12 22:57:55', 147),
(10, 2, 'Pending', '2023-12-12 23:00:22', 114),
(11, 2, 'Served', '2023-12-13 00:03:32', 316),
(12, 2, 'Pending', '2023-12-13 03:16:16', 139),
(13, 2, 'Paid', '2023-12-13 03:18:39', 12),
(14, 2, 'Pending', '2023-12-13 03:18:49', 139);

-- --------------------------------------------------------

--
-- Table structure for table `orderitem`
--

CREATE TABLE `orderitem` (
  `orderId` int(11) DEFAULT NULL,
  `menuItemId` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `orderitem`
--

INSERT INTO `orderitem` (`orderId`, `menuItemId`, `quantity`) VALUES
(6, 3, 2),
(6, 2, 3),
(7, 2, 2),
(7, 3, 7),
(7, 2, 2),
(8, 1, 2),
(8, 3, 7),
(9, 1, 1),
(9, 2, 2),
(9, 3, 4),
(9, 4, 7),
(9, 4, 2),
(9, 5, 3),
(10, 1, 3),
(10, 3, 9),
(11, 2, 25),
(11, 1, 2),
(12, 1, 6),
(12, 5, 13),
(13, 2, 1),
(14, 1, 6),
(14, 5, 13);

-- --------------------------------------------------------

--
-- Table structure for table `receipt`
--

CREATE TABLE `receipt` (
  `receiptId` int(11) NOT NULL,
  `orderId` int(11) DEFAULT NULL,
  `receiptPaymentAmount` int(11) DEFAULT NULL,
  `receiptPaymentDate` datetime DEFAULT NULL,
  `receiptPaymentType` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `receipt`
--

INSERT INTO `receipt` (`receiptId`, `orderId`, `receiptPaymentAmount`, `receiptPaymentDate`, `receiptPaymentType`) VALUES
(1, 9, 150, '2023-12-13 03:12:31', 'Debit'),
(2, 7, 120, '2023-12-13 03:14:10', 'Card'),
(3, 8, 90, '2023-12-13 03:15:48', 'Credit'),
(4, 13, 15, '2023-12-13 03:20:15', 'Cash');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userId` int(11) NOT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `userEmail` varchar(225) DEFAULT NULL,
  `userPassword` varchar(225) DEFAULT NULL,
  `userRole` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userId`, `userName`, `userEmail`, `userPassword`, `userRole`) VALUES
(1, 'ansel', 'ansel@binus.com', 'anselmus', 'Customer'),
(2, 'kaes', 'kaes@binus.com', 'asdasd', 'Customer'),
(3, 'admin', 'admin', 'admin123', 'Admin'),
(4, 'chef', 'chef', 'chef123', 'Chef'),
(5, 'waiter', 'waiter', 'waiter123', 'Waiter'),
(6, 'cashier', 'cashier', 'cashier123', 'Cashier');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `menuitem`
--
ALTER TABLE `menuitem`
  ADD PRIMARY KEY (`menuItemId`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`orderId`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `orderitem`
--
ALTER TABLE `orderitem`
  ADD KEY `orderId` (`orderId`),
  ADD KEY `menuItemId` (`menuItemId`);

--
-- Indexes for table `receipt`
--
ALTER TABLE `receipt`
  ADD PRIMARY KEY (`receiptId`),
  ADD KEY `orderId` (`orderId`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `menuitem`
--
ALTER TABLE `menuitem`
  MODIFY `menuItemId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `orderId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `receipt`
--
ALTER TABLE `receipt`
  MODIFY `receiptId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`);

--
-- Constraints for table `orderitem`
--
ALTER TABLE `orderitem`
  ADD CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`orderId`) REFERENCES `order` (`orderId`),
  ADD CONSTRAINT `orderitem_ibfk_2` FOREIGN KEY (`menuItemId`) REFERENCES `menuitem` (`menuItemId`);

--
-- Constraints for table `receipt`
--
ALTER TABLE `receipt`
  ADD CONSTRAINT `receipt_ibfk_1` FOREIGN KEY (`orderId`) REFERENCES `order` (`orderId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
