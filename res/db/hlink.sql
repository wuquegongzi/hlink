/*
 Navicat Premium Data Transfer

 Source Server         : flink-test
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 127.0.0.1:3306
 Source Schema         : flink_hlink

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 06/04/2020 11:38:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pvuv_sink
-- ----------------------------
DROP TABLE IF EXISTS `pvuv_sink`;
CREATE TABLE `pvuv_sink` (
  `dt` varchar(255) NOT NULL,
  `pv` bigint(20) DEFAULT NULL,
  `uv` bigint(20) DEFAULT NULL,
  `source` varchar(255) NOT NULL,
  `res1` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dt`,`source`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of pvuv_sink
-- ----------------------------
BEGIN;
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 01:00', 234475, 30837, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 01:00', 234475, 30837, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 01:00', 1, 1, 'obj_qt', 'china');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 02:00', 269510, 35261, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 02:00', 269510, 35261, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 03:00', 265675, 35302, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 03:00', 265675, 35302, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 04:00', 249315, 33537, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 04:00', 249315, 33537, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 05:00', 271525, 35748, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 05:00', 271525, 35748, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 06:00', 283590, 36934, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 06:00', 283590, 36934, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 07:00', 291620, 37763, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 07:00', 291620, 37763, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 08:00', 293360, 37961, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 08:00', 293360, 37961, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 09:00', 269510, 35356, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 09:00', 269510, 35356, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 10:00', 70, 14, 'cli_3', '');
INSERT INTO `pvuv_sink` VALUES ('2017-11-26 10:00', 70, 14, 'cli_4', '');
INSERT INTO `pvuv_sink` VALUES ('2018-11-26 01:00', 1, 1, 'obj_qt', 'mars');
INSERT INTO `pvuv_sink` VALUES ('2018-11-26 01:00', 1, 1, 'obj_qt2', 'mars');
INSERT INTO `pvuv_sink` VALUES ('2018-11-26 01:00', 1, 1, '上海网络', 'china');
INSERT INTO `pvuv_sink` VALUES ('2018-11-26 01:00', 1, 1, 't3', 'mars');
INSERT INTO `pvuv_sink` VALUES ('2018-11-26 01:00', 1, 1, 't2', '文二路');
INSERT INTO `pvuv_sink` VALUES ('2018-11-26 01:00', 1, 1, 't1', '文一路');
INSERT INTO `pvuv_sink` VALUES ('2019-11-26 01:00', 1, 1, 'obj_qt2', 't3');
COMMIT;

-- ----------------------------
-- Table structure for side_test
-- ----------------------------
DROP TABLE IF EXISTS `side_test`;
CREATE TABLE `side_test` (
  `a` varchar(50) NOT NULL,
  `b` bigint(50) DEFAULT NULL,
  `c` varchar(50) DEFAULT NULL,
  `d` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`a`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of side_test
-- ----------------------------
BEGIN;
INSERT INTO `side_test` VALUES ('1111', 3333, '2222', '44444');
INSERT INTO `side_test` VALUES ('7777', 8888, '2222', '9999');
INSERT INTO `side_test` VALUES ('world', 100, 'zc1', 'ml');
COMMIT;

-- ----------------------------
-- Table structure for sys_login_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_info`;
CREATE TABLE `sys_login_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `login_name` varchar(50) DEFAULT '' COMMENT '登录账号',
  `ipaddr` varchar(50) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='系统访问记录';

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(50) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';

-- ----------------------------
-- Table structure for sys_shedlock
-- ----------------------------
DROP TABLE IF EXISTS `sys_shedlock`;
CREATE TABLE `sys_shedlock` (
  `name` varchar(64) COLLATE utf8_bin NOT NULL,
  `lock_until` timestamp(3) NULL DEFAULT NULL,
  `locked_at` timestamp(3) NULL DEFAULT NULL,
  `locked_by` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of sys_shedlock
-- ----------------------------
BEGIN;
INSERT INTO `sys_shedlock` VALUES ('schedule_job_synchronization', '2020-04-06 11:38:00.050', '2020-04-06 11:38:00.006', 'haibao_admin');
INSERT INTO `sys_shedlock` VALUES ('schedule_job_synchronization2', '2020-04-03 23:57:27.233', '2020-04-03 23:57:00.027', 'haibao_admin');
COMMIT;

-- ----------------------------
-- Table structure for t_cluster
-- ----------------------------
DROP TABLE IF EXISTS `t_cluster`;
CREATE TABLE `t_cluster` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '集群名称',
  `type` tinyint(2) DEFAULT NULL COMMENT '集群类型,枚举,',
  `address` varchar(100) DEFAULT NULL COMMENT '集群地址',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除标识，逻辑删除。0正常 ,1 删除',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_cluster
-- ----------------------------
BEGIN;
INSERT INTO `t_cluster` VALUES (3, '测试环境', 0, 'http://10.57.30.38:8081', NULL, 0, NULL, NULL, NULL, '2020-03-26 11:31:37');
INSERT INTO `t_cluster` VALUES (6, '本地测试集群', 1, 'http://localhost:8081', NULL, 0, NULL, NULL, NULL, '2020-04-01 13:54:25');
COMMIT;

-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_name` varchar(100) DEFAULT NULL COMMENT '字典名称',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- ----------------------------
-- Records of t_dict
-- ----------------------------
BEGIN;
INSERT INTO `t_dict` VALUES (1, NULL, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '性别男');
INSERT INTO `t_dict` VALUES (2, NULL, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '性别女');
INSERT INTO `t_dict` VALUES (3, NULL, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '性别未知');
INSERT INTO `t_dict` VALUES (4, NULL, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '显示菜单');
INSERT INTO `t_dict` VALUES (5, NULL, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '隐藏菜单');
INSERT INTO `t_dict` VALUES (6, NULL, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '正常状态');
INSERT INTO `t_dict` VALUES (7, NULL, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '停用状态');
INSERT INTO `t_dict` VALUES (8, NULL, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '正常状态');
INSERT INTO `t_dict` VALUES (9, NULL, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '停用状态');
INSERT INTO `t_dict` VALUES (10, NULL, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '默认分组');
INSERT INTO `t_dict` VALUES (11, NULL, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '系统分组');
INSERT INTO `t_dict` VALUES (12, NULL, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '系统默认是');
INSERT INTO `t_dict` VALUES (13, NULL, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '系统默认否');
INSERT INTO `t_dict` VALUES (14, NULL, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '通知');
INSERT INTO `t_dict` VALUES (15, NULL, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '公告');
INSERT INTO `t_dict` VALUES (16, NULL, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '正常状态');
INSERT INTO `t_dict` VALUES (17, NULL, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '关闭状态');
INSERT INTO `t_dict` VALUES (18, NULL, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '新增操作');
INSERT INTO `t_dict` VALUES (19, NULL, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '修改操作');
INSERT INTO `t_dict` VALUES (20, NULL, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '删除操作');
INSERT INTO `t_dict` VALUES (21, NULL, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '授权操作');
INSERT INTO `t_dict` VALUES (22, NULL, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '导出操作');
INSERT INTO `t_dict` VALUES (23, NULL, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '导入操作');
INSERT INTO `t_dict` VALUES (24, NULL, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '强退操作');
INSERT INTO `t_dict` VALUES (25, NULL, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '生成操作');
INSERT INTO `t_dict` VALUES (26, NULL, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '清空操作');
INSERT INTO `t_dict` VALUES (27, NULL, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '正常状态');
INSERT INTO `t_dict` VALUES (28, NULL, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2018-03-16 11:33:00', NULL, NULL, '停用状态');
INSERT INTO `t_dict` VALUES (100, '源表类型', 0, 'kafka', 'kafka', 'dc_type_source', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-02-25 16:06:08', NULL);
INSERT INTO `t_dict` VALUES (101, '维表类型', 0, 'kafka', 'kafka', 'dc_type_side', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-02-25 16:02:32', NULL);
INSERT INTO `t_dict` VALUES (102, '维表类型', 1, 'mysql', 'mysql', 'dc_type_side', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-02-25 16:06:22', NULL);
INSERT INTO `t_dict` VALUES (103, '维表类型', 2, 'oracle', 'oracle', 'dc_type_side', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-02-25 16:02:34', NULL);
INSERT INTO `t_dict` VALUES (104, '维表类型', 3, 'http', 'http', 'dc_type_side', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-02-25 16:02:36', NULL);
INSERT INTO `t_dict` VALUES (105, '结果表类型', 0, 'kafka', 'kafka', 'dc_type_slink', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-02-25 16:02:37', NULL);
INSERT INTO `t_dict` VALUES (106, '结果表类型', 1, 'mysql', 'mysql', 'dc_type_slink', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-02-25 16:06:25', NULL);
INSERT INTO `t_dict` VALUES (107, '结果表类型', 2, 'oracle', 'oracle', 'dc_type_slink', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-02-25 16:02:44', NULL);
INSERT INTO `t_dict` VALUES (108, '结果表类型', 3, 'hbase', 'hbase', 'dc_type_slink', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-02-25 16:02:46', NULL);
INSERT INTO `t_dict` VALUES (109, '数据源功能类型', 0, '源表', 'source', 'ds_type', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-02-25 16:08:36', NULL);
INSERT INTO `t_dict` VALUES (110, '数据源功能类型', 1, '目标表', 'sink', 'ds_type', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-02-25 16:08:42', NULL);
INSERT INTO `t_dict` VALUES (111, '数据源功能类型', 2, '维表', 'side', 'ds_type', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-02-25 16:09:01', NULL);
INSERT INTO `t_dict` VALUES (112, '数据源功能类型', 3, 'UDF', 'udf', 'ds_type', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-02-25 16:09:24', NULL);
INSERT INTO `t_dict` VALUES (113, 'schema类型', 0, 'json', '0', 'schema_type', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-03-09 15:13:04', NULL);
INSERT INTO `t_dict` VALUES (114, 'schema类型', 1, 'avro', '1', 'schema_type', NULL, NULL, 'N', '1', 'ml.c', NULL, NULL, '2020-03-09 15:13:15', NULL);
INSERT INTO `t_dict` VALUES (115, '维表类型', 4, 'redis', 'redis', 'dc_type_side', NULL, NULL, 'N', '1', 'ml.c', NULL, NULL, '2020-03-09 15:16:26', NULL);
INSERT INTO `t_dict` VALUES (116, 'Flink时间类型', 0, 'IngestionTime', '0', 'flink_time_type', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-03-09 15:18:43', NULL);
INSERT INTO `t_dict` VALUES (117, 'Flink时间类型', 1, 'ProcessingTime', '1', 'flink_time_type', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-03-09 15:20:09', NULL);
INSERT INTO `t_dict` VALUES (118, 'Flink时间类型', 2, 'EventTime', '2', 'flink_time_type', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-03-09 15:27:08', NULL);
INSERT INTO `t_dict` VALUES (119, '检查点模式', 0, 'EXACTLY_ONCE', '0', 'checkpoint_mode', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-03-09 15:28:52', NULL);
INSERT INTO `t_dict` VALUES (120, '检查点模式', 1, 'AT_LEAST_ONCE', '1', 'checkpoint_mode', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-03-09 15:28:47', NULL);
INSERT INTO `t_dict` VALUES (121, '检查点清理模式', 0, 'RETAIN_ON_CANCELLATION', '0', 'checkpoint_cleanup_mode', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-03-09 15:30:58', NULL);
INSERT INTO `t_dict` VALUES (122, '检查点清理模式', 1, 'DELETE_ON_CANCELLATION', '1', 'checkpoint_cleanup_mode', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-03-09 15:32:00', NULL);
INSERT INTO `t_dict` VALUES (123, '重启策略', 0, '无重启 (No Restart) 策略', '0', 'restart_strategy', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-03-09 15:47:41', NULL);
INSERT INTO `t_dict` VALUES (124, '重启策略', 1, '固定间隔 (Fixed Delay) 重启策略', '1', 'restart_strategy', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-03-09 16:25:00', NULL);
INSERT INTO `t_dict` VALUES (125, '重启策略', 2, '失败率 (Failure Rate) 重启策略', '2', 'restart_strategy', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-03-09 16:25:03', NULL);
INSERT INTO `t_dict` VALUES (126, '作业运行状态', 0, '未提交', '0', 'job_status', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-04-03 20:27:52', NULL);
INSERT INTO `t_dict` VALUES (127, '作业运行状态', 1, '已生成', '1', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:28:43', NULL);
INSERT INTO `t_dict` VALUES (128, '作业运行状态', 2, '启动中', '2', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:28:44', NULL);
INSERT INTO `t_dict` VALUES (129, '作业运行状态', 3, '运行中', '3', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:28:46', NULL);
INSERT INTO `t_dict` VALUES (130, '作业运行状态', 4, '失败中', '4', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:28:47', NULL);
INSERT INTO `t_dict` VALUES (131, '作业运行状态', 5, '已失败', '5', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:28:50', NULL);
INSERT INTO `t_dict` VALUES (132, '集群类型', 0, 'local', '0', 'cluster_type', NULL, NULL, 'N', '1', 'ml.c', NULL, NULL, '2020-04-03 17:58:32', '本地模式');
INSERT INTO `t_dict` VALUES (133, '集群类型', 1, 'standalone', '1', 'cluster_type', NULL, NULL, 'Y', '0', 'ml.c', NULL, NULL, '2020-04-03 17:58:34', '提交到独立部署模式的flink集群');
INSERT INTO `t_dict` VALUES (134, '集群类型', 2, 'yarn', '2', 'cluster_type', NULL, NULL, 'N', '1', 'ml.c', NULL, NULL, '2020-04-03 17:58:35', '提交到yarn模式的flink集群(即提交到已有flink集群)');
INSERT INTO `t_dict` VALUES (135, '集群类型', 3, 'yarnPer', '3', 'cluster_type', NULL, NULL, 'N', '1', 'ml.c', NULL, NULL, '2020-04-03 17:58:36', 'yarn per_job模式提交(即创建新flink application)');
INSERT INTO `t_dict` VALUES (136, '作业运行状态', 6, '停止中', '6', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:29:03', NULL);
INSERT INTO `t_dict` VALUES (137, '作业运行状态', 7, '已停止', '7', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:29:13', NULL);
INSERT INTO `t_dict` VALUES (138, '作业运行状态', 8, '已完成', '8', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:29:34', NULL);
INSERT INTO `t_dict` VALUES (139, '作业运行状态', 9, '重启中', '9', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:31:27', NULL);
INSERT INTO `t_dict` VALUES (140, '作业运行状态', 10, '暂停中', '10', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:31:28', NULL);
INSERT INTO `t_dict` VALUES (141, '作业运行状态', 11, '已暂停', '11', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:31:29', NULL);
INSERT INTO `t_dict` VALUES (142, '作业运行状态', 12, '调度中', '12', 'job_status', NULL, NULL, 'N', '0', 'ml.c', NULL, NULL, '2020-04-03 20:31:32', NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_ds
-- ----------------------------
DROP TABLE IF EXISTS `t_ds`;
CREATE TABLE `t_ds` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ds_name` varchar(255) DEFAULT NULL COMMENT '数据源名称',
  `ds_type` varchar(10) DEFAULT NULL COMMENT '功能类型 见字典 ds_type',
  `table_type` varchar(10) DEFAULT NULL COMMENT '表类型，见字典表 dc_type_source、side、slink',
  `schema_type` tinyint(1) DEFAULT '0' COMMENT '见字典schema_type,默认0 json',
  `structure_type` tinyint(1) DEFAULT '0' COMMENT '结构类型，单一结构-0或者多层嵌套结构-1',
  `schema_file` text COMMENT 'schema文件描述',
  `table_name` varchar(255) DEFAULT NULL COMMENT '别名，用于SQL',
  `ddl_enable` tinyint(1) DEFAULT NULL COMMENT '是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段',
  `ds_ddl` text COMMENT 'ddl建表语句，维表不需要',
  `ds_version` int(10) DEFAULT NULL COMMENT '版本号，保留字段',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COMMENT='数据源表';

-- ----------------------------
-- Records of t_ds
-- ----------------------------
BEGIN;
INSERT INTO `t_ds` VALUES (15, 'testSourceKafa01', 'source', 'kafka', 0, 0, '{\n  \"type\": \"object\",\n  \"properties\": {\n    \"user_id\": {\n      \"type\": \"string\"\n    },\n    \"item_id\": {\n      \"type\": \"string\"\n    },\n    \"category_id\": {\n      \"type\": \"string\"\n    },\n    \"ts\": {\n      \"type\": \"string\",\n      \"format\": \"date-time\"\n    }\n  }\n}', 'user_log', 1, 'CREATE TABLE user_log (\n    user_id  VARCHAR ,\n    item_id  VARCHAR ,\n    category_id  VARCHAR ,\n    ts  TIMESTAMP \n) WITH (\n\'connector.type\' = \'kafka\',\n\'connector.version\' = \'universal\',\n\'connector.topic\' = \'user_behavior\',\n\'update-mode\' = \'append\',\n\'connector.properties.0.key\' = \'zookeeper.connect\',\n\'connector.properties.0.value\' = \'localhost:2181\',\n\'connector.properties.1.key\' = \'bootstrap.servers\',\n\'connector.properties.1.value\' = \'localhost:9092\',\n\'connector.properties.2.key\' = \'group.id\',\n\'connector.properties.2.value\' = \'testGroup\',\n\'connector.startup-mode\' = \'specific-offsets\',\n\'format.type\' = \'json\',\n\'format.fail-on-missing-field\' = \'true\',\n\'format.derive-schema\' = \'true\'\n)\n', 0, 'ml.c', '2020-03-17 17:20:01', 'ml.c', '2020-03-27 20:01:54');
INSERT INTO `t_ds` VALUES (17, 'testSinkMysql01', 'sink', 'mysql', 0, 0, '{\n  \"type\": \"object\",\n  \"properties\": {\n    \"dt\": {\n      \"type\": \"string\"\n    },\n    \"pv\": {\n      \"type\": \"integer\"\n    },\n    \"uv\": {\n      \"type\": \"integer\"\n    }\n  }\n}', 'pvuv_sink', 1, 'CREATE TABLE pvuv_sink (\n    dt  VARCHAR ,\n    pv  BIGINT ,\n    uv  BIGINT \n) WITH (\n    \'connector.type\' = \'jdbc\',\n    \'connector.url\' = \'jdbc:mysql://10.58.10.195:3306/flink_hlink\',\n    \'connector.table\' = \'pvuv_sink\',\n    \'connector.username\' = \'root\',\n    \'connector.password\' = \'123456\',\n    \'connector.write.flush.max-rows\' = \'5000\'\n)', 0, 'ml.c', '2020-03-17 17:20:01', 'ml.c', '2020-03-27 20:09:42');
INSERT INTO `t_ds` VALUES (22, 'test_source_kafa_obj_nesting_01', 'source', 'kafka', 0, 0, '{\n  \"type\": \"object\",\n  \"properties\": {\n    \"user_id\": {\n      \"type\": \"string\"\n    },\n    \"item_id\": {\n      \"type\": \"string\"\n    },\n    \"category_id\": {\n      \"type\": \"string\"\n    },\n    \"work\":{\n            \"type\":\"object\",\n            \"properties\":{\n                \"address\":{\n                    \"type\":\"string\"\n                  },\n                \"company\":{\n                    \"type\":\"string\"\n                  }\n            }\n        },\n    \"ts\": {\n      \"type\": \"string\",\n      \"format\": \"date-time\"\n    }\n  }\n}', 'user_obj_qt_log', 1, 'CREATE TABLE user_obj_qt_log (\n    user_id  VARCHAR ,\n    item_id  VARCHAR ,\n    category_id  VARCHAR ,\n    work  ROW<address VARCHAR,company VARCHAR> ,\n    ts  TIMESTAMP \n) WITH (\n\'connector.type\' = \'kafka\',\n\'connector.version\' = \'universal\',\n\'connector.topic\' = \'obj_nesting_data\',\n\'update-mode\' = \'append\',\n\'connector.properties.0.key\' = \'zookeeper.connect\',\n\'connector.properties.0.value\' = \'localhost:2181\',\n\'connector.properties.1.key\' = \'bootstrap.servers\',\n\'connector.properties.1.value\' = \'localhost:9092\',\n\'connector.properties.2.key\' = \'group.id\',\n\'connector.properties.2.value\' = \'obj_nesting_group\',\n\'connector.startup-mode\' = \'earliest-offset\',\n\'format.type\' = \'json\',\n\'format.fail-on-missing-field\' = \'true\',\n\'format.derive-schema\' = \'true\'\n)\n', 0, 'ml.c', '2020-03-17 17:20:01', '', '2020-04-01 19:46:11');
INSERT INTO `t_ds` VALUES (23, '维表', 'side', 'http', 0, 0, NULL, '第一个维表', 0, NULL, NULL, NULL, NULL, NULL, '2020-04-02 17:09:37');
COMMIT;

-- ----------------------------
-- Table structure for t_ds_json_field
-- ----------------------------
DROP TABLE IF EXISTS `t_ds_json_field`;
CREATE TABLE `t_ds_json_field` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ds_id` bigint(20) DEFAULT NULL COMMENT '关联数据源定义表t_ds',
  `json_value` text COMMENT '属性值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='数据源非公共属性值';

-- ----------------------------
-- Records of t_ds_json_field
-- ----------------------------
BEGIN;
INSERT INTO `t_ds_json_field` VALUES (14, 15, '[{\"label\":\"zookeeper.connect\",\"fieldName\":\"zookeeper_connect\",\"type\":\"inputfield\",\"required\":true,\"sequence\":1,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"zookeeper连接地址，多个逗号分割\",\"describe\":\"\",\"fieldValue\":\"localhost:2181\",\"multiple\":false},{\"label\":\"bootstrap.servers\",\"fieldName\":\"bootstrap_servers\",\"type\":\"inputfield\",\"required\":true,\"sequence\":2,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"your_bootstrap_servers,多个逗号分割\",\"describe\":\"\",\"fieldValue\":\"localhost:9092\",\"multiple\":false},{\"label\":\"connector.topic\",\"fieldName\":\"connector_topic\",\"type\":\"inputfield\",\"required\":true,\"sequence\":3,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"your_topic\",\"describe\":\"\",\"fieldValue\":\"user_behavior\",\"multiple\":false},{\"label\":\"group.id\",\"fieldName\":\"group_id\",\"type\":\"inputfield\",\"required\":true,\"sequence\":4,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"your_kafka_consumer_group\",\"describe\":\"\",\"fieldValue\":\"testGroup\",\"multiple\":false},{\"label\":\"自动偏移量\",\"fieldName\":\"startup_mode\",\"type\":\"combobox\",\"required\":false,\"sequence\":5,\"defaultValue\":\"earliest-offset\",\"readOnly\":false,\"tips\":\"auto.offset.reset\",\"describe\":\"\",\"fieldValue\":\"specific-offsets\",\"multiple\":false,\"option\":[{\"name\":\"earliest-offset\",\"dName\":\"earliest\",\"hasUnion\":false},{\"name\":\"latest-offset\",\"dName\":\"latest\",\"hasUnion\":false},{\"name\":\"group-offsets\",\"dName\":\"group\",\"hasUnion\":false},{\"name\":\"specific-offsets\",\"dName\":\"specific\",\"hasUnion\":true,\"unionFields\":[{\"label\":\"特定偏移量\",\"fieldName\":\"partition_offset\",\"type\":\"inputfield\",\"required\":true,\"hidden\":true,\"sequence\":1,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"填写特定分区以及偏移量，使用逗号分割。多组使用分号分割。 eg:0,42;1,300\",\"describe\":\"\",\"fieldValue\":\"11\"}]}]}]');
INSERT INTO `t_ds_json_field` VALUES (16, 17, '[{\"label\":\"连接地址\",\"fieldName\":\"connector_url\",\"type\":\"inputfield\",\"required\":true,\"sequence\":1,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"数据库连接地址，eg:\",\"describe\":\"\",\"fieldValue\":\"jdbc:mysql://10.58.10.195:3306/flink_hlink\",\"multiple\":false},{\"label\":\"表名\",\"fieldName\":\"table_name\",\"type\":\"inputfield\",\"required\":true,\"sequence\":2,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"mysql表名称\",\"describe\":\"\",\"fieldValue\":\"pvuv_sink\",\"multiple\":false},{\"label\":\"用户名\",\"fieldName\":\"connector_username\",\"type\":\"inputfield\",\"required\":true,\"sequence\":3,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"mysql连接用户名\",\"describe\":\"\",\"fieldValue\":\"root\",\"multiple\":false},{\"label\":\"密码\",\"fieldName\":\"connector_password\",\"type\":\"inputfield\",\"required\":true,\"sequence\":4,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"mysql连接密码\",\"describe\":\"password\",\"fieldValue\":\"123456\",\"multiple\":false},{\"label\":\"一次至多写入条数\",\"fieldName\":\"write_max_rows\",\"type\":\"numberfield\",\"required\":true,\"sequence\":3,\"defaultValue\":\"5000\",\"readOnly\":false,\"tips\":\"\",\"describe\":\"\",\"fieldValue\":\"5000\",\"multiple\":false}]');
INSERT INTO `t_ds_json_field` VALUES (21, 22, '[{\"label\":\"zookeeper.connect\",\"fieldName\":\"zookeeper_connect\",\"type\":\"inputfield\",\"required\":true,\"sequence\":1,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"zookeeper连接地址，多个逗号分割\",\"describe\":\"\",\"fieldValue\":\"localhost:2181\",\"multiple\":false},{\"label\":\"bootstrap.servers\",\"fieldName\":\"bootstrap_servers\",\"type\":\"inputfield\",\"required\":true,\"sequence\":2,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"your_bootstrap_servers,多个逗号分割\",\"describe\":\"\",\"fieldValue\":\"localhost:9092\",\"multiple\":false},{\"label\":\"connector.topic\",\"fieldName\":\"connector_topic\",\"type\":\"inputfield\",\"required\":true,\"sequence\":3,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"your_topic\",\"describe\":\"\",\"fieldValue\":\"obj_nesting_data\",\"multiple\":false},{\"label\":\"group.id\",\"fieldName\":\"group_id\",\"type\":\"inputfield\",\"required\":true,\"sequence\":4,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"your_kafka_consumer_group\",\"describe\":\"\",\"fieldValue\":\"obj_nesting_group\",\"multiple\":false},{\"label\":\"自动偏移量\",\"fieldName\":\"startup_mode\",\"type\":\"combobox\",\"required\":false,\"sequence\":5,\"defaultValue\":\"earliest-offset\",\"readOnly\":false,\"tips\":\"auto.offset.reset\",\"describe\":\"\",\"fieldValue\":\"earliest-offset\",\"multiple\":false,\"option\":[{\"name\":\"earliest-offset\",\"dName\":\"earliest\",\"hasUnion\":false},{\"name\":\"latest-offset\",\"dName\":\"latest\",\"hasUnion\":false},{\"name\":\"group-offsets\",\"dName\":\"group\",\"hasUnion\":false},{\"name\":\"specific-offsets\",\"dName\":\"specific\",\"hasUnion\":true,\"unionFields\":[{\"label\":\"特定偏移量\",\"fieldName\":\"partition_offset\",\"type\":\"inputfield\",\"required\":true,\"hidden\":true,\"sequence\":1,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"填写特定分区以及偏移量，使用逗号分割。多组使用分号分割。 eg:0,42;1,300\",\"describe\":\"\",\"fieldValue\":\"\"}]}]}]');
INSERT INTO `t_ds_json_field` VALUES (22, 23, '[{\"label\":\"http请求接口\",\"fieldName\":\"requestUrl\",\"type\":\"inputfield\",\"required\":true,\"sequence\":1,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"http请求地址，eg:\",\"describe\":\"\",\"fieldValue\":\"1\",\"multiple\":false},{\"label\":\"http超时时间(ms)\",\"fieldName\":\"timeOut\",\"type\":\"numberfield\",\"required\":true,\"sequence\":2,\"minLength\":1,\"maxLength\":100,\"defaultValue\":\"\",\"readOnly\":false,\"tips\":\"http超时时间\",\"describe\":\"\",\"fieldValue\":\"1\",\"multiple\":false},{\"label\":\"编码\",\"fieldName\":\"charSet\",\"type\":\"combobox\",\"required\":true,\"sequence\":3,\"defaultValue\":\"utf-8\",\"readOnly\":false,\"tips\":\"\",\"describe\":\"\",\"fieldValue\":\"gbk\",\"multiple\":false,\"option\":[{\"name\":\"utf-8\",\"dName\":\"utf-8\",\"hasUnion\":false},{\"name\":\"gbk\",\"dName\":\"gbk\",\"hasUnion\":false}]},{\"label\":\"缓存\",\"fieldName\":\"cache\",\"type\":\"combobox\",\"required\":false,\"sequence\":4,\"defaultValue\":\"1\",\"readOnly\":false,\"tips\":\"提示\",\"describe\":\"字段备注，供开发人员理解字段功能\",\"fieldValue\":\"none\",\"multiple\":false,\"option\":[{\"name\":\"none\",\"dName\":\"NONE\",\"hasUnion\":false},{\"name\":\"W-TinyLFU\",\"dName\":\"W-TinyLFU\",\"hasUnion\":true,\"unionFields\":[{\"label\":\"initialCapacity\",\"fieldName\":\"初始大小(整数)\",\"type\":\"numberfield\",\"required\":true,\"hidden\":true,\"sequence\":2,\"minLength\":1,\"maxLength\":10000,\"defaultValue\":\"60\",\"readOnly\":false,\"tips\":\"初始的缓存空间大小(整数)\",\"describe\":\"\",\"fieldValue\":\"\"},{\"label\":\"maximumSize\",\"fieldName\":\"缓存最大数（整数)\",\"type\":\"numberfield\",\"required\":true,\"hidden\":true,\"sequence\":1,\"minLength\":1,\"maxLength\":500000,\"defaultValue\":\"10000\",\"readOnly\":false,\"tips\":\"\",\"describe\":\"\",\"fieldValue\":\"\"},{\"label\":\"expireAfterWrite\",\"fieldName\":\"过期时间(s)\",\"type\":\"numberfield\",\"required\":true,\"hidden\":true,\"sequence\":2,\"minLength\":1,\"maxLength\":31536000,\"defaultValue\":\"60\",\"readOnly\":false,\"tips\":\"\",\"describe\":\"\",\"fieldValue\":\"\"}]}]}]');
COMMIT;

-- ----------------------------
-- Table structure for t_ds_schema_column
-- ----------------------------
DROP TABLE IF EXISTS `t_ds_schema_column`;
CREATE TABLE `t_ds_schema_column` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `virtual_id` varchar(20) DEFAULT NULL COMMENT '虚拟ID,采用雪花算法',
  `virtual_pid` varchar(20) DEFAULT '0' COMMENT '虚拟父级ID',
  `ds_id` bigint(20) DEFAULT NULL COMMENT '关联数据源ID，指定所属数据源',
  `name` varchar(20) DEFAULT NULL COMMENT '字段属性名',
  `flink_type` varchar(50) DEFAULT NULL COMMENT '字段属性类型',
  `basic_type` varchar(50) DEFAULT NULL COMMENT '基础数据类型',
  `join_key` tinyint(1) DEFAULT NULL COMMENT '是否是连接key',
  `event_time` tinyint(1) DEFAULT NULL COMMENT '是否是事件时间',
  `comment` varchar(255) DEFAULT NULL COMMENT '描述',
  `level` int(10) DEFAULT NULL COMMENT '层级',
  `res1` varchar(50) DEFAULT NULL COMMENT '备用字段1',
  `res2` varchar(255) DEFAULT NULL,
  `res3` varchar(255) DEFAULT NULL,
  `res4` varchar(500) DEFAULT NULL,
  `res5` text,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8mb4 COMMENT='数据源的schema解析后的属性';

-- ----------------------------
-- Records of t_ds_schema_column
-- ----------------------------
BEGIN;
INSERT INTO `t_ds_schema_column` VALUES (84, '1243485398462763008', '0', 17, 'root', 'ROW', 'Row', 0, 0, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (85, '1243485398496317440', '1243485398462763008', 17, 'dt', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (86, '1243485398496317441', '1243485398462763008', 17, 'pv', 'DECIMAL', 'Integer', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (87, '1243485398496317442', '1243485398462763008', 17, 'uv', 'DECIMAL', 'Integer', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (138, '1245234838580105216', '0', 22, 'root', 'ROW', 'Row', 0, 0, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (139, '1245234838617853952', '1245234838580105216', 22, 'user_id', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (140, '1245234838617853953', '1245234838580105216', 22, 'item_id', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (141, '1245234838617853954', '1245234838580105216', 22, 'category_id', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (142, '1245234838617853955', '1245234838580105216', 22, 'work', 'ROW', 'Row', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (143, '1245234838617853956', '1245234838617853955', 22, 'address', 'VARCHAR', 'String', 0, 0, NULL, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (144, '1245234838617853957', '1245234838617853955', 22, 'company', 'VARCHAR', 'String', 0, 0, NULL, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (145, '1245234838617853958', '1245234838580105216', 22, 'ts', 'TIMESTAMP', 'LocalDateTime', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (156, '1245291542416068608', '0', 23, 'root', 'ROW', 'Row', 0, 1, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (157, '1245291542416068609', '1245291542416068608', 23, 'user_id', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (158, '1245291542416068610', '1245291542416068608', 23, 'item_id', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (159, '1245291542416068611', '1245291542416068608', 23, 'category_id', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (160, '1245291542416068612', '1245291542416068608', 23, 'ts', 'TIMESTAMP', 'LocalDateTime', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (161, '1243474216813203456', '0', 15, 'root', 'ROW', 'Row', 0, 0, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (162, '1243474216880312320', '1243474216813203456', 15, 'user_id', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (163, '1243474216880312321', '1243474216813203456', 15, 'item_id', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (164, '1243474216880312322', '1243474216813203456', 15, 'category_id', 'VARCHAR', 'String', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_ds_schema_column` VALUES (165, '1243474216884506624', '1243474216813203456', 15, 'ts', 'TIMESTAMP', 'LocalDateTime', 0, 0, NULL, 1, NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_job
-- ----------------------------
DROP TABLE IF EXISTS `t_job`;
CREATE TABLE `t_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_code` varchar(255) DEFAULT NULL COMMENT '作业编号',
  `job_name` varchar(120) NOT NULL COMMENT '作业名称',
  `job_type` tinyint(1) DEFAULT NULL COMMENT '作业类型  0-SQL 1-JAR',
  `cluster_id` bigint(20) DEFAULT NULL COMMENT '集群ID',
  `flink_job_id` varchar(64) DEFAULT NULL COMMENT '关联的flink任务Id',
  `job_desc` varchar(500) DEFAULT NULL COMMENT '作业描述',
  `job_status` tinyint(2) DEFAULT '0' COMMENT '作业状态0初始状态、1运行中、2取消、3失败',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT '1' COMMENT '0 废弃 1 正常',
  `use_jar` varchar(255) DEFAULT NULL COMMENT '任务运行时使用的JAR',
  `parallelism` int(255) DEFAULT NULL COMMENT '并行度',
  `computing_unit` int(10) DEFAULT NULL COMMENT '计算单元个数，1个计算单元资源为1核3GB2个并发度，当前可用资源为146核168.00GB',
  `entry_class` varchar(255) DEFAULT NULL,
  `program` varchar(1000) DEFAULT NULL COMMENT '参数',
  `savepoint_path` varchar(255) DEFAULT NULL COMMENT '存储点路径',
  `allow_nrs` tinyint(1) DEFAULT '0' COMMENT 'Allow Non Restore State,允许非还原状态',
  `time_type` tinyint(1) DEFAULT NULL COMMENT '时间类型',
  `restart_strategy` tinyint(1) DEFAULT NULL COMMENT '重启策略',
  `restart_attempts` int(10) DEFAULT NULL COMMENT '尝试重启的次数',
  `delay_interval` int(10) DEFAULT NULL COMMENT '重启间隔时间(s)',
  `failure_rate` int(10) DEFAULT NULL COMMENT '一个时间段内的最大失败次数',
  `failure_interval` int(10) DEFAULT NULL COMMENT '计算失败率的时间间隔(m)',
  `checkpoint_Interval` bigint(20) DEFAULT NULL COMMENT 'Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启',
  `checkpoint_mode` tinyint(1) DEFAULT NULL,
  `checkpoint_cleanup_mode` tinyint(1) DEFAULT NULL,
  `checkpoint_timeout` bigint(20) DEFAULT NULL COMMENT '生成checkpoint的超时时间',
  `checkpoint_max_cuncurrency` int(10) DEFAULT NULL COMMENT '最大并发生成Checkpoint数',
  `min_pause_between_checkpoints` bigint(20) DEFAULT NULL COMMENT 'checkpoint最小间隔(ms)',
  `version` varchar(100) DEFAULT '0' COMMENT '版本号',
  `enable_minibatch_optimization` tinyint(1) DEFAULT '0' COMMENT '开启微型批次聚合 0-不开启 1-开启',
  `table_exec_minibatch_allowlatency` bigint(20) DEFAULT '-1' COMMENT '缓冲输入记录最大等待时间(s). 注：若开启微批次聚合，其值必须大于零',
  `table_exec_minibatch_size` bigint(20) DEFAULT '-1' COMMENT '缓冲最大输入记录数.注：若开启微批次聚合，则其值必须为正',
  `table_optimizer_agg_phase_strategy` tinyint(1) DEFAULT '0' COMMENT '启用局部本地全局聚合. 0-禁用 1-开启',
  `table_optimizer_distinct_agg_split_enabled` tinyint(1) DEFAULT '0' COMMENT '启用不同的agg分割. 0-禁用 1-开启',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='作业管理-主信息表';

-- ----------------------------
-- Records of t_job
-- ----------------------------
BEGIN;
INSERT INTO `t_job` VALUES (2, 'S202003171806439928', 'SQL作业-无嵌套-kafka-mysql测试', 0, 6, 'ca26c0398d718a7555d87ab1a50167c9', 'SQL作业测试', 1, 'ml.c', '2020-03-17 17:58:42', 'ml.c', '2020-04-01 13:56:48', 1, '', 1, 0, '', '', '', 1, 0, 0, 0, 0, 0, 0, 0, 0, NULL, 0, 0, 0, '1', 0, -1, -1, 0, 0);
INSERT INTO `t_job` VALUES (8, 'J202003231615446799', 'JAR测试', 1, 2, NULL, '11', 0, NULL, NULL, NULL, '2020-03-27 19:59:27', 1, '648227e7-908a-49ba-898a-5d5b951dd3ce_flinkdemo-1.0-SNAPSHOT.jar', 11, NULL, NULL, '11', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, -1, 0, 0);
INSERT INTO `t_job` VALUES (17, 'S202003261444143120', 'SQL作业测试', 0, 1, '', 'SQL作业测试', 3, NULL, NULL, NULL, '2020-04-03 15:53:45', 1, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', 0, -1, -1, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_job_ds
-- ----------------------------
DROP TABLE IF EXISTS `t_job_ds`;
CREATE TABLE `t_job_ds` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ds_type` varchar(10) DEFAULT NULL COMMENT '元数据类型  源表、结果表、维表、视图、自定义函数 ',
  `ds_id` bigint(20) DEFAULT NULL COMMENT '选择的对应类型ID',
  `job_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COMMENT='作业对应的数据源选择结果';

-- ----------------------------
-- Records of t_job_ds
-- ----------------------------
BEGIN;
INSERT INTO `t_job_ds` VALUES (1, 'source', 15, 2);
INSERT INTO `t_job_ds` VALUES (3, '', 0, 3);
INSERT INTO `t_job_ds` VALUES (4, '', 0, 4);
INSERT INTO `t_job_ds` VALUES (12, 'source', NULL, 17);
INSERT INTO `t_job_ds` VALUES (15, '', 0, 18);
COMMIT;

-- ----------------------------
-- Table structure for t_job_ds_sink
-- ----------------------------
DROP TABLE IF EXISTS `t_job_ds_sink`;
CREATE TABLE `t_job_ds_sink` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ds_id` bigint(20) DEFAULT NULL COMMENT '选择的对应类型ID',
  `job_id` bigint(20) DEFAULT NULL COMMENT '对应的作业ID',
  `run_sql` text COMMENT '特征加工语句',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='作业对应的数据源选择结果';

-- ----------------------------
-- Records of t_job_ds_sink
-- ----------------------------
BEGIN;
INSERT INTO `t_job_ds_sink` VALUES (1, 17, 2, 'SELECT DATE_FORMAT(ts, \'yyyy-MM-dd HH:00\') dt, COUNT(*) AS pv, COUNT(DISTINCT user_id) AS uv  FROM user_log GROUP BY DATE_FORMAT(ts, \'yyyy-MM-dd HH:00\')');
INSERT INTO `t_job_ds_sink` VALUES (9, NULL, 17, 'SELECT DATE_FORMAT(ts, \'yyyy-MM-dd HH:00\') dt, COUNT(*) AS pv, COUNT(DISTINCT user_id) AS uv  FROM user_log GROUP BY DATE_FORMAT(ts, \'yyyy-MM-dd HH:00\')');
COMMIT;

-- ----------------------------
-- Table structure for t_res
-- ----------------------------
DROP TABLE IF EXISTS `t_res`;
CREATE TABLE `t_res` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cluster_id` bigint(20) DEFAULT NULL COMMENT '集群ID',
  `res_name` varchar(255) DEFAULT NULL COMMENT '资源原始名称',
  `res_type` tinyint(1) DEFAULT '0' COMMENT '资源类型 0-jar',
  `res_uname` varchar(255) DEFAULT NULL COMMENT '资源新名称',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `entry_class` varchar(100) DEFAULT NULL,
  `res_size` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_res
-- ----------------------------
BEGIN;
INSERT INTO `t_res` VALUES (3, 1, 'flinkdemo-1.0-SNAPSHOT.jar', 0, '648227e7-908a-49ba-898a-5d5b951dd3ce_flinkdemo-1.0-SNAPSHOT.jar', NULL, '2020-02-26 11:34:17', NULL, '2020-02-26 11:34:17', NULL, 13256);
INSERT INTO `t_res` VALUES (4, NULL, 'original-quickstart-0.1.jar', 0, 'd4ff8abb-495c-42cd-bf86-0b2fae6fd489_original-quickstart-0.1.jar', NULL, '2020-03-26 11:30:50', NULL, '2020-03-26 11:30:50', NULL, 12045);
INSERT INTO `t_res` VALUES (5, NULL, 'original-quickstart-0.1.jar', 0, '4368cab8-f526-447a-bb54-8592d9006232_original-quickstart-0.1.jar', NULL, '2020-03-26 11:35:40', NULL, '2020-03-26 11:35:40', NULL, 12045);
INSERT INTO `t_res` VALUES (6, NULL, 'original-quickstart-0.1.jar', 0, 'fc7b521e-dc7b-4e5a-a6e7-725997e16e69_original-quickstart-0.1.jar', NULL, '2020-03-26 13:46:44', NULL, '2020-03-26 13:46:44', NULL, 12045);
INSERT INTO `t_res` VALUES (7, NULL, 'original-quickstart-0.1.jar', 0, '5f472709-f0a8-4bb3-85ef-74c5adfb7418_original-quickstart-0.1.jar', NULL, '2020-03-30 10:15:36', NULL, '2020-03-30 10:15:36', NULL, 12045);
INSERT INTO `t_res` VALUES (8, NULL, 'original-quickstart-0.1.jar', 0, '79c374ff-f6d3-41a1-97ac-1284efade4aa_original-quickstart-0.1.jar', NULL, '2020-03-30 10:16:12', NULL, '2020-03-30 10:16:12', NULL, 12045);
INSERT INTO `t_res` VALUES (9, NULL, 'original-quickstart-0.2.jar', 0, 'f64eb5a5-a8ff-415c-9195-5614425c8ab5_original-quickstart-0.2.jar', NULL, '2020-03-30 10:16:48', NULL, '2020-03-30 10:16:48', NULL, 12045);
COMMIT;

-- ----------------------------
-- Table structure for t_udf
-- ----------------------------
DROP TABLE IF EXISTS `t_udf`;
CREATE TABLE `t_udf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `jar_name` varchar(255) DEFAULT NULL,
  `udf_class` varchar(255) DEFAULT NULL COMMENT '加载的class',
  `udf_path` varchar(255) DEFAULT NULL COMMENT '存储路径',
  `udf_type` varchar(255) DEFAULT NULL,
  `udf_name` varchar(255) DEFAULT NULL COMMENT '函数名',
  `udf_desc` varchar(255) DEFAULT NULL COMMENT '函数描述',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_flag` int(2) DEFAULT NULL COMMENT '0:已删除 1:保存',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='自定义函数信息定义表';

-- ----------------------------
-- Records of t_udf
-- ----------------------------
BEGIN;
INSERT INTO `t_udf` VALUES (1, 'original-quickstart-0.1.jar', 'aa.bb.cc', '/Users/zhengce/Documents/springupload/70a78b02-0d46-4560-bff4-4e2e38ea9d2coriginal-quickstart-0.1.jar', 'SCALA', 'aaa', '你好，郑策测试', NULL, '2020-03-20 15:12:14', NULL, '2020-03-20 15:32:47', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
