-- 设备管理表（SQL Server）
-- 字段与 org.jeecg.modules.device.entity.Device 对应
IF OBJECT_ID(N'device_info', N'U') IS NULL
BEGIN
    CREATE TABLE device_info (
        id              NVARCHAR(32)  NOT NULL,
        device_name     NVARCHAR(100) NULL,
        device_code     NVARCHAR(64)  NULL,
        sys_org_code    NVARCHAR(64)  NULL,
        status          NVARCHAR(10)  NULL,
        model           NVARCHAR(100) NULL,
        location        NVARCHAR(200) NULL,
        use_date        DATE          NULL,
        remark          NVARCHAR(500) NULL,
        create_by       NVARCHAR(50)  NULL,
        create_time     DATETIME      NULL,
        update_by       NVARCHAR(50)  NULL,
        update_time     DATETIME      NULL,
        CONSTRAINT pk_device_info PRIMARY KEY (id)
    );

    CREATE INDEX idx_device_info_name ON device_info (device_name);
    CREATE INDEX idx_device_info_code ON device_info (device_code);
    CREATE INDEX idx_device_info_org  ON device_info (sys_org_code);
    CREATE INDEX idx_device_info_ct   ON device_info (create_time);
END
GO
