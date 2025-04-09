CREATE TABLE `wenxun_wx_contact`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '编号',
    `app_id`        varchar(32)  NOT NULL COMMENT '应用ID',
    `wx_id`         varchar(64)  NOT NULL COMMENT '微信ID',
    `friend_wx_id`  varchar(64)  NOT NULL COMMENT '好友微信ID',
    `wx_code`       varchar(64)           DEFAULT NULL COMMENT '微信号',
    `nick_name`     varchar(100)          DEFAULT NULL COMMENT '昵称',
    `avatar`        varchar(500)          DEFAULT NULL COMMENT '头像',
    `remark`        varchar(100)          DEFAULT NULL COMMENT '备注',
    `tags`          varchar(500)          DEFAULT NULL COMMENT '标签，多个标签以逗号分隔',
    `phone_number`  varchar(20)           DEFAULT NULL COMMENT '手机号',
    `is_star_friend` bit(1)               DEFAULT b'0' COMMENT '是否星标好友',
    `creator`       varchar(64)           DEFAULT '' COMMENT '创建者',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`       varchar(64)           DEFAULT '' COMMENT '更新者',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_wx_friend_app` (`wx_id`, `friend_wx_id`, `app_id`),
    KEY `idx_wx_id` (`wx_id`),
    KEY `idx_app_id` (`app_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='微信联系人表'; 