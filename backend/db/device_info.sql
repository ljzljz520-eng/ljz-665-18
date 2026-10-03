-- 设备管理表 + 菜单（MySQL，与 jeecgboot-mysql-5.7.sql 同库执行）
-- SQL Server 版本见 db/sqlserver/device_info.sql 与 db/sqlserver/device_menu.sql

CREATE TABLE IF NOT EXISTS `device_info` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `device_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门编码',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 1运行中 2停用 3维修中 0闲置',
  `model` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `location` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安装位置',
  `use_date` date NULL DEFAULT NULL COMMENT '启用日期',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_device_info_name`(`device_name`) USING BTREE,
  INDEX `idx_device_info_code`(`device_code`) USING BTREE,
  INDEX `idx_device_info_org`(`sys_org_code`) USING BTREE,
  INDEX `idx_device_info_ct`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备信息表' ROW_FORMAT = DYNAMIC;

-- 菜单（后台菜单模式需要；与 db/sqlserver/device_menu.sql 对应）
INSERT INTO `sys_permission` (`id`,`parent_id`,`name`,`url`,`component`,`is_route`,`component_name`,`redirect`,`menu_type`,`perms`,`perms_type`,`sort_no`,`always_show`,`icon`,`is_leaf`,`keep_alive`,`hidden`,`hide_tab`,`description`,`create_by`,`create_time`,`del_flag`,`rule_flag`,`status`,`internal_or_external`)
SELECT '910000000000000001', NULL, '设备管理', '/device', 'layouts/default/index', 1, NULL, '/device/list', 0, NULL, '1', 15, 0, 'ant-design:hdd-outlined', 0, 0, 0, 0, '设备管理目录', 'admin', NOW(), 0, 0, '1', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_permission` WHERE id = '910000000000000001');

INSERT INTO `sys_permission` (`id`,`parent_id`,`name`,`url`,`component`,`is_route`,`component_name`,`redirect`,`menu_type`,`perms`,`perms_type`,`sort_no`,`always_show`,`icon`,`is_leaf`,`keep_alive`,`hidden`,`hide_tab`,`description`,`create_by`,`create_time`,`del_flag`,`rule_flag`,`status`,`internal_or_external`)
SELECT '910000000000000002', '910000000000000001', '设备列表', '/device/list', 'device/index', 1, 'DeviceList', NULL, 1, NULL, '1', 1, 0, NULL, 0, 1, 0, 0, '设备列表（查询条件缓存）', 'admin', NOW(), 0, 0, '1', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_permission` WHERE id = '910000000000000002');

INSERT INTO `sys_permission` (`id`,`parent_id`,`name`,`url`,`component`,`is_route`,`component_name`,`redirect`,`menu_type`,`perms`,`perms_type`,`sort_no`,`always_show`,`icon`,`is_leaf`,`keep_alive`,`hidden`,`hide_tab`,`description`,`create_by`,`create_time`,`del_flag`,`rule_flag`,`status`,`internal_or_external`)
SELECT '910000000000000003', '910000000000000001', '设备详情', '/device/detail/:id', 'device/DeviceDetail', 1, 'DeviceDetail', NULL, 1, NULL, '1', 2, 0, NULL, 1, 0, 1, 1, '设备详情（菜单隐藏）', 'admin', NOW(), 0, 0, '1', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_permission` WHERE id = '910000000000000003');
