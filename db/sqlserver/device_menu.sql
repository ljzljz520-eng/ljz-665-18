-- 设备管理菜单（SQL Server）
-- 后台菜单模式(BACK)下需要执行本脚本，前端静态路由仍保留以便前端菜单模式下访问
-- 注意：sys_permission 列顺序与 jeecgboot 初始化脚本保持一致

IF NOT EXISTS (SELECT 1 FROM sys_permission WHERE id = '910000000000000001')
BEGIN
    INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
    VALUES ('910000000000000001', NULL, N'设备管理', '/device', 'layouts/default/index', 1, NULL, '/device/list', 0, NULL, '1', 15.00, 0, 'ant-design:hdd-outlined', 0, 0, 0, 0, N'设备管理目录', 'admin', GETDATE(), NULL, NULL, 0, 0, '1', 0);
END
GO

IF NOT EXISTS (SELECT 1 FROM sys_permission WHERE id = '910000000000000002')
BEGIN
    INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
    VALUES ('910000000000000002', '910000000000000001', N'设备列表', '/device/list', 'device/index', 1, 'DeviceList', NULL, 1, NULL, '1', 1.00, 0, NULL, 0, 1, 0, 0, N'设备列表（查询条件缓存）', 'admin', GETDATE(), NULL, NULL, 0, 0, '1', 0);
END
GO

IF NOT EXISTS (SELECT 1 FROM sys_permission WHERE id = '910000000000000003')
BEGIN
    INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
    VALUES ('910000000000000003', '910000000000000001', N'设备详情', '/device/detail/:id', 'device/DeviceDetail', 1, 'DeviceDetail', NULL, 1, NULL, '1', 2.00, 0, NULL, 1, 0, 1, 1, N'设备详情（菜单隐藏）', 'admin', GETDATE(), NULL, NULL, 0, 0, '1', 0);
END
GO
