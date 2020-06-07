CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) DEFAULT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT uc_email UNIQUE (email)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8