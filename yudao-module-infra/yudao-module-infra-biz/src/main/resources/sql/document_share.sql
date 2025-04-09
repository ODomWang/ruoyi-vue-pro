-- 创建文档分享表
CREATE TABLE IF NOT EXISTS `infra_document_share` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分享编号',
  `share_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分享链接的标识',
  `document_id` bigint NOT NULL COMMENT '文档编号',
  `user_id` bigint NOT NULL COMMENT '创建人的用户编号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分享密码，允许为空',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间，为空则永久有效',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '分享状态：0-正常，1-已过期，2-已禁用',
  `password_protected` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否需要密码访问：0-不需要，1-需要',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_share_id` (`share_id`) USING BTREE COMMENT '分享标识唯一索引',
  KEY `idx_document_id` (`document_id`) USING BTREE COMMENT '文档编号索引',
  KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户编号索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文档分享表'; 