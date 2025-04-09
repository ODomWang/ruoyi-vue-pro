-- 文巡用户表
CREATE TABLE `wenxun_user`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `app_id`      varchar(64)  NOT NULL COMMENT '应用ID',
    `token`       varchar(255) NOT NULL COMMENT '用户token',
    `status`      tinyint      NOT NULL DEFAULT '0' COMMENT '登录状态',
    `nickname`    varchar(50)           DEFAULT NULL COMMENT '用户昵称',
    `avatar`      varchar(255)          DEFAULT NULL COMMENT '用户头像',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_id` (`app_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文巡用户表';

-- 文巡设备表
CREATE TABLE `wenxun_device`
(
    `id`              bigint      NOT NULL AUTO_INCREMENT COMMENT '设备ID',
    `app_id`          varchar(64) NOT NULL COMMENT '应用ID',
    `device_name`     varchar(100)         DEFAULT NULL COMMENT '设备名称',
    `device_type`     varchar(50)          DEFAULT NULL COMMENT '设备类型',
    `last_login_time` datetime             DEFAULT NULL COMMENT '最后登录时间',
    `create_time`     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         bit(1)      NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_app_id` (`app_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文巡设备表';

-- 文巡登录日志表
CREATE TABLE `wenxun_login_log`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `app_id`       varchar(64) NOT NULL COMMENT '应用ID',
    `login_type`   tinyint     NOT NULL COMMENT '登录类型',
    `login_result` tinyint     NOT NULL COMMENT '登录结果',
    `login_ip`     varchar(50)          DEFAULT NULL COMMENT '登录IP',
    `user_agent`   varchar(512)         DEFAULT NULL COMMENT '用户代理',
    `create_time`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`      bit(1)      NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_app_id` (`app_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文巡登录日志表';