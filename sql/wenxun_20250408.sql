create table QRTZ_CALENDARS
(
    SCHED_NAME    varchar(120) not null,
    CALENDAR_NAME varchar(190) not null,
    CALENDAR      blob         not null,
    primary key (SCHED_NAME, CALENDAR_NAME)
)
    collate = utf8mb4_unicode_ci;

create table QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME        varchar(120) not null,
    ENTRY_ID          varchar(95)  not null,
    TRIGGER_NAME      varchar(190) not null,
    TRIGGER_GROUP     varchar(190) not null,
    INSTANCE_NAME     varchar(190) not null,
    FIRED_TIME        bigint       not null,
    SCHED_TIME        bigint       not null,
    PRIORITY          int          not null,
    STATE             varchar(16)  not null,
    JOB_NAME          varchar(190) null,
    JOB_GROUP         varchar(190) null,
    IS_NONCONCURRENT  varchar(1)   null,
    REQUESTS_RECOVERY varchar(1)   null,
    primary key (SCHED_NAME, ENTRY_ID)
)
    collate = utf8mb4_unicode_ci;

create index IDX_QRTZ_FT_INST_JOB_REQ_RCVRY
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);

create index IDX_QRTZ_FT_JG
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_GROUP);

create index IDX_QRTZ_FT_J_G
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);

create index IDX_QRTZ_FT_TG
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);

create index IDX_QRTZ_FT_TRIG_INST_NAME
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME);

create index IDX_QRTZ_FT_T_G
    on QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

create table QRTZ_JOB_DETAILS
(
    SCHED_NAME        varchar(120) not null,
    JOB_NAME          varchar(190) not null,
    JOB_GROUP         varchar(190) not null,
    DESCRIPTION       varchar(250) null,
    JOB_CLASS_NAME    varchar(250) not null,
    IS_DURABLE        varchar(1)   not null,
    IS_NONCONCURRENT  varchar(1)   not null,
    IS_UPDATE_DATA    varchar(1)   not null,
    REQUESTS_RECOVERY varchar(1)   not null,
    JOB_DATA          blob         null,
    primary key (SCHED_NAME, JOB_NAME, JOB_GROUP)
)
    collate = utf8mb4_unicode_ci;

create index IDX_QRTZ_J_GRP
    on QRTZ_JOB_DETAILS (SCHED_NAME, JOB_GROUP);

create index IDX_QRTZ_J_REQ_RECOVERY
    on QRTZ_JOB_DETAILS (SCHED_NAME, REQUESTS_RECOVERY);

create table QRTZ_LOCKS
(
    SCHED_NAME varchar(120) not null,
    LOCK_NAME  varchar(40)  not null,
    primary key (SCHED_NAME, LOCK_NAME)
)
    collate = utf8mb4_unicode_ci;

create table QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME    varchar(120) not null,
    TRIGGER_GROUP varchar(190) not null,
    primary key (SCHED_NAME, TRIGGER_GROUP)
)
    collate = utf8mb4_unicode_ci;

create table QRTZ_SCHEDULER_STATE
(
    SCHED_NAME        varchar(120) not null,
    INSTANCE_NAME     varchar(190) not null,
    LAST_CHECKIN_TIME bigint       not null,
    CHECKIN_INTERVAL  bigint       not null,
    primary key (SCHED_NAME, INSTANCE_NAME)
)
    collate = utf8mb4_unicode_ci;

create table QRTZ_TRIGGERS
(
    SCHED_NAME     varchar(120) not null,
    TRIGGER_NAME   varchar(190) not null,
    TRIGGER_GROUP  varchar(190) not null,
    JOB_NAME       varchar(190) not null,
    JOB_GROUP      varchar(190) not null,
    DESCRIPTION    varchar(250) null,
    NEXT_FIRE_TIME bigint       null,
    PREV_FIRE_TIME bigint       null,
    PRIORITY       int          null,
    TRIGGER_STATE  varchar(16)  not null,
    TRIGGER_TYPE   varchar(8)   not null,
    START_TIME     bigint       not null,
    END_TIME       bigint       null,
    CALENDAR_NAME  varchar(190) null,
    MISFIRE_INSTR  smallint     null,
    JOB_DATA       blob         null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint qrtz_triggers_ibfk_1
        foreign key (SCHED_NAME, JOB_NAME, JOB_GROUP) references QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
)
    collate = utf8mb4_unicode_ci;

create table QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    varchar(120) not null,
    TRIGGER_NAME  varchar(190) not null,
    TRIGGER_GROUP varchar(190) not null,
    BLOB_DATA     blob         null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint qrtz_blob_triggers_ibfk_1
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
    collate = utf8mb4_unicode_ci;

create index SCHED_NAME
    on QRTZ_BLOB_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

create table QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      varchar(120) not null,
    TRIGGER_NAME    varchar(190) not null,
    TRIGGER_GROUP   varchar(190) not null,
    CRON_EXPRESSION varchar(120) not null,
    TIME_ZONE_ID    varchar(80)  null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint qrtz_cron_triggers_ibfk_1
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
    collate = utf8mb4_unicode_ci;

create table QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      varchar(120) not null,
    TRIGGER_NAME    varchar(190) not null,
    TRIGGER_GROUP   varchar(190) not null,
    REPEAT_COUNT    bigint       not null,
    REPEAT_INTERVAL bigint       not null,
    TIMES_TRIGGERED bigint       not null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint qrtz_simple_triggers_ibfk_1
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
    collate = utf8mb4_unicode_ci;

create table QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    varchar(120)   not null,
    TRIGGER_NAME  varchar(190)   not null,
    TRIGGER_GROUP varchar(190)   not null,
    STR_PROP_1    varchar(512)   null,
    STR_PROP_2    varchar(512)   null,
    STR_PROP_3    varchar(512)   null,
    INT_PROP_1    int            null,
    INT_PROP_2    int            null,
    LONG_PROP_1   bigint         null,
    LONG_PROP_2   bigint         null,
    DEC_PROP_1    decimal(13, 4) null,
    DEC_PROP_2    decimal(13, 4) null,
    BOOL_PROP_1   varchar(1)     null,
    BOOL_PROP_2   varchar(1)     null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint qrtz_simprop_triggers_ibfk_1
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
    collate = utf8mb4_unicode_ci;

create index IDX_QRTZ_T_C
    on QRTZ_TRIGGERS (SCHED_NAME, CALENDAR_NAME);

create index IDX_QRTZ_T_G
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);

create index IDX_QRTZ_T_J
    on QRTZ_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);

create index IDX_QRTZ_T_JG
    on QRTZ_TRIGGERS (SCHED_NAME, JOB_GROUP);

create index IDX_QRTZ_T_NEXT_FIRE_TIME
    on QRTZ_TRIGGERS (SCHED_NAME, NEXT_FIRE_TIME);

create index IDX_QRTZ_T_NFT_MISFIRE
    on QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);

create index IDX_QRTZ_T_NFT_ST
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);

create index IDX_QRTZ_T_NFT_ST_MISFIRE
    on QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);

create index IDX_QRTZ_T_NFT_ST_MISFIRE_GRP
    on QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP, TRIGGER_STATE);

create index IDX_QRTZ_T_N_G_STATE
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);

create index IDX_QRTZ_T_N_STATE
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);

create index IDX_QRTZ_T_STATE
    on QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE);

create table ai_api_key
(
    id          bigint auto_increment comment '编号'
        primary key,
    name        varchar(255)                         not null comment '名称',
    api_key     varchar(255)                         not null comment '密钥',
    platform    varchar(255)                         not null comment '平台',
    url         varchar(255)                         not null comment 'API 地址',
    status      int                                  not null comment '状态',
    create_time datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator     varchar(255)                         not null comment '创建者',
    updater     varchar(255)                         not null comment '更新者',
    deleted     tinyint(1) default 0                 not null comment '是否删除'
)
    comment 'AI API 秘钥表' charset = utf8mb4;

create table ai_chat_message
(
    id              bigint auto_increment comment '编号，作为每条聊天记录的唯一标识符'
        primary key,
    conversation_id bigint                               null comment '对话编号，关联 ai_chat_conversation 表的 id 字段',
    reply_id        bigint                               null comment '回复消息编号，关联自身的 id 字段，用于“问答”关联',
    type            varchar(255)                         null comment '消息类型，等价于 OpenAPI 的 role 字段',
    user_id         bigint                               null comment '用户编号，关联 admin_user 表的 user_id 字段',
    role_id         bigint                               null comment '角色编号，关联 ai_chat_role 表的 id 字段',
    model           varchar(255)                         null comment '模型标志',
    model_id        bigint                               null comment '模型编号，关联 ai_chat_model 表的 id 字段',
    content         text                                 null comment '聊天内容',
    use_context     tinyint(1)                           null comment '是否携带上下文',
    create_time     timestamp  default CURRENT_TIMESTAMP null comment '创建时间',
    update_time     timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator         varchar(255)                         null comment '创建者，SysUser 的 id 编号',
    updater         varchar(255)                         null comment '更新者，SysUser 的 id 编号',
    deleted         tinyint(1) default 0                 null comment '是否删除'
)
    comment 'AI Chat 消息表，用于存储聊天消息记录' charset = utf8mb4;

create table ai_chat_model
(
    id           bigint auto_increment comment '编号'
        primary key,
    key_id       bigint                               not null comment 'API 秘钥编号',
    name         varchar(255)                         not null comment '模型名称',
    model        varchar(255)                         not null comment '模型标志',
    platform     varchar(255)                         not null comment '平台',
    sort         int                                  not null comment '排序值',
    status       int                                  not null comment '状态',
    temperature  double                               not null comment '温度参数',
    max_tokens   int                                  not null comment '单条回复的最大 Token 数量',
    max_contexts int                                  not null comment '上下文的最大 Message 数量',
    create_time  datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator      varchar(255)                         not null comment '创建者',
    updater      varchar(255)                         not null comment '更新者',
    deleted      tinyint(1) default 0                 not null comment '是否删除',
    constraint ai_chat_model_ibfk_1
        foreign key (key_id) references ai_api_key (id)
            on delete cascade
)
    comment 'AI 聊天模型表' charset = utf8mb4;

create index key_id
    on ai_chat_model (key_id);

create table ai_chat_role
(
    id             bigint auto_increment comment '编号'
        primary key,
    name           varchar(255)                           not null comment '角色名称',
    avatar         varchar(255)                           not null comment '角色头像',
    category       varchar(255) default '0'               not null comment '角色分类',
    description    text                                   not null comment '角色描述',
    system_message text                                   not null comment '角色设定',
    user_id        bigint                                 not null comment '用户编号',
    model_id       bigint       default 1                 not null comment '模型编号',
    public_status  tinyint(1)   default 0                 not null comment '是否公开',
    sort           int                                    not null comment '排序值',
    status         int                                    not null comment '状态',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator        varchar(255)                           not null comment '创建者',
    updater        varchar(255)                           not null comment '更新者',
    deleted        tinyint(1)   default 0                 not null comment '是否删除',
    constraint ai_chat_role_ibfk_1
        foreign key (model_id) references ai_chat_model (id)
            on delete cascade
)
    comment 'AI 聊天角色表' charset = utf8mb4;

create table ai_chat_conversation
(
    id             bigint auto_increment comment 'ID 编号，自增'
        primary key,
    user_id        bigint                                 not null comment '用户编号，关联 AdminUserDO 的 userId 字段',
    title          varchar(255) default '新对话'          null comment '对话标题，默认由系统自动生成，可用户手动修改',
    pinned         tinyint(1)   default 0                 null comment '是否置顶',
    pinned_time    datetime                               null comment '置顶时间',
    role_id        bigint                                 null comment '角色编号，关联 ai_chat_role 表的 id 字段',
    model_id       bigint                                 not null comment '模型编号，关联 ai_chat_model 表的 id 字段',
    model          varchar(255)                           not null comment '模型标志',
    system_message text                                   null comment '角色设定',
    temperature    double                                 null comment '温度参数，用于调整生成回复的随机性和多样性程度',
    max_tokens     int                                    null comment '单条回复的最大 Token 数量',
    max_contexts   int                                    null comment '上下文的最大 Message 数量',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator        varchar(255)                           not null comment '创建者',
    updater        varchar(255)                           not null comment '更新者',
    deleted        tinyint(1)   default 0                 not null comment '是否删除',
    constraint ai_chat_conversation_ibfk_1
        foreign key (role_id) references ai_chat_role (id)
            on delete cascade,
    constraint ai_chat_conversation_ibfk_2
        foreign key (model_id) references ai_chat_model (id)
            on delete cascade
)
    comment 'AI Chat 对话表' charset = utf8mb4;

create index model_id
    on ai_chat_conversation (model_id);

create index role_id
    on ai_chat_conversation (role_id);

create index model_id
    on ai_chat_role (model_id);

create table ai_image
(
    id            bigint auto_increment comment '编号'
        primary key,
    user_id       bigint                               not null comment '用户编号',
    prompt        text                                 not null comment '提示词',
    platform      varchar(255)                         not null comment '平台',
    model         varchar(255)                         not null comment '模型',
    width         int                                  not null comment '图片宽度',
    height        int                                  not null comment '图片高度',
    status        int                                  not null comment '生成状态',
    finish_time   datetime                             null comment '完成时间',
    error_message text                                 null comment '绘画错误信息',
    pic_url       varchar(255)                         null comment '图片地址',
    public_status tinyint(1) default 0                 not null comment '是否公开',
    options       json                                 null comment '绘制参数，不同平台的不同参数',
    buttons       json                                 null comment 'mj buttons 按钮',
    task_id       varchar(255)                         null comment '任务编号',
    create_time   datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator       varchar(255)                         not null comment '创建者',
    updater       varchar(255)                         not null comment '更新者',
    deleted       tinyint(1) default 0                 not null comment '是否删除'
)
    comment 'AI 绘画表' charset = utf8mb4;

create table ai_knowledge
(
    id                     bigint auto_increment comment '编号'
        primary key,
    user_id                bigint                               not null comment '用户编号',
    name                   varchar(255)                         not null comment '知识库名称',
    description            text                                 not null comment '知识库描述',
    visibility_permissions json                                 not null comment '可见权限',
    model_id               bigint                               not null comment '嵌入模型编号',
    model                  varchar(255)                         not null comment '模型标识',
    status                 int                                  not null comment '状态',
    create_time            datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time            datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator                varchar(255)                         not null comment '创建者',
    updater                varchar(255)                         not null comment '更新者',
    deleted                tinyint(1) default 0                 not null comment '是否删除'
)
    comment 'AI 知识库表' charset = utf8mb4;

create table ai_knowledge_document
(
    id           bigint auto_increment comment '编号'
        primary key,
    knowledge_id bigint                               not null comment '知识库编号',
    name         varchar(255)                         not null comment '文件名称',
    content      text                                 not null comment '内容',
    url          varchar(255)                         not null comment '文件 URL',
    tokens       int                                  not null comment 'token 数量',
    word_count   int                                  not null comment '字符数',
    slice_status int                                  not null comment '切片状态',
    status       int                                  not null comment '状态',
    create_time  datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator      varchar(255)                         not null comment '创建者',
    updater      varchar(255)                         not null comment '更新者',
    deleted      tinyint(1) default 0                 not null comment '是否删除',
    constraint ai_knowledge_document_ibfk_1
        foreign key (knowledge_id) references ai_knowledge (id)
            on delete cascade
)
    comment 'AI 知识库文档表' charset = utf8mb4;

create index knowledge_id
    on ai_knowledge_document (knowledge_id);

create table ai_knowledge_segment
(
    id           bigint auto_increment comment '编号'
        primary key,
    vector_id    varchar(255)                         not null comment '向量库的编号',
    knowledge_id bigint                               not null comment '知识库编号',
    document_id  bigint                               not null comment '文档编号',
    content      text                                 not null comment '切片内容',
    word_count   int                                  not null comment '字符数',
    tokens       int                                  not null comment 'token 数量',
    status       int                                  not null comment '状态',
    create_time  datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator      varchar(255)                         not null comment '创建者',
    updater      varchar(255)                         not null comment '更新者',
    deleted      tinyint(1) default 0                 not null comment '是否删除',
    constraint ai_knowledge_segment_ibfk_1
        foreign key (knowledge_id) references ai_knowledge (id)
            on delete cascade,
    constraint ai_knowledge_segment_ibfk_2
        foreign key (document_id) references ai_knowledge_document (id)
            on delete cascade
)
    comment 'AI 知识库文档分段表' charset = utf8mb4;

create index document_id
    on ai_knowledge_segment (document_id);

create index knowledge_id
    on ai_knowledge_segment (knowledge_id);

create table ai_mind_map
(
    id                bigint auto_increment comment '编号'
        primary key,
    user_id           bigint                               not null comment '用户编号',
    platform          varchar(255)                         not null comment '平台',
    model             varchar(255)                         not null comment '模型',
    prompt            text                                 not null comment '生成内容提示',
    generated_content text                                 not null comment '生成的内容',
    error_message     text                                 not null comment '错误信息',
    create_time       datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time       datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator           varchar(255)                         not null comment '创建者',
    updater           varchar(255)                         not null comment '更新者',
    deleted           tinyint(1) default 0                 not null comment '是否删除'
)
    comment 'AI 思维导图表' charset = utf8mb4;

create table ai_music
(
    id            bigint auto_increment comment '编号'
        primary key,
    user_id       bigint                               not null comment '用户编号',
    title         varchar(255)                         not null comment '音乐名称',
    lyric         text                                 not null comment '歌词',
    image_url     varchar(255)                         not null comment '图片地址',
    audio_url     varchar(255)                         not null comment '音频地址',
    video_url     varchar(255)                         not null comment '视频地址',
    status        int                                  not null comment '音乐状态',
    generate_mode int                                  not null comment '生成模式',
    description   text                                 not null comment '描述词',
    platform      varchar(255)                         not null comment '平台',
    model         varchar(255)                         not null comment '模型',
    tags          json                                 not null comment '音乐风格标签',
    duration      double                               not null comment '音乐时长',
    public_status tinyint(1) default 0                 not null comment '是否公开',
    task_id       varchar(255)                         not null comment '任务编号',
    error_message text                                 not null comment '错误信息',
    create_time   datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator       varchar(255)                         not null comment '创建者',
    updater       varchar(255)                         not null comment '更新者',
    deleted       tinyint(1) default 0                 not null comment '是否删除'
)
    comment 'AI 音乐表' charset = utf8mb4;

create table ai_write
(
    id                bigint auto_increment comment '编号'
        primary key,
    user_id           bigint                               not null comment '用户编号',
    type              int                                  not null comment '写作类型',
    platform          varchar(255)                         not null comment '平台',
    model             varchar(255)                         not null comment '模型',
    prompt            text                                 not null comment '生成内容提示',
    generated_content text                                 null comment '生成的内容',
    original_content  text                                 null comment '原文',
    length            int                                  not null comment '长度提示词',
    format            int                                  not null comment '格式提示词',
    tone              int                                  not null comment '语气提示词',
    language          int                                  not null comment '语言提示词',
    error_message     text                                 null comment '错误信息',
    create_time       datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time       datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator           varchar(255)                         null comment '创建者',
    updater           varchar(255)                         null comment '更新者',
    deleted           tinyint(1) default 0                 not null comment '是否删除'
)
    comment 'AI 写作表' charset = utf8mb4;

create table infra_api_access_log
(
    id               bigint auto_increment comment '日志主键'
        primary key,
    trace_id         varchar(64)  default ''                not null comment '链路追踪编号',
    user_id          bigint       default 0                 not null comment '用户编号',
    user_type        tinyint      default 0                 not null comment '用户类型',
    application_name varchar(50)                            not null comment '应用名',
    request_method   varchar(16)  default ''                not null comment '请求方法名',
    request_url      varchar(255) default ''                not null comment '请求地址',
    request_params   text                                   null comment '请求参数',
    response_body    text                                   null comment '响应结果',
    user_ip          varchar(50)                            not null comment '用户 IP',
    user_agent       varchar(512)                           not null comment '浏览器 UA',
    operate_module   varchar(50)                            null comment '操作模块',
    operate_name     varchar(50)                            null comment '操作名',
    operate_type     tinyint      default 0                 null comment '操作分类',
    begin_time       datetime                               not null comment '开始请求时间',
    end_time         datetime                               not null comment '结束请求时间',
    duration         int                                    not null comment '执行时长',
    result_code      int          default 0                 not null comment '结果码',
    result_msg       varchar(512) default ''                null comment '结果提示',
    creator          varchar(64)  default ''                null comment '创建者',
    create_time      datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater          varchar(64)  default ''                null comment '更新者',
    update_time      datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted          bit          default b'0'              not null comment '是否删除',
    tenant_id        bigint       default 0                 not null comment '租户编号'
)
    comment 'API 访问日志表' collate = utf8mb4_unicode_ci;

create index idx_create_time
    on infra_api_access_log (create_time);

create table infra_api_error_log
(
    id                           bigint auto_increment comment '编号'
        primary key,
    trace_id                     varchar(64)                            not null comment '链路追踪编号',
    user_id                      bigint       default 0                 not null comment '用户编号',
    user_type                    tinyint      default 0                 not null comment '用户类型',
    application_name             varchar(50)                            not null comment '应用名',
    request_method               varchar(16)                            not null comment '请求方法名',
    request_url                  varchar(255)                           not null comment '请求地址',
    request_params               varchar(8000)                          not null comment '请求参数',
    user_ip                      varchar(50)                            not null comment '用户 IP',
    user_agent                   varchar(512)                           not null comment '浏览器 UA',
    exception_time               datetime                               not null comment '异常发生时间',
    exception_name               varchar(128) default ''                not null comment '异常名',
    exception_message            text                                   not null comment '异常导致的消息',
    exception_root_cause_message text                                   not null comment '异常导致的根消息',
    exception_stack_trace        text                                   not null comment '异常的栈轨迹',
    exception_class_name         varchar(512)                           not null comment '异常发生的类全名',
    exception_file_name          varchar(512)                           not null comment '异常发生的类文件',
    exception_method_name        varchar(512)                           not null comment '异常发生的方法名',
    exception_line_number        int                                    not null comment '异常发生的方法所在行',
    process_status               tinyint                                not null comment '处理状态',
    process_time                 datetime                               null comment '处理时间',
    process_user_id              int          default 0                 null comment '处理用户编号',
    creator                      varchar(64)  default ''                null comment '创建者',
    create_time                  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater                      varchar(64)  default ''                null comment '更新者',
    update_time                  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted                      bit          default b'0'              not null comment '是否删除',
    tenant_id                    bigint       default 0                 not null comment '租户编号'
)
    comment '系统异常日志' collate = utf8mb4_unicode_ci;

create table infra_codegen_column
(
    id                       bigint auto_increment comment '编号'
        primary key,
    table_id                 bigint                                 not null comment '表编号',
    column_name              varchar(200)                           not null comment '字段名',
    data_type                varchar(100)                           not null comment '字段类型',
    column_comment           varchar(500)                           not null comment '字段描述',
    nullable                 bit                                    not null comment '是否允许为空',
    primary_key              bit                                    not null comment '是否主键',
    ordinal_position         int                                    not null comment '排序',
    java_type                varchar(32)                            not null comment 'Java 属性类型',
    java_field               varchar(64)                            not null comment 'Java 属性名',
    dict_type                varchar(200) default ''                null comment '字典类型',
    example                  varchar(64)                            null comment '数据示例',
    create_operation         bit                                    not null comment '是否为 Create 创建操作的字段',
    update_operation         bit                                    not null comment '是否为 Update 更新操作的字段',
    list_operation           bit                                    not null comment '是否为 List 查询操作的字段',
    list_operation_condition varchar(32)  default '='               not null comment 'List 查询操作的条件类型',
    list_operation_result    bit                                    not null comment '是否为 List 查询操作的返回字段',
    html_type                varchar(32)                            not null comment '显示类型',
    creator                  varchar(64)  default ''                null comment '创建者',
    create_time              datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater                  varchar(64)  default ''                null comment '更新者',
    update_time              datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted                  bit          default b'0'              not null comment '是否删除'
)
    comment '代码生成表字段定义' collate = utf8mb4_unicode_ci;

create table infra_codegen_table
(
    id                    bigint auto_increment comment '编号'
        primary key,
    data_source_config_id bigint                                 not null comment '数据源配置的编号',
    scene                 tinyint      default 1                 not null comment '生成场景',
    table_name            varchar(200) default ''                not null comment '表名称',
    table_comment         varchar(500) default ''                not null comment '表描述',
    remark                varchar(500)                           null comment '备注',
    module_name           varchar(30)                            not null comment '模块名',
    business_name         varchar(30)                            not null comment '业务名',
    class_name            varchar(100) default ''                not null comment '类名称',
    class_comment         varchar(50)                            not null comment '类描述',
    author                varchar(50)                            not null comment '作者',
    template_type         tinyint      default 1                 not null comment '模板类型',
    front_type            tinyint                                not null comment '前端类型',
    parent_menu_id        bigint                                 null comment '父菜单编号',
    master_table_id       bigint                                 null comment '主表的编号',
    sub_join_column_id    bigint                                 null comment '子表关联主表的字段编号',
    sub_join_many         bit                                    null comment '主表与子表是否一对多',
    tree_parent_column_id bigint                                 null comment '树表的父字段编号',
    tree_name_column_id   bigint                                 null comment '树表的名字字段编号',
    creator               varchar(64)  default ''                null comment '创建者',
    create_time           datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater               varchar(64)  default ''                null comment '更新者',
    update_time           datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted               bit          default b'0'              not null comment '是否删除'
)
    comment '代码生成表定义' collate = utf8mb4_unicode_ci;

create table infra_config
(
    id          bigint auto_increment comment '参数主键'
        primary key,
    category    varchar(50)                            not null comment '参数分组',
    type        tinyint                                not null comment '参数类型',
    name        varchar(100) default ''                not null comment '参数名称',
    config_key  varchar(100) default ''                not null comment '参数键名',
    value       varchar(500) default ''                not null comment '参数键值',
    visible     bit                                    not null comment '是否可见',
    remark      varchar(500)                           null comment '备注',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除'
)
    comment '参数配置表' collate = utf8mb4_unicode_ci;

create table infra_data_source_config
(
    id          bigint auto_increment comment '主键编号'
        primary key,
    name        varchar(100) default ''                not null comment '参数名称',
    url         varchar(1024)                          not null comment '数据源连接',
    username    varchar(255)                           not null comment '用户名',
    password    varchar(255) default ''                not null comment '密码',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除'
)
    comment '数据源配置表' collate = utf8mb4_unicode_ci;

create table infra_document
(
    id              bigint auto_increment comment '文档编号'
        primary key,
    title           varchar(200)                          not null comment '文档标题',
    content         text                                  null comment '文档内容',
    status          tinyint     default 0                 not null comment '文档状态(0-草稿, 1-已发布, 2-已归档)',
    last_updated_by bigint                                null comment '最后修改人的用户编号',
    version         int         default 1                 not null comment '版本号',
    remark          varchar(500)                          null comment '备注',
    creator         varchar(64) default ''                null comment '创建者',
    create_time     datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater         varchar(64) default ''                null comment '更新者',
    update_time     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted         bit         default b'0'              not null comment '是否删除',
    parent_id       bigint      default 0                 null comment '父级文档编号，如果是顶级文档则为0',
    type            int         default 0                 not null comment '文档类型'
)
    comment '文档表' collate = utf8mb4_unicode_ci;

create index idx_title
    on infra_document (title)
    comment '标题索引';

create index infra_document_parent_id_index
    on infra_document (parent_id)
    comment '索引';

create table infra_document_share
(
    id                 bigint auto_increment comment '分享编号'
        primary key,
    share_id           varchar(64)                           not null comment '分享链接的标识',
    document_id        bigint                                not null comment '文档编号',
    user_id            bigint                                not null comment '创建人的用户编号',
    password           varchar(64)                           null comment '分享密码，允许为空',
    expire_time        datetime                              null comment '过期时间，为空则永久有效',
    status             tinyint     default 0                 not null comment '分享状态：0-正常，1-已过期，2-已禁用',
    password_protected bit         default b'0'              not null comment '是否需要密码访问：0-不需要，1-需要',
    remark             varchar(500)                          null comment '备注',
    creator            varchar(64) default ''                null comment '创建者',
    create_time        datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater            varchar(64) default ''                null comment '更新者',
    update_time        datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted            bit         default b'0'              not null comment '是否删除',
    constraint uk_share_id
        unique (share_id) comment '分享标识唯一索引'
)
    comment '文档分享表' collate = utf8mb4_unicode_ci;

create index idx_document_id
    on infra_document_share (document_id)
    comment '文档编号索引';

create index idx_user_id
    on infra_document_share (user_id)
    comment '用户编号索引';

create table infra_file
(
    id          bigint auto_increment comment '文件编号'
        primary key,
    config_id   bigint                                null comment '配置编号',
    name        varchar(256)                          null comment '文件名',
    path        varchar(512)                          not null comment '文件路径',
    url         varchar(1024)                         not null comment '文件 URL',
    type        varchar(128)                          null comment '文件类型',
    size        int                                   not null comment '文件大小',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除'
)
    comment '文件表' collate = utf8mb4_unicode_ci;

create table infra_file_config
(
    id          bigint auto_increment comment '编号'
        primary key,
    name        varchar(63)                           not null comment '配置名',
    storage     tinyint                               not null comment '存储器',
    remark      varchar(255)                          null comment '备注',
    master      bit                                   not null comment '是否为主配置',
    config      varchar(4096)                         not null comment '存储配置',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除'
)
    comment '文件配置表' collate = utf8mb4_unicode_ci;

create table infra_file_content
(
    id          bigint auto_increment comment '编号'
        primary key,
    config_id   bigint                                not null comment '配置编号',
    path        varchar(512)                          not null comment '文件路径',
    content     mediumblob                            not null comment '文件内容',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除'
)
    comment '文件表' collate = utf8mb4_unicode_ci;

create table infra_job
(
    id              bigint auto_increment comment '任务编号'
        primary key,
    name            varchar(32)                           not null comment '任务名称',
    status          tinyint                               not null comment '任务状态',
    handler_name    varchar(64)                           not null comment '处理器的名字',
    handler_param   varchar(255)                          null comment '处理器的参数',
    cron_expression varchar(32)                           not null comment 'CRON 表达式',
    retry_count     int         default 0                 not null comment '重试次数',
    retry_interval  int         default 0                 not null comment '重试间隔',
    monitor_timeout int         default 0                 not null comment '监控超时时间',
    creator         varchar(64) default ''                null comment '创建者',
    create_time     datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater         varchar(64) default ''                null comment '更新者',
    update_time     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted         bit         default b'0'              not null comment '是否删除'
)
    comment '定时任务表' collate = utf8mb4_unicode_ci;

create table infra_job_log
(
    id            bigint auto_increment comment '日志编号'
        primary key,
    job_id        bigint                                  not null comment '任务编号',
    handler_name  varchar(64)                             not null comment '处理器的名字',
    handler_param varchar(255)                            null comment '处理器的参数',
    execute_index tinyint       default 1                 not null comment '第几次执行',
    begin_time    datetime                                not null comment '开始执行时间',
    end_time      datetime                                null comment '结束执行时间',
    duration      int                                     null comment '执行时长',
    status        tinyint                                 not null comment '任务状态',
    result        varchar(4000) default ''                null comment '结果数据',
    creator       varchar(64)   default ''                null comment '创建者',
    create_time   datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    updater       varchar(64)   default ''                null comment '更新者',
    update_time   datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted       bit           default b'0'              not null comment '是否删除'
)
    comment '定时任务日志表' collate = utf8mb4_unicode_ci;

create table member_address
(
    id             bigint auto_increment comment '用户收件地址主键'
        primary key,
    user_id        bigint                                 not null comment '用户编号',
    name           varchar(255)                           not null comment '收件人名称',
    mobile         varchar(20)                            not null comment '手机号',
    area_id        bigint                                 not null comment '地区编号',
    detail_address varchar(512)                           not null comment '收件详细地址',
    default_status tinyint(1) default 0                   not null comment '是否默认地址，true 为默认地址',
    create_time    datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time    datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator        varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater        varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted        tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '用户收件地址表' charset = utf8mb4;

create table member_config
(
    id                            bigint auto_increment comment '会员配置主键'
        primary key,
    point_trade_deduct_enable     tinyint(1)                             not null comment '积分抵扣开关，true 表示启用，false 表示关闭',
    point_trade_deduct_unit_price int                                    not null comment '积分抵扣单价，单位为分，1 积分抵扣多少分',
    point_trade_deduct_max_price  int                                    not null comment '积分抵扣最大值，单位为分',
    point_trade_give_point        int                                    not null comment '每消费 1 元赠送多少积分',
    create_time                   datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time                   datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator                       varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater                       varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted                       tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '会员配置表' charset = utf8mb4;

create table member_experience_record
(
    id               bigint auto_increment comment '经验记录主键'
        primary key,
    user_id          bigint                                 not null comment '用户编号，关联 MemberUserDO 的 id 字段',
    biz_type         tinyint                                not null comment '业务类型，枚举 MemberExperienceBizTypeEnum',
    biz_id           varchar(255)                           not null comment '业务编号',
    title            varchar(255)                           null comment '标题',
    description      varchar(512)                           null comment '描述',
    experience       int                                    not null comment '变动的经验值',
    total_experience int                                    not null comment '变更后的总经验值',
    create_time      datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time      datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator          varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater          varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted          tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '会员经验记录表' charset = utf8mb4;

create table member_group
(
    id          bigint auto_increment comment '用户分组主键'
        primary key,
    name        varchar(255)                           not null comment '分组名称',
    remark      varchar(512)                           null comment '备注信息',
    status      tinyint                                not null comment '状态，枚举 CommonStatusEnum',
    create_time datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator     varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater     varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted     tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '用户分组表' charset = utf8mb4;

create table member_level
(
    id               bigint auto_increment comment '会员等级主键'
        primary key,
    name             varchar(255)                           not null comment '等级名称',
    level            int                                    not null comment '等级',
    experience       int                                    not null comment '升级经验',
    discount_percent int                                    not null comment '享受折扣，百分比值',
    icon             varchar(255)                           null comment '等级图标',
    background_url   varchar(255)                           null comment '等级背景图',
    status           tinyint                                not null comment '状态，枚举 CommonStatusEnum',
    create_time      datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time      datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator          varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater          varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted          tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '会员等级表' charset = utf8mb4;

create table member_level_record
(
    id               bigint auto_increment comment '会员等级记录主键'
        primary key,
    user_id          bigint                                 not null comment '用户编号，关联 MemberUserDO 的 id 字段',
    level_id         bigint                                 not null comment '等级编号，关联 MemberLevelDO 的 id 字段',
    level            int                                    not null comment '会员等级，冗余 MemberLevelDO 的 level 字段',
    discount_percent int                                    not null comment '享受折扣，百分比值',
    experience       int                                    not null comment '升级经验',
    user_experience  int                                    not null comment '会员此时的经验值',
    remark           varchar(255)                           null comment '备注信息',
    description      varchar(512)                           null comment '描述信息',
    create_time      datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time      datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator          varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater          varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted          tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '会员等级记录表' charset = utf8mb4;

create table member_point_record
(
    id          bigint auto_increment comment '积分记录主键'
        primary key,
    user_id     bigint                                 not null comment '用户编号，对应 MemberUserDO 的 id 属性',
    biz_id      varchar(255)                           not null comment '业务编码',
    biz_type    tinyint                                not null comment '业务类型，枚举 MemberPointBizTypeEnum',
    title       varchar(255)                           null comment '积分标题',
    description varchar(512)                           null comment '积分描述',
    point       int                                    not null comment '变动积分，正数表示获得，负数表示消耗',
    total_point int                                    not null comment '变动后的积分',
    create_time datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator     varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater     varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted     tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '用户积分记录表' charset = utf8mb4;

create table member_sign_in_config
(
    id          bigint auto_increment comment '规则自增主键'
        primary key,
    day         int                                    not null comment '签到第 x 天',
    point       int                                    not null comment '奖励积分',
    experience  int                                    not null comment '奖励经验',
    status      tinyint                                not null comment '状态，枚举 CommonStatusEnum',
    create_time datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator     varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater     varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted     tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '签到规则表' charset = utf8mb4;

create table member_sign_in_record
(
    id          bigint auto_increment comment '签到记录主键'
        primary key,
    user_id     bigint                                 not null comment '签到用户的 ID',
    day         int                                    not null comment '第几天签到',
    point       int                                    not null comment '签到的积分',
    experience  int                                    not null comment '签到的经验',
    create_time datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator     varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater     varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted     tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '签到记录表' charset = utf8mb4;

create table member_tag
(
    id          bigint auto_increment comment '会员标签主键'
        primary key,
    name        varchar(255)                           not null comment '标签名称',
    create_time datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator     varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater     varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    deleted     tinyint(1) default 0                   null comment '逻辑删除标志'
)
    comment '会员标签表' charset = utf8mb4;

create table member_user
(
    id                bigint auto_increment comment '用户ID'
        primary key,
    mobile            varchar(20)                            null comment '手机号码',
    password          varchar(255)                           null comment '加密后的密码，使用 BCryptPasswordEncoder 加密',
    status            tinyint                                null comment '帐号状态，枚举 CommonStatusEnum',
    register_ip       varchar(45)                            null comment '注册 IP',
    register_terminal tinyint                                null comment '注册终端，枚举 TerminalEnum',
    login_ip          varchar(45)                            null comment '最后登录IP',
    login_date        datetime                               null comment '最后登录时间',
    nickname          varchar(100)                           null comment '用户昵称',
    avatar            varchar(255)                           null comment '用户头像',
    name              varchar(50)                            null comment '真实名字',
    sex               tinyint                                null comment '性别，枚举 SexEnum',
    birthday          date                                   null comment '出生日期',
    area_id           int                                    null comment '所在地，对应 Area 表的 ID',
    mark              varchar(255)                           null comment '用户备注',
    point             int        default 0                   null comment '积分',
    tag_ids           text                                   null comment '会员标签列表，以逗号分隔，存储为 JSON 格式',
    level_id          bigint                                 null comment '会员级别编号，关联 MemberLevel 表',
    experience        int        default 0                   null comment '会员经验值',
    group_id          bigint                                 null comment '用户分组编号，关联 MemberGroup 表',
    create_time       datetime   default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time       datetime   default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator           varchar(64) collate utf8mb4_unicode_ci null comment '创建者',
    updater           varchar(64) collate utf8mb4_unicode_ci null comment '更新者',
    tenant_id         bigint     default 0                   not null comment '租户编号',
    deleted           tinyint(1) default 0                   null comment '逻辑删除标志',
    constraint uk_mobile
        unique (mobile)
)
    comment '会员用户表' charset = utf8mb4;

create table mp_account
(
    id          bigint auto_increment comment '公众号账号主键'
        primary key,
    name        varchar(255)                                                      not null comment '公众号名称',
    account     varchar(255)                                                      not null comment '公众号账号',
    app_id      varchar(255)                                                      not null comment '公众号 appid',
    app_secret  varchar(255)                                                      not null comment '公众号密钥',
    token       varchar(255)                                                      not null comment '公众号token',
    aes_key     varchar(255)                                                      not null comment '消息加解密密钥',
    qr_code_url varchar(1024)                                                     null comment '二维码图片 URL',
    remark      varchar(1024)                                                     null comment '备注',
    create_time datetime                                default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime                                default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator     varchar(255) collate utf8mb4_unicode_ci default ''                null comment '创建者，SysUser 的 id 编号',
    updater     varchar(64) collate utf8mb4_unicode_ci  default ''                null comment '更新者，SysUser 的 id 编号',
    deleted     tinyint(1)                              default 0                 null comment '是否删除，逻辑删除字段',
    tenant_id   mediumtext                                                        null comment '租户id'
)
    comment '公众号账号表' charset = utf8mb4;

create table mp_auto_reply
(
    id                       bigint auto_increment comment '主键，自增'
        primary key,
    account_id               bigint                                                            null comment '公众号账号编号',
    app_id                   varchar(255)                                                      null comment '公众号 appId，冗余 MpAccountDO 的 appId 字段',
    type                     int                                                               null comment '回复类型，枚举 MpAutoReplyTypeEnum',
    request_keyword          varchar(255)                                                      null comment '请求的关键字，当 type 为 KEYWORD 时',
    request_match            int                                                               null comment '请求的关键字的匹配方式，枚举 MpAutoReplyMatchEnum，当 type 为 KEYWORD 时',
    request_message_type     varchar(50)                                                       null comment '请求的消息类型，当 type 为 MESSAGE 时，枚举 WxConsts.XmlMsgType',
    response_message_type    varchar(50)                                                       null comment '回复的消息类型，枚举 WxConsts.XmlMsgType 中的 TEXT、IMAGE、VOICE、VIDEO、NEWS',
    response_content         text                                                              null comment '回复的消息内容，当消息类型为 TEXT 时',
    response_media_id        varchar(255)                                                      null comment '回复的媒体 ID，消息类型为 IMAGE、VOICE、VIDEO 时使用',
    response_media_url       varchar(255)                                                      null comment '回复的媒体 URL，消息类型为 IMAGE、VOICE、VIDEO 时使用',
    response_title           varchar(255)                                                      null comment '回复的标题，消息类型为 VIDEO 时使用',
    response_description     varchar(255)                                                      null comment '回复的描述，消息类型为 VIDEO 时使用',
    response_thumb_media_id  varchar(255)                                                      null comment '回复缩略图的媒体 ID，消息类型为 MUSIC、VIDEO 时使用',
    response_thumb_media_url varchar(255)                                                      null comment '回复缩略图的媒体 URL，消息类型为 MUSIC、VIDEO 时使用',
    response_articles        text                                                              null comment '回复的图文消息，消息类型为 NEWS 时使用',
    response_music_url       varchar(255)                                                      null comment '回复的音乐链接，消息类型为 MUSIC 时使用',
    response_hq_music_url    varchar(255)                                                      null comment '回复的高质量音乐链接，WIFI 环境优先使用该链接播放音乐，消息类型为 MUSIC 时使用',
    create_time              datetime                                default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time              datetime                                default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator                  varchar(255) collate utf8mb4_unicode_ci default ''                null comment '创建者，SysUser 的 id 编号',
    updater                  varchar(64) collate utf8mb4_unicode_ci  default ''                null comment '更新者，SysUser 的 id 编号',
    deleted                  tinyint(1)                              default 0                 null comment '是否删除，逻辑删除字段'
)
    comment '公众号消息自动回复表' charset = utf8mb4;

create table mp_material
(
    id           bigint auto_increment comment '公众号素材主键'
        primary key,
    account_id   bigint                                                            null comment '公众号账号的编号，冗余 MpAccountDO 的 id 字段',
    app_id       varchar(255)                                                      null comment '公众号 appId，冗余 MpAccountDO 的 appId 字段',
    media_id     varchar(255)                                                      null comment '公众号素材 id',
    type         varchar(50)                                                       null comment '文件类型，枚举 MediaFileType',
    permanent    tinyint(1)                                                        null comment '是否永久素材，true 为永久素材，false 为临时素材',
    url          varchar(1024)                                                     null comment '文件服务器的 URL',
    name         varchar(255)                                                      null comment '素材名称，永久素材非空，临时素材可能为空',
    mp_url       varchar(1024)                                                     null comment '公众号文件 URL，仅永久素材使用',
    title        varchar(255)                                                      null comment '视频素材标题，仅永久素材使用',
    introduction varchar(1024)                                                     null comment '视频素材描述，仅永久素材使用',
    create_time  datetime                                default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime                                default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator      varchar(255) collate utf8mb4_unicode_ci default ''                null comment '创建者，SysUser 的 id 编号',
    updater      varchar(64) collate utf8mb4_unicode_ci  default ''                null comment '更新者，SysUser 的 id 编号',
    deleted      tinyint(1)                              default 0                 null comment '是否删除，逻辑删除字段'
)
    comment '公众号素材表' charset = utf8mb4;

create table mp_menu
(
    id                     bigint auto_increment comment '菜单编号，顶级菜单为 0'
        primary key,
    account_id             bigint                                                            null comment '公众号账号的编号，冗余 MpAccountDO 的 id 字段',
    app_id                 varchar(255)                                                      null comment '公众号 appId，冗余 MpAccountDO 的 appId 字段',
    name                   varchar(255)                                                      null comment '菜单名称',
    menu_key               varchar(255)                                                      null comment '菜单标识',
    parent_id              bigint                                  default 0                 null comment '父菜单编号，顶级菜单为 0',
    type                   varchar(50)                                                       null comment '按钮类型，枚举 MenuButtonType',
    url                    varchar(1024)                                                     null comment '网页链接，粉丝点击菜单可打开链接',
    mini_program_app_id    varchar(255)                                                      null comment '小程序的 appId，当按钮类型为 MINIPROGRAM 时使用',
    mini_program_page_path varchar(255)                                                      null comment '小程序的页面路径，当按钮类型为 MINIPROGRAM 时使用',
    article_id             varchar(255)                                                      null comment '跳转图文的媒体编号',
    reply_message_type     varchar(50)                                                       null comment '消息类型，当按钮类型为 CLICK 或 SCANCODE_WAITMSG 时使用',
    reply_content          text                                                              null comment '回复的消息内容，当消息类型为 TEXT 时使用',
    reply_media_id         varchar(255)                                                      null comment '回复的媒体 id，当消息类型为 IMAGE、VOICE、VIDEO 时使用',
    reply_media_url        varchar(255)                                                      null comment '回复的媒体 URL，当消息类型为 IMAGE、VOICE、VIDEO 时使用',
    reply_title            varchar(255)                                                      null comment '回复的标题，当消息类型为 VIDEO 时使用',
    reply_description      varchar(255)                                                      null comment '回复的描述，当消息类型为 VIDEO 时使用',
    reply_thumb_media_id   varchar(255)                                                      null comment '回复的缩略图的媒体 id，当消息类型为 MUSIC、VIDEO 时使用',
    reply_thumb_media_url  varchar(255)                                                      null comment '回复的缩略图的媒体 URL，当消息类型为 MUSIC、VIDEO 时使用',
    reply_articles         text                                                              null comment '回复的图文消息数组，当消息类型为 NEWS 时使用',
    reply_music_url        varchar(255)                                                      null comment '回复的音乐链接，当消息类型为 MUSIC 时使用',
    reply_hq_music_url     varchar(255)                                                      null comment '回复的高质量音乐链接，WIFI 环境优先使用该链接播放音乐，当消息类型为 MUSIC 时使用',
    create_time            datetime                                default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time            datetime                                default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator                varchar(255) collate utf8mb4_unicode_ci default ''                null comment '创建者，SysUser 的 id 编号',
    updater                varchar(64) collate utf8mb4_unicode_ci  default ''                null comment '更新者，SysUser 的 id 编号',
    deleted                tinyint(1)                              default 0                 null comment '是否删除，逻辑删除字段'
)
    comment '公众号菜单表' charset = utf8mb4;

create table mp_message
(
    id              bigint auto_increment comment '主键，自增'
        primary key,
    msg_id          bigint                                                            null comment '微信公众号消息 id',
    account_id      bigint                                                            null comment '公众号账号的 ID，冗余 MpAccountDO 的 id 字段',
    app_id          varchar(255)                                                      null comment '公众号 appid，冗余 MpAccountDO 的 appId 字段',
    user_id         bigint                                                            null comment '公众号粉丝的编号，冗余 MpUserDO 的 id 字段',
    openid          varchar(255)                                                      null comment '公众号粉丝标志，冗余 MpUserDO 的 openid 字段',
    type            varchar(50)                                                       null comment '消息类型，枚举 WxConsts.XmlMsgType',
    send_from       int                                                               null comment '消息来源，枚举 MpMessageSendFromEnum',
    content         text                                                              null comment '消息内容，当消息类型为 TEXT 时使用',
    media_id        varchar(255)                                                      null comment '媒体文件的编号，当消息类型为 IMAGE、VOICE、VIDEO 时使用',
    media_url       varchar(255)                                                      null comment '媒体文件的 URL',
    recognition     varchar(255)                                                      null comment '语音识别后文本，当消息类型为 VOICE 时使用',
    format          varchar(50)                                                       null comment '语音格式，如 amr，speex 等，当消息类型为 VOICE 时使用',
    title           varchar(255)                                                      null comment '标题，当消息类型为 VIDEO、MUSIC、LINK 时使用',
    description     varchar(255)                                                      null comment '描述，当消息类型为 VIDEO、MUSIC 时使用',
    thumb_media_id  varchar(255)                                                      null comment '缩略图的媒体 id，当消息类型为 MUSIC、VIDEO 时使用',
    thumb_media_url varchar(255)                                                      null comment '缩略图的媒体 URL，当消息类型为 MUSIC、VIDEO 时使用',
    url             varchar(255)                                                      null comment '点击图文消息跳转链接，当消息类型为 LINK 时使用',
    location_x      double                                                            null comment '地理位置维度，当消息类型为 LOCATION 时使用',
    location_y      double                                                            null comment '地理位置经度，当消息类型为 LOCATION 时使用',
    scale           double                                                            null comment '地图缩放大小，当消息类型为 LOCATION 时使用',
    label           varchar(255)                                                      null comment '详细地址，当消息类型为 LOCATION 时使用',
    articles        text                                                              null comment '图文消息数组，当消息类型为 NEWS 时使用',
    music_url       varchar(255)                                                      null comment '音乐链接，当消息类型为 MUSIC 时使用',
    hq_music_url    varchar(255)                                                      null comment '高质量音乐链接，WIFI 环境优先使用该链接播放音乐，当消息类型为 MUSIC 时使用',
    event           varchar(50)                                                       null comment '事件类型，枚举 WxConsts.EventType',
    event_key       varchar(255)                                                      null comment '事件 Key，二维码参数值或自定义菜单 KEY 值',
    create_time     datetime                                default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime                                default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator         varchar(255) collate utf8mb4_unicode_ci default ''                null comment '创建者，SysUser 的 id 编号',
    updater         varchar(64) collate utf8mb4_unicode_ci  default ''                null comment '更新者，SysUser 的 id 编号',
    deleted         tinyint(1)                              default 0                 null comment '是否删除，逻辑删除字段'
)
    comment '公众号消息表' charset = utf8mb4;

create table mp_tag
(
    id          bigint auto_increment comment '主键，自增'
        primary key,
    tag_id      bigint                                                            null comment '公众号标签 ID',
    name        varchar(255)                                                      null comment '标签名',
    count       int                                                               null comment '此标签下粉丝数，冗余字段，需要管理员点击同步更新',
    account_id  bigint                                                            null comment '公众号账号编号',
    app_id      varchar(255)                                                      null comment '公众号 appId，冗余 MpAccountDO 的 appId 字段',
    create_time datetime                                default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime                                default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator     varchar(255) collate utf8mb4_unicode_ci default ''                null comment '创建者，SysUser 的 id 编号',
    updater     varchar(64) collate utf8mb4_unicode_ci  default ''                null comment '更新者，SysUser 的 id 编号',
    deleted     tinyint(1)                              default 0                 null comment '是否删除，逻辑删除字段'
)
    comment '公众号标签表' charset = utf8mb4;

create table mp_user
(
    id               bigint auto_increment comment '主键，自增'
        primary key,
    openid           varchar(255)                                                      not null comment '粉丝标识',
    union_id         varchar(255)                                                      null comment '微信生态唯一标识',
    subscribe_status int                                                               null comment '关注状态: 1 - 已关注, 2 - 取消关注',
    subscribe_time   datetime                                                          null comment '关注时间',
    unsubscribe_time datetime                                                          null comment '取消关注时间',
    nickname         varchar(255)                                                      null comment '昵称',
    head_image_url   varchar(255)                                                      null comment '头像地址',
    language         varchar(50)                                                       null comment '语言',
    country          varchar(50)                                                       null comment '国家',
    province         varchar(50)                                                       null comment '省份',
    city             varchar(50)                                                       null comment '城市',
    remark           varchar(255)                                                      null comment '备注',
    tag_ids          text                                                              null comment '标签编号数组，关联 MpTagDO 的 tag_id 字段',
    account_id       bigint                                                            null comment '公众号账号编号',
    app_id           varchar(255)                                                      null comment '公众号 appId，冗余 MpAccountDO 的 appId 字段',
    create_time      datetime                                default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime                                default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后更新时间',
    creator          varchar(255) collate utf8mb4_unicode_ci default ''                null comment '创建者，SysUser 的 id 编号',
    updater          varchar(64) collate utf8mb4_unicode_ci  default ''                null comment '更新者，SysUser 的 id 编号',
    deleted          tinyint(1)                              default 0                 null comment '是否删除，逻辑删除字段'
)
    comment '微信公众号粉丝表' charset = utf8mb4;

create table sp_datasource
(
    id                varchar(32)                        not null
        primary key,
    name              varchar(255)                       null,
    driver_class_name varchar(255)                       null,
    jdbc_url          varchar(255)                       null,
    username          varchar(64)                        null,
    password          varchar(32)                        null,
    create_date       datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create table sp_flow
(
    id                varchar(32)                        not null
        primary key,
    name              varchar(64)                        null comment '任务名字',
    xml               longtext                           null comment 'xml表达式',
    cron              varchar(255)                       null comment 'corn表达式',
    enabled           char     default '0'               null comment '任务是否启动,默认未启动',
    create_date       datetime default CURRENT_TIMESTAMP null comment '创建时间',
    last_execute_time datetime                           null comment '上一次执行时间',
    next_execute_time datetime                           null comment '下一次执行时间',
    execute_count     int                                null comment '定时执行的已执行次数'
)
    comment '爬虫任务表' charset = utf8mb4;

create table sp_flow_notice
(
    id               varchar(32)      not null
        primary key,
    recipients       varchar(200)     null comment '收件人',
    notice_way       char(10)         null comment '通知方式',
    start_notice     char default '0' null comment '流程开始通知:1:开启通知,0:关闭通知',
    exception_notice char default '0' null comment '流程异常通知:1:开启通知,0:关闭通知',
    end_notice       char default '0' null comment '流程结束通知:1:开启通知,0:关闭通知'
)
    comment '爬虫任务通知表' charset = utf8mb4;

create table sp_function
(
    id          varchar(32)                        not null
        primary key,
    name        varchar(255)                       null comment '函数名',
    parameter   varchar(512)                       null comment '参数',
    script      text                               null comment 'js脚本',
    create_date datetime default CURRENT_TIMESTAMP null
)
    row_format = DYNAMIC;

create table sp_task
(
    id         int auto_increment
        primary key,
    flow_id    varchar(32) not null,
    begin_time datetime    null,
    end_time   datetime    null
)
    charset = utf8mb4;

create table sp_variable
(
    id          int auto_increment
        primary key,
    name        varchar(32)                        null comment '变量名',
    value       varchar(512)                       null comment '变量值',
    description varchar(255)                       null comment '变量描述',
    create_date datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    charset = utf8mb4;

create table system_dept
(
    id             bigint auto_increment comment '部门id'
        primary key,
    name           varchar(30) default ''                not null comment '部门名称',
    parent_id      bigint      default 0                 not null comment '父部门id',
    sort           int         default 0                 not null comment '显示顺序',
    leader_user_id bigint                                null comment '负责人',
    phone          varchar(11)                           null comment '联系电话',
    email          varchar(50)                           null comment '邮箱',
    status         tinyint                               not null comment '部门状态（0正常 1停用）',
    creator        varchar(64) default ''                null comment '创建者',
    create_time    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater        varchar(64) default ''                null comment '更新者',
    update_time    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted        bit         default b'0'              not null comment '是否删除',
    tenant_id      bigint      default 0                 not null comment '租户编号'
)
    comment '部门表' collate = utf8mb4_unicode_ci;

create table system_dict_data
(
    id          bigint auto_increment comment '字典编码'
        primary key,
    sort        int          default 0                 not null comment '字典排序',
    label       varchar(100) default ''                not null comment '字典标签',
    value       varchar(100) default ''                not null comment '字典键值',
    dict_type   varchar(100) default ''                not null comment '字典类型',
    status      tinyint      default 0                 not null comment '状态（0正常 1停用）',
    color_type  varchar(100) default ''                null comment '颜色类型',
    css_class   varchar(100) default ''                null comment 'css 样式',
    remark      varchar(500)                           null comment '备注',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除'
)
    comment '字典数据表' collate = utf8mb4_unicode_ci;

create table system_dict_type
(
    id           bigint auto_increment comment '字典主键'
        primary key,
    name         varchar(100) default ''                not null comment '字典名称',
    type         varchar(100) default ''                not null comment '字典类型',
    status       tinyint      default 0                 not null comment '状态（0正常 1停用）',
    remark       varchar(500)                           null comment '备注',
    creator      varchar(64)  default ''                null comment '创建者',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater      varchar(64)  default ''                null comment '更新者',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted      bit          default b'0'              not null comment '是否删除',
    deleted_time datetime                               null comment '删除时间'
)
    comment '字典类型表' collate = utf8mb4_unicode_ci;

create table system_login_log
(
    id          bigint auto_increment comment '访问ID'
        primary key,
    log_type    bigint                                not null comment '日志类型',
    trace_id    varchar(64) default ''                not null comment '链路追踪编号',
    user_id     bigint      default 0                 not null comment '用户编号',
    user_type   tinyint     default 0                 not null comment '用户类型',
    username    varchar(50) default ''                not null comment '用户账号',
    result      tinyint                               not null comment '登陆结果',
    user_ip     varchar(50)                           not null comment '用户 IP',
    user_agent  varchar(512)                          not null comment '浏览器 UA',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除',
    tenant_id   bigint      default 0                 not null comment '租户编号'
)
    comment '系统访问记录' collate = utf8mb4_unicode_ci;

create table system_mail_account
(
    id              bigint                                not null comment '主键'
        primary key,
    mail            varchar(255)                          not null comment '邮箱',
    username        varchar(255)                          not null comment '用户名',
    password        varchar(255)                          not null comment '密码',
    host            varchar(255)                          not null comment 'SMTP 服务器域名',
    port            int                                   not null comment 'SMTP 服务器端口',
    ssl_enable      bit         default b'0'              not null comment '是否开启 SSL',
    starttls_enable bit         default b'0'              not null comment '是否开启 STARTTLS',
    creator         varchar(64) default ''                null comment '创建者',
    create_time     datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater         varchar(64) default ''                null comment '更新者',
    update_time     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted         bit         default b'0'              not null comment '是否删除'
)
    comment '邮箱账号表' collate = utf8mb4_unicode_ci;

create table system_mail_log
(
    id                bigint                                not null comment '编号'
        primary key,
    user_id           bigint                                null comment '用户编号',
    user_type         tinyint                               null comment '用户类型',
    to_mail           varchar(255)                          not null comment '接收邮箱地址',
    account_id        bigint                                not null comment '邮箱账号编号',
    from_mail         varchar(255)                          not null comment '发送邮箱地址',
    template_id       bigint                                not null comment '模板编号',
    template_code     varchar(63)                           not null comment '模板编码',
    template_nickname varchar(255)                          null comment '模版发送人名称',
    template_title    varchar(255)                          not null comment '邮件标题',
    template_content  varchar(10240)                        not null comment '邮件内容',
    template_params   varchar(255)                          not null comment '邮件参数',
    send_status       tinyint     default 0                 not null comment '发送状态',
    send_time         datetime                              null comment '发送时间',
    send_message_id   varchar(255)                          null comment '发送返回的消息 ID',
    send_exception    varchar(4096)                         null comment '发送异常',
    creator           varchar(64) default ''                null comment '创建者',
    create_time       datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater           varchar(64) default ''                null comment '更新者',
    update_time       datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted           bit         default b'0'              not null comment '是否删除'
)
    comment '邮件日志表' collate = utf8mb4_unicode_ci;

create table system_mail_template
(
    id          bigint                                not null comment '编号'
        primary key,
    name        varchar(63)                           not null comment '模板名称',
    code        varchar(63)                           not null comment '模板编码',
    account_id  bigint                                not null comment '发送的邮箱账号编号',
    nickname    varchar(255)                          null comment '发送人名称',
    title       varchar(255)                          not null comment '模板标题',
    content     varchar(10240)                        not null comment '模板内容',
    params      varchar(255)                          not null comment '参数数组',
    status      tinyint                               not null comment '开启状态',
    remark      varchar(255)                          null comment '备注',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除'
)
    comment '邮件模版表' collate = utf8mb4_unicode_ci;

create table system_menu
(
    id             bigint auto_increment comment '菜单ID'
        primary key,
    name           varchar(50)                            not null comment '菜单名称',
    permission     varchar(100) default ''                not null comment '权限标识',
    type           tinyint                                not null comment '菜单类型',
    sort           int          default 0                 not null comment '显示顺序',
    parent_id      bigint       default 0                 not null comment '父菜单ID',
    path           varchar(200) default ''                null comment '路由地址',
    icon           varchar(100) default '#'               null comment '菜单图标',
    component      varchar(255)                           null comment '组件路径',
    component_name varchar(255)                           null comment '组件名',
    status         tinyint      default 0                 not null comment '菜单状态',
    visible        bit          default b'1'              not null comment '是否可见',
    keep_alive     bit          default b'1'              not null comment '是否缓存',
    always_show    bit          default b'1'              not null comment '是否总是显示',
    creator        varchar(64)  default ''                null comment '创建者',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater        varchar(64)  default ''                null comment '更新者',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted        bit          default b'0'              not null comment '是否删除'
)
    comment '菜单权限表' collate = utf8mb4_unicode_ci;

create index system_menu_deleted_index
    on system_menu (deleted);

create table system_notice
(
    id          bigint                                not null comment '公告ID'
        primary key,
    title       varchar(50)                           not null comment '公告标题',
    content     text                                  not null comment '公告内容',
    type        tinyint                               not null comment '公告类型（1通知 2公告）',
    status      tinyint     default 0                 not null comment '公告状态（0正常 1关闭）',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除',
    tenant_id   bigint      default 0                 not null comment '租户编号'
)
    comment '通知公告表' collate = utf8mb4_unicode_ci;

create table system_notify_message
(
    id                bigint                                not null comment '用户ID'
        primary key,
    user_id           bigint                                not null comment '用户id',
    user_type         tinyint                               not null comment '用户类型',
    template_id       bigint                                not null comment '模版编号',
    template_code     varchar(64)                           not null comment '模板编码',
    template_nickname varchar(63)                           not null comment '模版发送人名称',
    template_content  varchar(1024)                         not null comment '模版内容',
    template_type     int                                   not null comment '模版类型',
    template_params   varchar(255)                          not null comment '模版参数',
    read_status       bit                                   not null comment '是否已读',
    read_time         datetime                              null comment '阅读时间',
    creator           varchar(64) default ''                null comment '创建者',
    create_time       datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater           varchar(64) default ''                null comment '更新者',
    update_time       datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted           bit         default b'0'              not null comment '是否删除',
    tenant_id         bigint      default 0                 not null comment '租户编号'
)
    comment '站内信消息表' collate = utf8mb4_unicode_ci;

create table system_notify_template
(
    id          bigint auto_increment comment '主键'
        primary key,
    name        varchar(63)                           not null comment '模板名称',
    code        varchar(64)                           not null comment '模版编码',
    nickname    varchar(255)                          not null comment '发送人名称',
    content     varchar(1024)                         not null comment '模版内容',
    type        tinyint                               not null comment '类型',
    params      varchar(255)                          null comment '参数数组',
    status      tinyint                               not null comment '状态',
    remark      varchar(255)                          null comment '备注',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除'
)
    comment '站内信模板表' collate = utf8mb4_unicode_ci;

create table system_oauth2_access_token
(
    id            bigint auto_increment comment '编号'
        primary key,
    user_id       bigint                                not null comment '用户编号',
    user_type     tinyint                               not null comment '用户类型',
    user_info     varchar(512)                          not null comment '用户信息',
    access_token  varchar(255)                          not null comment '访问令牌',
    refresh_token varchar(32)                           not null comment '刷新令牌',
    client_id     varchar(255)                          not null comment '客户端编号',
    scopes        varchar(255)                          null comment '授权范围',
    expires_time  datetime                              not null comment '过期时间',
    creator       varchar(64) default ''                null comment '创建者',
    create_time   datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater       varchar(64) default ''                null comment '更新者',
    update_time   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted       bit         default b'0'              not null comment '是否删除',
    tenant_id     bigint      default 0                 not null comment '租户编号'
)
    comment 'OAuth2 访问令牌' collate = utf8mb4_unicode_ci;

create index idx_access_token
    on system_oauth2_access_token (access_token);

create index idx_refresh_token
    on system_oauth2_access_token (refresh_token);

create table system_oauth2_approve
(
    id           bigint auto_increment comment '编号'
        primary key,
    user_id      bigint                                 not null comment '用户编号',
    user_type    tinyint                                not null comment '用户类型',
    client_id    varchar(255)                           not null comment '客户端编号',
    scope        varchar(255) default ''                not null comment '授权范围',
    approved     bit          default b'0'              not null comment '是否接受',
    expires_time datetime                               not null comment '过期时间',
    creator      varchar(64)  default ''                null comment '创建者',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater      varchar(64)  default ''                null comment '更新者',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted      bit          default b'0'              not null comment '是否删除',
    tenant_id    bigint       default 0                 not null comment '租户编号'
)
    comment 'OAuth2 批准表' collate = utf8mb4_unicode_ci;

create table system_oauth2_client
(
    id                             bigint auto_increment comment '编号'
        primary key,
    client_id                      varchar(255)                          not null comment '客户端编号',
    secret                         varchar(255)                          not null comment '客户端密钥',
    name                           varchar(255)                          not null comment '应用名',
    logo                           varchar(255)                          not null comment '应用图标',
    description                    varchar(255)                          null comment '应用描述',
    status                         tinyint                               not null comment '状态',
    access_token_validity_seconds  int                                   not null comment '访问令牌的有效期',
    refresh_token_validity_seconds int                                   not null comment '刷新令牌的有效期',
    redirect_uris                  varchar(255)                          not null comment '可重定向的 URI 地址',
    authorized_grant_types         varchar(255)                          not null comment '授权类型',
    scopes                         varchar(255)                          null comment '授权范围',
    auto_approve_scopes            varchar(255)                          null comment '自动通过的授权范围',
    authorities                    varchar(255)                          null comment '权限',
    resource_ids                   varchar(255)                          null comment '资源',
    additional_information         varchar(4096)                         null comment '附加信息',
    creator                        varchar(64) default ''                null comment '创建者',
    create_time                    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater                        varchar(64) default ''                null comment '更新者',
    update_time                    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted                        bit         default b'0'              not null comment '是否删除'
)
    comment 'OAuth2 客户端表' collate = utf8mb4_unicode_ci;

create table system_oauth2_code
(
    id           bigint auto_increment comment '编号'
        primary key,
    user_id      bigint                                 not null comment '用户编号',
    user_type    tinyint                                not null comment '用户类型',
    code         varchar(32)                            not null comment '授权码',
    client_id    varchar(255)                           not null comment '客户端编号',
    scopes       varchar(255) default ''                null comment '授权范围',
    expires_time datetime                               not null comment '过期时间',
    redirect_uri varchar(255)                           null comment '可重定向的 URI 地址',
    state        varchar(255) default ''                not null comment '状态',
    creator      varchar(64)  default ''                null comment '创建者',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater      varchar(64)  default ''                null comment '更新者',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted      bit          default b'0'              not null comment '是否删除',
    tenant_id    bigint       default 0                 not null comment '租户编号'
)
    comment 'OAuth2 授权码表' collate = utf8mb4_unicode_ci;

create table system_oauth2_refresh_token
(
    id            bigint auto_increment comment '编号'
        primary key,
    user_id       bigint                                not null comment '用户编号',
    refresh_token varchar(32)                           not null comment '刷新令牌',
    user_type     tinyint                               not null comment '用户类型',
    client_id     varchar(255)                          not null comment '客户端编号',
    scopes        varchar(255)                          null comment '授权范围',
    expires_time  datetime                              not null comment '过期时间',
    creator       varchar(64) default ''                null comment '创建者',
    create_time   datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater       varchar(64) default ''                null comment '更新者',
    update_time   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted       bit         default b'0'              not null comment '是否删除',
    tenant_id     bigint      default 0                 not null comment '租户编号'
)
    comment 'OAuth2 刷新令牌' collate = utf8mb4_unicode_ci;

create table system_operate_log
(
    id             bigint auto_increment comment '日志主键'
        primary key,
    trace_id       varchar(64)   default ''                not null comment '链路追踪编号',
    user_id        bigint                                  not null comment '用户编号',
    user_type      tinyint       default 0                 not null comment '用户类型',
    type           varchar(50)                             not null comment '操作模块类型',
    sub_type       varchar(50)                             not null comment '操作名',
    biz_id         bigint                                  not null comment '操作数据模块编号',
    action         varchar(2000) default ''                not null comment '操作内容',
    extra          varchar(2000) default ''                not null comment '拓展字段',
    request_method varchar(16)   default ''                null comment '请求方法名',
    request_url    varchar(255)  default ''                null comment '请求地址',
    user_ip        varchar(50)                             null comment '用户 IP',
    user_agent     varchar(200)                            null comment '浏览器 UA',
    creator        varchar(64)   default ''                null comment '创建者',
    create_time    datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    updater        varchar(64)   default ''                null comment '更新者',
    update_time    datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted        bit           default b'0'              not null comment '是否删除',
    tenant_id      bigint        default 0                 not null comment '租户编号'
)
    comment '操作日志记录 V2 版本' collate = utf8mb4_unicode_ci;

create table system_post
(
    id          bigint                                not null comment '岗位ID'
        primary key,
    code        varchar(64)                           not null comment '岗位编码',
    name        varchar(50)                           not null comment '岗位名称',
    sort        int                                   not null comment '显示顺序',
    status      tinyint                               not null comment '状态（0正常 1停用）',
    remark      varchar(500)                          null comment '备注',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除',
    tenant_id   bigint      default 0                 not null comment '租户编号'
)
    comment '岗位信息表' collate = utf8mb4_unicode_ci;

create table system_role
(
    id                  bigint auto_increment comment '角色ID'
        primary key,
    name                varchar(30)                            not null comment '角色名称',
    code                varchar(100)                           not null comment '角色权限字符串',
    sort                int                                    not null comment '显示顺序',
    data_scope          tinyint      default 1                 not null comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    data_scope_dept_ids varchar(500) default ''                not null comment '数据范围(指定部门数组)',
    status              tinyint                                not null comment '角色状态（0正常 1停用）',
    type                tinyint                                not null comment '角色类型',
    remark              varchar(500)                           null comment '备注',
    creator             varchar(64)  default ''                null comment '创建者',
    create_time         datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater             varchar(64)  default ''                null comment '更新者',
    update_time         datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted             bit          default b'0'              not null comment '是否删除',
    tenant_id           bigint       default 0                 not null comment '租户编号'
)
    comment '角色信息表' collate = utf8mb4_unicode_ci;

create table system_role_menu
(
    id          bigint auto_increment comment '自增编号'
        primary key,
    role_id     bigint                                not null comment '角色ID',
    menu_id     bigint                                not null comment '菜单ID',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除',
    tenant_id   bigint      default 0                 not null comment '租户编号'
)
    comment '角色和菜单关联表' collate = utf8mb4_unicode_ci;

create table system_sms_channel
(
    id           bigint auto_increment comment '编号'
        primary key,
    signature    varchar(12)                           not null comment '短信签名',
    code         varchar(63)                           not null comment '渠道编码',
    status       tinyint                               not null comment '开启状态',
    remark       varchar(255)                          null comment '备注',
    api_key      varchar(128)                          not null comment '短信 API 的账号',
    api_secret   varchar(128)                          null comment '短信 API 的秘钥',
    callback_url varchar(255)                          null comment '短信发送回调 URL',
    creator      varchar(64) default ''                null comment '创建者',
    create_time  datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater      varchar(64) default ''                null comment '更新者',
    update_time  datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted      bit         default b'0'              not null comment '是否删除'
)
    comment '短信渠道' collate = utf8mb4_unicode_ci;

create table system_sms_code
(
    id          bigint auto_increment comment '编号'
        primary key,
    mobile      varchar(11)                           not null comment '手机号',
    code        varchar(6)                            not null comment '验证码',
    create_ip   varchar(15)                           not null comment '创建 IP',
    scene       tinyint                               not null comment '发送场景',
    today_index tinyint                               not null comment '今日发送的第几条',
    used        tinyint                               not null comment '是否使用',
    used_time   datetime                              null comment '使用时间',
    used_ip     varchar(255)                          null comment '使用 IP',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除',
    tenant_id   bigint      default 0                 not null comment '租户编号'
)
    comment '手机验证码' collate = utf8mb4_unicode_ci;

create index idx_mobile
    on system_sms_code (mobile)
    comment '手机号';

create table system_sms_log
(
    id               bigint auto_increment comment '编号'
        primary key,
    channel_id       bigint                                not null comment '短信渠道编号',
    channel_code     varchar(63)                           not null comment '短信渠道编码',
    template_id      bigint                                not null comment '模板编号',
    template_code    varchar(63)                           not null comment '模板编码',
    template_type    tinyint                               not null comment '短信类型',
    template_content varchar(255)                          not null comment '短信内容',
    template_params  varchar(255)                          not null comment '短信参数',
    api_template_id  varchar(63)                           not null comment '短信 API 的模板编号',
    mobile           varchar(11)                           not null comment '手机号',
    user_id          bigint                                null comment '用户编号',
    user_type        tinyint                               null comment '用户类型',
    send_status      tinyint     default 0                 not null comment '发送状态',
    send_time        datetime                              null comment '发送时间',
    api_send_code    varchar(63)                           null comment '短信 API 发送结果的编码',
    api_send_msg     varchar(255)                          null comment '短信 API 发送失败的提示',
    api_request_id   varchar(255)                          null comment '短信 API 发送返回的唯一请求 ID',
    api_serial_no    varchar(255)                          null comment '短信 API 发送返回的序号',
    receive_status   tinyint     default 0                 not null comment '接收状态',
    receive_time     datetime                              null comment '接收时间',
    api_receive_code varchar(63)                           null comment 'API 接收结果的编码',
    api_receive_msg  varchar(255)                          null comment 'API 接收结果的说明',
    creator          varchar(64) default ''                null comment '创建者',
    create_time      datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater          varchar(64) default ''                null comment '更新者',
    update_time      datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted          bit         default b'0'              not null comment '是否删除'
)
    comment '短信日志' collate = utf8mb4_unicode_ci;

create table system_sms_template
(
    id              bigint auto_increment comment '编号'
        primary key,
    type            tinyint                               not null comment '模板类型',
    status          tinyint                               not null comment '开启状态',
    code            varchar(63)                           not null comment '模板编码',
    name            varchar(63)                           not null comment '模板名称',
    content         varchar(255)                          not null comment '模板内容',
    params          varchar(255)                          not null comment '参数数组',
    remark          varchar(255)                          null comment '备注',
    api_template_id varchar(63)                           not null comment '短信 API 的模板编号',
    channel_id      bigint                                not null comment '短信渠道编号',
    channel_code    varchar(63)                           not null comment '短信渠道编码',
    creator         varchar(64) default ''                null comment '创建者',
    create_time     datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater         varchar(64) default ''                null comment '更新者',
    update_time     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted         bit         default b'0'              not null comment '是否删除'
)
    comment '短信模板' collate = utf8mb4_unicode_ci;

create table system_social_client
(
    id            bigint auto_increment comment '编号'
        primary key,
    name          varchar(255)                          not null comment '应用名',
    social_type   tinyint                               not null comment '社交平台的类型',
    user_type     tinyint                               not null comment '用户类型',
    client_id     varchar(255)                          not null comment '客户端编号',
    client_secret varchar(255)                          not null comment '客户端密钥',
    agent_id      varchar(255)                          null comment '代理编号',
    status        tinyint                               not null comment '状态',
    creator       varchar(64) default ''                null comment '创建者',
    create_time   datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater       varchar(64) default ''                null comment '更新者',
    update_time   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted       bit         default b'0'              not null comment '是否删除',
    tenant_id     bigint      default 0                 not null comment '租户编号'
)
    comment '社交客户端表' collate = utf8mb4_unicode_ci;

create table system_social_user
(
    id             bigint unsigned auto_increment comment '主键(自增策略)'
        primary key,
    type           tinyint                               not null comment '社交平台的类型',
    openid         varchar(32)                           not null comment '社交 openid',
    token          varchar(256)                          null comment '社交 token',
    raw_token_info varchar(1024)                         not null comment '原始 Token 数据，一般是 JSON 格式',
    nickname       varchar(32)                           not null comment '用户昵称',
    avatar         varchar(255)                          null comment '用户头像',
    raw_user_info  varchar(1024)                         not null comment '原始用户数据，一般是 JSON 格式',
    code           varchar(256)                          not null comment '最后一次的认证 code',
    state          varchar(256)                          null comment '最后一次的认证 state',
    creator        varchar(64) default ''                null comment '创建者',
    create_time    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater        varchar(64) default ''                null comment '更新者',
    update_time    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted        bit         default b'0'              not null comment '是否删除',
    tenant_id      bigint      default 0                 not null comment '租户编号'
)
    comment '社交用户表' collate = utf8mb4_unicode_ci;

create table system_social_user_bind
(
    id             bigint unsigned auto_increment comment '主键(自增策略)'
        primary key,
    user_id        bigint                                not null comment '用户编号',
    user_type      tinyint                               not null comment '用户类型',
    social_type    tinyint                               not null comment '社交平台的类型',
    social_user_id bigint                                not null comment '社交用户的编号',
    creator        varchar(64) default ''                null comment '创建者',
    create_time    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater        varchar(64) default ''                null comment '更新者',
    update_time    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted        bit         default b'0'              not null comment '是否删除',
    tenant_id      bigint      default 0                 not null comment '租户编号'
)
    comment '社交绑定表' collate = utf8mb4_unicode_ci;

create table system_tenant
(
    id              bigint auto_increment comment '租户编号'
        primary key,
    name            varchar(30)                            not null comment '租户名',
    contact_user_id bigint                                 null comment '联系人的用户编号',
    contact_name    varchar(30)                            not null comment '联系人',
    contact_mobile  varchar(500)                           null comment '联系手机',
    status          tinyint      default 0                 not null comment '租户状态（0正常 1停用）',
    website         varchar(256) default ''                null comment '绑定域名',
    package_id      bigint                                 not null comment '租户套餐编号',
    expire_time     datetime                               not null comment '过期时间',
    account_count   int                                    not null comment '账号数量',
    creator         varchar(64)  default ''                not null comment '创建者',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater         varchar(64)  default ''                null comment '更新者',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted         bit          default b'0'              not null comment '是否删除'
)
    comment '租户表' collate = utf8mb4_unicode_ci;

create table system_tenant_package
(
    id          bigint auto_increment comment '套餐编号'
        primary key,
    name        varchar(30)                            not null comment '套餐名',
    status      tinyint      default 0                 not null comment '租户状态（0正常 1停用）',
    remark      varchar(256) default ''                null comment '备注',
    menu_ids    varchar(4096)                          not null comment '关联的菜单编号',
    creator     varchar(64)  default ''                not null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除'
)
    comment '租户套餐表' collate = utf8mb4_unicode_ci;

create table system_user_post
(
    id          bigint auto_increment comment 'id'
        primary key,
    user_id     bigint      default 0                 not null comment '用户ID',
    post_id     bigint      default 0                 not null comment '岗位ID',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除',
    tenant_id   bigint      default 0                 not null comment '租户编号'
)
    comment '用户岗位表' collate = utf8mb4_unicode_ci;

create table system_user_role
(
    id          bigint auto_increment comment '自增编号'
        primary key,
    user_id     bigint                                not null comment '用户ID',
    role_id     bigint                                not null comment '角色ID',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              null comment '是否删除',
    tenant_id   bigint      default 0                 not null comment '租户编号'
)
    comment '用户和角色关联表' collate = utf8mb4_unicode_ci;

create table system_users
(
    id          bigint auto_increment comment '用户ID'
        primary key,
    username    varchar(30)                            not null comment '用户账号',
    password    varchar(100) default ''                not null comment '密码',
    nickname    varchar(30)                            not null comment '用户昵称',
    remark      varchar(500)                           null comment '备注',
    dept_id     bigint                                 null comment '部门ID',
    post_ids    varchar(255)                           null comment '岗位编号数组',
    email       varchar(50)  default ''                null comment '用户邮箱',
    mobile      varchar(11)  default ''                null comment '手机号码',
    sex         tinyint      default 0                 null comment '用户性别',
    avatar      varchar(512) default ''                null comment '头像地址',
    status      tinyint      default 0                 not null comment '帐号状态（0正常 1停用）',
    login_ip    varchar(50)  default ''                null comment '最后登录IP',
    login_date  datetime                               null comment '最后登录时间',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除',
    tenant_id   bigint       default 0                 not null comment '租户编号'
)
    comment '用户信息表' collate = utf8mb4_unicode_ci;

create table wenxun_audit_log
(
    id              int auto_increment comment '主键'
        primary key,
    spider_id       varchar(32)                        not null comment '文章校验表id',
    approved_record text                               null comment '审核通过信息',
    rejected_record text                               null comment '审核驳回信息',
    status          int                                not null comment '审核状态',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    creator         varchar(100)                       not null comment '操作员名称',
    updater         varchar(100)                       null comment '更新人',
    deleted         int      default 0                 null comment '删除标记（弃用）',
    dept_id         bigint                             null comment '部门id'
)
    comment '人工研判审核表';

create index wenxun_audit_log_spider_id_index
    on wenxun_audit_log (spider_id);

create index wenxun_audit_log_status_index
    on wenxun_audit_log (status);

create table wenxun_customer_audit_log
(
    id              int auto_increment comment '住建'
        primary key,
    spider_id       varchar(32)                        not null comment '文章校验表id',
    approved_record text                               null comment '审核通过信息',
    rejected_record text                               null comment '审核驳回信息',
    status          int                                not null comment '审核状态',
    update_time     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_time     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    auditor         varchar(100)                       not null comment '操作员名称',
    dept_id         bigint                             null comment '部门id',
    creator         varchar(100)                       null comment '创建人',
    updater         varchar(100)                       null comment '更新人',
    deleted         int                                null comment '删除标记'
)
    comment '客户审核表';

create index wenxun_audit_log_spider_id_index
    on wenxun_customer_audit_log (spider_id);

create index wenxun_audit_log_status_index
    on wenxun_customer_audit_log (status);

create table wenxun_detail_check_audit_info
(
    id               bigint                             not null comment '主键'
        primary key,
    check_source     int                                not null comment '检查源，1 敏感词，2，错词，3，接口',
    check_detail     text                               null comment '错词详情',
    target_detail    text                               not null comment '修正词汇',
    status           int                                not null comment '数据状态',
    create_time      datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    source_url       text                               not null comment '源网站地址',
    deleted          int      default 0                 not null comment '删除标记',
    spider_config_id int                                not null comment '采集配置id',
    web_icon         varchar(600)                       null comment '网站图标',
    title_desc       text                               null comment '文章描述',
    dept_id          bigint                             null comment '部门id',
    creator          varchar(200)                       null comment '创建者',
    updater          varchar(200)                       null comment '更新人'
)
    comment '详情检测信息表-用户审核';

create index wenxun_detail_check_info_spider_config_id_index
    on wenxun_detail_check_audit_info (spider_config_id)
    comment '配置源';

create table wenxun_detail_check_info
(
    id               bigint auto_increment comment '主键'
        primary key,
    check_source     int                                not null comment '检查源，1 敏感词，2，错词，3，接口',
    check_detail     text                               null comment '错词详情',
    target_detail    text                               not null comment '修正词汇',
    status           int                                not null comment '数据状态',
    create_time      datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    source_url       text                               not null comment '源网站地址',
    deleted          int      default 0                 not null comment '删除标记',
    spider_config_id int                                not null comment '采集配置id',
    web_icon         varchar(600)                       null comment '网站图标',
    title_desc       text                               null comment '文章描述',
    dept_id          bigint                             null comment '部门信息',
    creator          varchar(200)                       not null comment '创建人',
    updater          varchar(200)                       null comment '更新人'
)
    comment '详情检测信息表';

create index wenxun_detail_check_info_spider_config_id_index
    on wenxun_detail_check_info (spider_config_id)
    comment '配置源';

create table wenxun_dict_data
(
    id          bigint auto_increment comment '字典编码'
        primary key,
    sort        int          default 0                 not null comment '字典排序',
    label       varchar(100) default ''                not null comment '字典标签',
    value       varchar(100) default ''                not null comment '字典键值',
    dict_type   varchar(100) default ''                not null comment '字典类型',
    status      tinyint      default 0                 not null comment '状态（0正常 1停用）',
    color_type  varchar(100) default ''                null comment '颜色类型',
    css_class   varchar(100) default ''                null comment 'css 样式',
    remark      varchar(500)                           null comment '备注',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除'
)
    comment '文巡-字典数据表' collate = utf8mb4_unicode_ci;

create table wenxun_dict_type
(
    id           bigint auto_increment comment '字典主键'
        primary key,
    name         varchar(100) default ''                not null comment '字典名称',
    type         varchar(100) default ''                not null comment '字典类型',
    status       tinyint      default 0                 not null comment '状态（0正常 1停用）',
    remark       varchar(500)                           null comment '备注',
    creator      varchar(64)  default ''                null comment '创建者',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater      varchar(64)  default ''                null comment '更新者',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted      bit          default b'0'              not null comment '是否删除',
    deleted_time datetime                               null comment '删除时间',
    dept_id      bigint                                 null comment '部门id'
)
    comment '文巡-字典类型表' collate = utf8mb4_unicode_ci;

create table wenxun_draft_data
(
    id          bigint auto_increment comment '草稿id'
        primary key,
    content     text                                  not null comment '草稿详情',
    remark      varchar(1000)                         null comment '备注',
    creator     varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit         default b'0'              not null comment '是否删除',
    user_id     bigint                                not null comment '用户id'
)
    comment '文巡-在线检测草稿表' collate = utf8mb4_unicode_ci;

create table wenxun_spider_crawl_detail
(
    id               bigint auto_increment
        primary key,
    spider_url       varchar(500)                        not null,
    title            varchar(500)                        null comment '文章标题',
    content          text                                null comment '文章正文',
    date             varchar(50)                         null,
    author           varchar(100)                        null comment '作者',
    remark           text                                null comment '备注',
    spider_config_id bigint                              not null comment '采集配置表关联id',
    create_time      timestamp default CURRENT_TIMESTAMP not null,
    update_time      timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    status           int       default 0                 not null comment '数据状态',
    title_desc       text                                null comment '文章简述',
    icon             varchar(600)                        null comment '网站图标',
    dept_id          bigint                              null comment '部门id'
)
    comment '采集数据详情表';

create index wenxun_spider_crawl_detail_spider_url_index
    on wenxun_spider_crawl_detail (spider_url);

create table wenxun_spider_source_config
(
    id              bigint auto_increment comment '主键'
        primary key,
    spider_url      varchar(500)                         not null comment '采集地址',
    spider_name     varchar(500)                         not null comment '采集地址名称',
    spider_model    varchar(500)                         not null comment '采集模块标准名称',
    ping_status     int                                  not null comment '连通状态',
    status          int                                  not null comment '数据有效状态',
    create_time     timestamp  default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    dept_id         bigint                               not null comment '所属部门代码',
    creator         varchar(64)                          not null comment '创建人',
    updater         varchar(64)                          not null comment '更新人',
    remark          varchar(500)                         not null comment '备注',
    spider_page_num int                                  not null comment '采集页数',
    body_xpath      varchar(200)                         not null comment '列表xpath配置',
    next_page_xpath varchar(200)                         null,
    title_xpath     varchar(1000)                        null comment '标题配置',
    date_xpath      varchar(1000)                        null comment '日期配置',
    desc_xpath      varchar(1000)                        null comment '文章描述配置',
    item_xpath      varchar(1000)                        null comment '文章块配置',
    list_xpath      varchar(1000)                        null comment '列表配置',
    deleted         tinyint(1) default 0                 not null comment '是否删除'
)
    comment '文巡-用户采集数据源配置';

create index wenxun_spider_source_config_create_time_index
    on wenxun_spider_source_config (create_time)
    comment '创建时间';

create index wenxun_spider_source_config_spider_model_index
    on wenxun_spider_source_config (spider_model)
    comment '采集模块';

create index wenxun_spider_source_config_spider_url_index
    on wenxun_spider_source_config (spider_url);

create table wenxun_typo_checklist
(
    id          bigint auto_increment comment '主键'
        primary key,
    typo        text                               not null comment '异常词汇',
    correction  text                               not null comment '校政词汇',
    status      int                                not null comment '数据状态。0未修复，1已修复，2不存在，3.无需处理',
    updater     varchar(500)                       not null comment '操作人',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    spider_url  varchar(500)                       not null comment '文章地址',
    color_type  varchar(500)                       not null comment '错词等级'
)
    comment '错词检查';

create index wenxun_typo_checklist_spider_url_index
    on wenxun_typo_checklist (spider_url);

create index wenxun_typo_checklist_status_index
    on wenxun_typo_checklist (status);

create table wenxun_url_change_info
(
    id            int                                  not null comment '主键，同爬虫配置表主键相同',
    url_name      varchar(500)                         null comment '地址名称',
    all_count     int                                  null comment '总检查次数',
    success_count int                                  null comment '更新天数',
    fail_count    int                                  null comment '未更新天数',
    create_time   datetime   default CURRENT_TIMESTAMP not null comment '首次检测时间',
    update_time   datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    url           varchar(2000)                        not null comment 'url',
    last_title    varchar(2000)                        null comment '最后标题',
    dept_id       bigint                               null comment '部门id',
    deleted       tinyint(1) default 0                 null comment '删除标记',
    creator       varchar(100)                         null comment '创建人',
    updater       varchar(200)                         null comment '更新人'
)
    comment '网站更新检查';

create table wenxun_url_change_log
(
    id          int auto_increment comment '主键，同爬虫配置表主键相同'
        primary key,
    url_name    varchar(500)                                                     null comment '地址名称',
    spider_id   int                                                              not null comment '主键，同爬虫主键相同',
    status      tinyint                                default 1                 not null comment '状态（1正常 0停用）',
    updater     varchar(64) collate utf8mb4_unicode_ci default ''                null comment '更新者',
    create_time datetime                               default CURRENT_TIMESTAMP not null comment '首次检测时间',
    update_time datetime                               default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    title       varchar(2000)                                                    null comment '标题',
    url         varchar(2000)                                                    not null comment 'url',
    dept_id     bigint                                                           null comment '部门id'
)
    comment '网站更新检查-日志表';

create table wenxun_url_ping_info
(
    id            int                                  not null comment '主键，同爬虫配置表主键相同',
    url_name      varchar(500)                         null comment '地址名称',
    all_count     int                                  null comment '总检查次数',
    success_count int                                  null comment '连通成功次数',
    fail_count    int                                  null comment '异常检查次数',
    create_time   datetime   default CURRENT_TIMESTAMP not null comment '首次检测时间',
    update_time   datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    url           varchar(2000)                        not null comment 'url',
    dept_id       bigint                               null comment '部门id',
    creator       varchar(100)                         null comment '更新人',
    updater       varchar(100)                         null comment '更新人',
    deleted       tinyint(1) default 0                 null comment '删除标记'
)
    comment '网页连通记录';

create table wenxun_url_ping_log
(
    id          int auto_increment comment '主键，同爬虫配置表主键相同'
        primary key,
    url_name    varchar(500)                                                     null comment '地址名称',
    ping_id     int                                                              not null comment '主键，同连通表主键相同',
    status      tinyint                                default 1                 not null comment '状态（1正常 0停用）',
    updater     varchar(64) collate utf8mb4_unicode_ci default ''                null comment '更新者',
    create_time datetime                               default CURRENT_TIMESTAMP not null comment '首次检测时间',
    update_time datetime                               default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    ping_code   varchar(5)                                                       null comment '网络状态码',
    url         varchar(2000)                                                    not null comment 'url'
)
    comment '网页连通记录-日志表';

create table yudao_demo01_contact
(
    id          bigint auto_increment comment '编号'
        primary key,
    name        varchar(100) default ''                not null comment '名字',
    sex         tinyint(1)                             not null comment '性别',
    birthday    datetime                               not null comment '出生年',
    description varchar(255)                           not null comment '简介',
    avatar      varchar(512)                           null comment '头像',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除',
    tenant_id   bigint       default 0                 not null comment '租户编号'
)
    comment '示例联系人表' collate = utf8mb4_unicode_ci;

create table yudao_demo02_category
(
    id          bigint auto_increment comment '编号'
        primary key,
    name        varchar(100) default ''                not null comment '名字',
    parent_id   bigint                                 not null comment '父级编号',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除',
    tenant_id   bigint       default 0                 not null comment '租户编号'
)
    comment '示例分类表' collate = utf8mb4_unicode_ci;

create table yudao_demo03_course
(
    id          bigint auto_increment comment '编号'
        primary key,
    student_id  bigint                                 not null comment '学生编号',
    name        varchar(100) default ''                not null comment '名字',
    score       tinyint                                not null comment '分数',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除',
    tenant_id   bigint       default 0                 not null comment '租户编号'
)
    comment '学生课程表' collate = utf8mb4_unicode_ci;

create table yudao_demo03_grade
(
    id          bigint auto_increment comment '编号'
        primary key,
    student_id  bigint                                 not null comment '学生编号',
    name        varchar(100) default ''                not null comment '名字',
    teacher     varchar(255)                           not null comment '班主任',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除',
    tenant_id   bigint       default 0                 not null comment '租户编号'
)
    comment '学生班级表' collate = utf8mb4_unicode_ci;

create table yudao_demo03_student
(
    id          bigint auto_increment comment '编号'
        primary key,
    name        varchar(100) default ''                not null comment '名字',
    sex         tinyint                                not null comment '性别',
    birthday    datetime                               not null comment '出生日期',
    description varchar(255)                           not null comment '简介',
    creator     varchar(64)  default ''                null comment '创建者',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)  default ''                null comment '更新者',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     bit          default b'0'              not null comment '是否删除',
    tenant_id   bigint       default 0                 not null comment '租户编号'
)
    comment '学生表' collate = utf8mb4_unicode_ci;

