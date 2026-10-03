-- =====================================================================
-- 设备管理模块增量脚本（SQL Server）
-- 适用：数据库已初始化，需要增量构建设备管理功能的环境
-- 全新部署无需执行本脚本（jeecgboot-mysql-5.7.sql 已包含等价内容）
-- =====================================================================

IF OBJECT_ID(N'[device_info]', N'U') IS NULL
BEGIN
    CREATE TABLE [device_info] (
      [id] nvarchar(32) NOT NULL,
      [device_name] nvarchar(100) NOT NULL,
      [device_code] nvarchar(50) NOT NULL,
      [depart_id] nvarchar(32) NULL,
      [status] int NULL DEFAULT 1,
      [model] nvarchar(100) NULL,
      [location] nvarchar(200) NULL,
      [charge_person] nvarchar(50) NULL,
      [purchase_date] date NULL,
      [remark] nvarchar(500) NULL,
      [create_by] nvarchar(50) NULL,
      [create_time] datetime2 NULL,
      [update_by] nvarchar(50) NULL,
      [update_time] datetime2 NULL,
      [del_flag] int NULL DEFAULT 0,
      PRIMARY KEY ([id])
    );
    CREATE INDEX [idx_device_name] ON [device_info] ([device_name]);
    CREATE INDEX [idx_device_code] ON [device_info] ([device_code]);
    CREATE INDEX [idx_device_depart] ON [device_info] ([depart_id]);
    CREATE INDEX [idx_device_status] ON [device_info] ([status]);
    CREATE INDEX [idx_device_create_time] ON [device_info] ([create_time]);
END
GO

IF NOT EXISTS (SELECT 1 FROM [sys_dict] WHERE [dict_code] = N'device_status')
BEGIN
    INSERT INTO [sys_dict] ([id], [dict_name], [dict_code], [description], [del_flag], [create_by], [create_time], [type], [tenant_id])
    VALUES ('devdict000000000000000000000001', N'设备状态', N'device_status', N'设备状态：1在用 2闲置 3维修中 4已报废', 0, N'admin', '2026-10-03 10:00:00', 1, 0);
END
GO
IF NOT EXISTS (SELECT 1 FROM [sys_dict_item] WHERE [id] = 'devdictitem0000000000000000001')
    INSERT INTO [sys_dict_item] ([id],[dict_id],[item_text],[item_value],[item_color],[description],[sort_order],[status],[create_by],[create_time])
    VALUES ('devdictitem0000000000000000001','devdict000000000000000000000001',N'在用','1','green',N'在用',1,1,N'admin','2026-10-03 10:00:00');
IF NOT EXISTS (SELECT 1 FROM [sys_dict_item] WHERE [id] = 'devdictitem0000000000000000002')
    INSERT INTO [sys_dict_item] ([id],[dict_id],[item_text],[item_value],[item_color],[description],[sort_order],[status],[create_by],[create_time])
    VALUES ('devdictitem0000000000000000002','devdict000000000000000000000001',N'闲置','2','default',N'闲置',2,1,N'admin','2026-10-03 10:00:00');
IF NOT EXISTS (SELECT 1 FROM [sys_dict_item] WHERE [id] = 'devdictitem0000000000000000003')
    INSERT INTO [sys_dict_item] ([id],[dict_id],[item_text],[item_value],[item_color],[description],[sort_order],[status],[create_by],[create_time])
    VALUES ('devdictitem0000000000000000003','devdict000000000000000000000001',N'维修中','3','orange',N'维修中',3,1,N'admin','2026-10-03 10:00:00');
IF NOT EXISTS (SELECT 1 FROM [sys_dict_item] WHERE [id] = 'devdictitem0000000000000000004')
    INSERT INTO [sys_dict_item] ([id],[dict_id],[item_text],[item_value],[item_color],[description],[sort_order],[status],[create_by],[create_time])
    VALUES ('devdictitem0000000000000000004','devdict000000000000000000000001',N'已报废','4','red',N'已报废',4,1,N'admin','2026-10-03 10:00:00');
GO

IF NOT EXISTS (SELECT 1 FROM [sys_permission] WHERE [id] = 'devmenu0000000000000000000000001')
    INSERT INTO [sys_permission] ([id],[parent_id],[name],[url],[component],[is_route],[component_name],[redirect],[menu_type],[perms],[perms_type],[sort_no],[always_show],[icon],[is_leaf],[keep_alive],[hidden],[hide_tab],[description],[create_by],[create_time],[del_flag],[rule_flag],[status],[internal_or_external])
    VALUES ('devmenu0000000000000000000000001',NULL,N'设备管理',N'/device',NULL,1,NULL,NULL,0,NULL,'1',5.00,1,'ant-design:hdd-outlined',0,0,0,0,NULL,N'admin','2026-10-03 10:00:00',0,0,'1',0);
IF NOT EXISTS (SELECT 1 FROM [sys_permission] WHERE [id] = 'devmenu0000000000000000000000002')
    INSERT INTO [sys_permission] ([id],[parent_id],[name],[url],[component],[is_route],[component_name],[redirect],[menu_type],[perms],[perms_type],[sort_no],[always_show],[icon],[is_leaf],[keep_alive],[hidden],[hide_tab],[description],[create_by],[create_time],[del_flag],[rule_flag],[status],[internal_or_external])
    VALUES ('devmenu0000000000000000000000002','devmenu0000000000000000000000001',N'设备列表',N'/device/list','device/index',1,'device-list',NULL,1,NULL,'1',1.00,0,'ant-design:unordered-list-outlined',1,1,0,0,NULL,N'admin','2026-10-03 10:00:00',0,0,'1',0);
IF NOT EXISTS (SELECT 1 FROM [sys_permission] WHERE [id] = 'devmenu0000000000000000000000003')
    INSERT INTO [sys_permission] ([id],[parent_id],[name],[url],[component],[is_route],[component_name],[redirect],[menu_type],[perms],[perms_type],[sort_no],[always_show],[icon],[is_leaf],[keep_alive],[hidden],[hide_tab],[description],[create_by],[create_time],[del_flag],[rule_flag],[status],[internal_or_external])
    VALUES ('devmenu0000000000000000000000003','devmenu0000000000000000000000001',N'设备详情',N'/device/detail/:id','device/detail/index',1,'device-detail',NULL,1,NULL,'1',2.00,0,NULL,1,0,1,1,NULL,N'admin','2026-10-03 10:00:00',0,0,'1',0);
GO

IF NOT EXISTS (SELECT 1 FROM [sys_role_permission] WHERE [id] = 'devroleperm000000000000000000001')
    INSERT INTO [sys_role_permission] ([id],[role_id],[permission_id]) VALUES ('devroleperm000000000000000000001','f6817f48af4fb3af11b9e8bf182f618b','devmenu0000000000000000000000001');
IF NOT EXISTS (SELECT 1 FROM [sys_role_permission] WHERE [id] = 'devroleperm000000000000000000002')
    INSERT INTO [sys_role_permission] ([id],[role_id],[permission_id]) VALUES ('devroleperm000000000000000000002','f6817f48af4fb3af11b9e8bf182f618b','devmenu0000000000000000000000002');
IF NOT EXISTS (SELECT 1 FROM [sys_role_permission] WHERE [id] = 'devroleperm000000000000000000003')
    INSERT INTO [sys_role_permission] ([id],[role_id],[permission_id]) VALUES ('devroleperm000000000000000000003','f6817f48af4fb3af11b9e8bf182f618b','devmenu0000000000000000000000003');
GO
