CREATE TABLE `wenxun_wx_account`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '编号',
    `app_id`      varchar(32) NOT NULL COMMENT '应用ID',
    `wx_id`       varchar(64) NOT NULL COMMENT '微信ID',
    `status`      tinyint     NOT NULL COMMENT '状态',
    `creator`     varchar(64)          DEFAULT '' COMMENT '创建者',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64)          DEFAULT '' COMMENT '更新者',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)      NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_app_id` (`app_id`),
    KEY `idx_wx_id` (`wx_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='微信账号表';