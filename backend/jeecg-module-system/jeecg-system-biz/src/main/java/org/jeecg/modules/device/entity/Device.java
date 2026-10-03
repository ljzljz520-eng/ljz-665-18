package org.jeecg.modules.device.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 设备信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "设备信息")
@TableName("device_info")
public class Device extends JeecgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 设备名称 */
    @Excel(name = "设备名称", width = 25)
    @Schema(description = "设备名称")
    private java.lang.String deviceName;

    /** 设备编号 */
    @Excel(name = "设备编号", width = 20)
    @Schema(description = "设备编号")
    private java.lang.String deviceCode;

    /** 所属部门编码 */
    @Excel(name = "所属部门编码", width = 20)
    @Schema(description = "所属部门编码")
    private java.lang.String sysOrgCode;

    /** 所属部门名称（非数据库字段） */
    @TableField(exist = false)
    @Schema(description = "所属部门名称")
    private java.lang.String sysOrgName;

    /** 设备状态：1-运行中 2-停用 3-维修中 0-闲置 */
    @Excel(name = "设备状态", width = 15, replace = {"运行中_1", "停用_2", "维修中_3", "闲置_0"})
    @Schema(description = "设备状态")
    private java.lang.String status;

    /** 规格型号 */
    @Excel(name = "规格型号", width = 20)
    @Schema(description = "规格型号")
    private java.lang.String model;

    /** 安装位置 */
    @Excel(name = "安装位置", width = 25)
    @Schema(description = "安装位置")
    private java.lang.String location;

    /** 启用日期 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "启用日期", width = 15, format = "yyyy-MM-dd")
    @Schema(description = "启用日期")
    private java.util.Date useDate;

    /** 备注 */
    @Excel(name = "备注", width = 30)
    @Schema(description = "备注")
    private java.lang.String remark;
}
