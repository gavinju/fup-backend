create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                           null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userRole     varchar(256) default 'user'            not null comment '用户角色 user-普通用户 admin-管理员',
    userName     varchar(256)                           null comment '用户昵称',
    avatarUrl    varchar(1024)                          null comment '用户头像',
    gender       tinyint      default 1                 null comment '性别',
    phone        varchar(128)                           null comment '电话',
    email        varchar(512)                           null comment '邮箱',
    userStatus   int          default 0                 not null comment '状态 0 - 正常',
    createTime   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint      default 0                 not null comment '是否删除'
)
    comment '用户';

INSERT INTO user (id, userAccount, userPassword, userRole, userName, avatarUrl, gender, phone, email, userStatus, createTime, updateTime, isDelete) VALUES (1, 'admin', '3014dcb9ee3639535d5d9301b32c840c', 'admin', '管理员', 'http://niu.liangtianyu.space/user.png', 1, null, null, 0, '2023-03-19 09:12:41', '2023-06-04 19:00:36', 0);
INSERT INTO user (id, userAccount, userPassword, userRole, userName, avatarUrl, gender, phone, email, userStatus, createTime, updateTime, isDelete) VALUES (2, 'test', '3014dcb9ee3639535d5d9301b32c840c', 'user', '测试账户', 'http://niu.liangtianyu.space/user.png', 0, null, null, 0, '2023-03-19 09:13:05', '2023-06-27 14:13:34', 0);

create table file
(
    id          bigint auto_increment comment 'id'
        primary key,
    url         varchar(512)                       null comment '文件地址',
    size        bigint unsigned                    null comment '文件大小',
    userId      bigint                             null comment '用户id',
    type        varchar(255)                       null comment '分类',
    description varchar(255)                       null comment '描述',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '文件管理' charset = utf8mb4;