This is a personal knowledge base deployed locally, based on the Vue3 and SpringBoot3 frameworks. It includes an integrated whiteboard and rich text editor.
You need to create two tables with DDL before using it.
//
CREATE TABLE `articlenote` (
  `id` bigint NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `updatetime` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
//
CREATE TABLE `articles` (
  `id` bigint NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `context` longtext,
  `createtime` date DEFAULT NULL,
  `updatetime` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

You may also need an Alibaba Cloud account to purchase an OSS bucket.
