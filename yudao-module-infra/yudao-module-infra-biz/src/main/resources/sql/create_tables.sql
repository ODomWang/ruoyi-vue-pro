-- 创建文档表
CREATE TABLE IF NOT EXISTS `infra_document` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文档编号',
  `parent_id` bigint DEFAULT '0' COMMENT '父级文档编号，如果是顶级文档则为0',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文档标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '文档内容',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '文档状态(0-草稿, 1-已发布, 2-已归档)',
  `last_updated_by` bigint DEFAULT NULL COMMENT '最后修改人的用户编号',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本号',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_title` (`title`) USING BTREE COMMENT '标题索引',
  KEY `idx_parent_id` (`parent_id`) USING BTREE COMMENT '父级文档索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档表'; 