package org.jeecg.modules.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 设备信息
 * @Author: jeecg-boot
 * @Date: 2026-10-03
 * @Version: V1.0
 */
@Data
@TableName("device_info")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "设备信息表")
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private String id;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 20)
    @Schema(description = "设备名称")
    private String deviceName;

    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 20)
    @Schema(description = "设备编号")
    private String deviceCode;

    /**
     * 所属部门
     */
    @Excel(name = "所属部门", width = 20, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Schema(description = "所属部门")
    private String departId;

    /**
     * 设备状态：1-在用 2-闲置 3-维修中 4-已报废
     */
    @Excel(name = "设备状态", width = 15, dicCode = "device_status")
    @Dict(dicCode = "device_status")
    @Schema(description = "设备状态")
    private Integer status;

    /**
     * 规格型号
     */
    @Excel(name = "规格型号", width = 20)
    @Schema(description = "规格型号")
    private String model;

    /**
     * 存放位置
     */
    @Excel(name = "存放位置", width = 20)
    @Schema(description = "存放位置")
    private String location;

    /**
     * 负责人
     */
    @Excel(name = "负责人", width = 15)
    @Schema(description = "负责人")
    private String chargePerson;

    /**
     * 购置日期
     */
    @Excel(name = "购置日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "购置日期")
    private Date purchaseDate;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * 删除状态 0正常 1已删除
     */
    @TableLogic
    @Schema(description = "删除状态")
    private Integer delFlag;
}
