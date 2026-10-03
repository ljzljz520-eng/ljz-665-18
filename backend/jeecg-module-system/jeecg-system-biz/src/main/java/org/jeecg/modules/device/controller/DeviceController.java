package org.jeecg.modules.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.modules.device.entity.Device;
import org.jeecg.modules.device.service.IDeviceService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 设备信息
 */
@Slf4j
@Tag(name = "设备管理")
@RestController
@RequestMapping("/device/deviceInfo")
public class DeviceController extends JeecgController<Device, IDeviceService> {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private ISysDepartService sysDepartService;

    /**
     * 分页列表查询
     * 支持条件：设备名称(模糊)、设备编号(模糊)、所属部门(精确)、状态(精确)、创建时间区间(createTime_begin/createTime_end)
     * 所有过滤条件均由后端拼装 SQL 执行，在数据库层面过滤后再分页返回
     */
    @Operation(summary = "设备-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> list(Device device,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        // 设备名称、设备编号按模糊查询；部门、状态等其余字段沿用默认规则（精确匹配）
        Map<String, QueryRuleEnum> customRuleMap = new HashMap<>(2);
        customRuleMap.put("deviceName", QueryRuleEnum.LIKE);
        customRuleMap.put("deviceCode", QueryRuleEnum.LIKE);
        QueryWrapper<Device> queryWrapper = QueryGenerator.initQueryWrapper(device, req.getParameterMap(), customRuleMap);
        queryWrapper.orderByDesc("create_time");
        Page<Device> page = new Page<>(pageNo, pageSize);
        IPage<Device> pageList = deviceService.page(page, queryWrapper);
        // 填充所属部门名称（非数据库字段，仅用于列表/详情展示）
        this.fillDepartName(pageList.getRecords());
        return Result.OK(pageList);
    }

    /**
     * 按 sysOrgCode 批量回填部门名称
     */
    private void fillDepartName(List<Device> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<String> orgCodes = records.stream().map(Device::getSysOrgCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (orgCodes.isEmpty()) {
            return;
        }
        List<SysDepart> departs = sysDepartService.list(new QueryWrapper<SysDepart>().in("org_code", orgCodes));
        Map<String, String> nameMap = departs.stream().collect(Collectors.toMap(SysDepart::getOrgCode, SysDepart::getDepartName, (a, b) -> a));
        records.forEach(item -> item.setSysOrgName(nameMap.get(item.getSysOrgCode())));
    }

    /**
     * 添加
     */
    @AutoLog(value = "设备-添加")
    @Operation(summary = "设备-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Device device) {
        deviceService.save(device);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     */
    @AutoLog(value = "设备-编辑", operateType = CommonConstant.OPERATE_TYPE_3)
    @Operation(summary = "设备-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody Device device) {
        deviceService.updateById(device);
        return Result.OK("编辑成功！");
    }

    /**
     * 通过id删除
     */
    @AutoLog(value = "设备-删除")
    @Operation(summary = "设备-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        deviceService.removeById(id);
        return Result.OK("删除成功！");
    }

    /**
     * 批量删除
     */
    @AutoLog(value = "设备-批量删除")
    @Operation(summary = "设备-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.deviceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     */
    @Operation(summary = "设备-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        Device device = deviceService.getById(id);
        if (device != null && device.getSysOrgCode() != null) {
            SysDepart depart = sysDepartService.getOne(new QueryWrapper<SysDepart>().eq("org_code", device.getSysOrgCode()), false);
            if (depart != null) {
                device.setSysOrgName(depart.getDepartName());
            }
        }
        return Result.OK(device);
    }

    /**
     * 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Device device) {
        return super.exportXls(request, device, Device.class, "设备列表");
    }

}
