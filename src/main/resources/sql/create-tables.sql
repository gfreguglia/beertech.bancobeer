DROP TABLE IF EXISTS `transacoes`;

CREATE TABLE `transacoes` (
  `id` int(9) unsigned NOT NULL AUTO_INCREMENT,
  `datahora` datetime NOT NULL,
  `valor` decimal(15,2) NOT NULL,
  `tipo` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;