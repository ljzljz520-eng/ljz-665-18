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
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.device.entity.DeviceInfo;
import org.jeecg.modules.device.service.IDeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 设备信息
 * @Author: jeecg-boot
 * @Date: 2026-10-03
 * @Version: V1.0
 */
@Slf4j
@Tag(name = "设备信息")
@RestController
@RequestMapping("/device/deviceInfo")
public class DeviceInfoController {

    @Autowired
    private IDeviceInfoService deviceInfoService;

    /**
     * 分页列表查询
     * 支持按设备名称、设备编号、所属部门、设备状态、创建时间范围进行后端筛选
     *
     * @param deviceInfo 查询条件（deviceName / deviceCode / departId / status）
     * @param pageNo     页码
     * @param pageSize   每页条数
     * @param req        请求参数，支持 createTime_begin、createTime_end 时间区间参数
     */
    @AutoLog(value = "设备信息-分页列表查询")
    @Operation(summary = "设备信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<DeviceInfo>> queryPageList(DeviceInfo deviceInfo,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   HttpServletRequest req) {
        // 设备名称、设备编号采用模糊匹配，先取出再交给QueryGenerator处理其余条件（部门、状态、时间区间等）
        String deviceName = deviceInfo.getDeviceName();
        String deviceCode = deviceInfo.getDeviceCode();
        deviceInfo.setDeviceName(null);
        deviceInfo.setDeviceCode(null);
        QueryWrapper<DeviceInfo> queryWrapper = QueryGenerator.initQueryWrapper(deviceInfo, req.getParameterMap());
        if (oConvertUtils.isNotEmpty(deviceName)) {
            queryWrapper.like("device_name", deviceName.trim());
        }
        if (oConvertUtils.isNotEmpty(deviceCode)) {
            queryWrapper.like("device_code", deviceCode.trim());
        }
        queryWrapper.orderByDesc("create_time");
        Page<DeviceInfo> page = new Page<>(pageNo, pageSize);
        IPage<DeviceInfo> pageList = deviceInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 通过id查询详情
     *
     * @param id 主键id
     */
    @AutoLog(value = "设备信息-详情查询")
    @Operation(summary = "设备信息-详情查询")
    @GetMapping(value = "/queryById")
    public Result<DeviceInfo> queryById(@RequestParam(name = "id") String id) {
        DeviceInfo deviceInfo = deviceInfoService.getById(id);
        if (deviceInfo == null) {
            return Result.error("未找到对应设备数据");
        }
        return Result.OK(deviceInfo);
    }

    /**
     * 添加
     *
     * @param deviceInfo 设备信息
     */
    @AutoLog(value = "设备信息-添加")
    @Operation(summary = "设备信息-添加")
    @PostMapping(value = "/add")
    public Result<DeviceInfo> add(@RequestBody DeviceInfo deviceInfo) {
        deviceInfo.setDelFlag(0);
        deviceInfoService.save(deviceInfo);
        return Result.OK("添加成功！", deviceInfo);
    }

    /**
     * 编辑
     *
     * @param deviceInfo 设备信息
     */
    @AutoLog(value = "设备信息-编辑")
    @Operation(summary = "设备信息-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<DeviceInfo> edit(@RequestBody DeviceInfo deviceInfo) {
        deviceInfoService.updateById(deviceInfo);
        return Result.OK("编辑成功！", deviceInfo);
    }

    /**
     * 通过id删除
     *
     * @param id 主键id
     */
    @AutoLog(value = "设备信息-删除")
    @Operation(summary = "设备信息-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        deviceInfoService.removeById(id);
        return Result.OK("删除成功！");
    }

    /**
     * 批量删除
     *
     * @param ids 主键id，多个用逗号分隔
     */
    @AutoLog(value = "设备信息-批量删除")
    @Operation(summary = "设备信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.deviceInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 导出excel
     *
     * @param deviceInfo 查询条件
     * @param req        请求参数
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response, DeviceInfo deviceInfo) {
        String deviceName = deviceInfo.getDeviceName();
        String deviceCode = deviceInfo.getDeviceCode();
        deviceInfo.setDeviceName(null);
        deviceInfo.setDeviceCode(null);
        QueryWrapper<DeviceInfo> queryWrapper = QueryGenerator.initQueryWrapper(deviceInfo, request.getParameterMap());
        if (oConvertUtils.isNotEmpty(deviceName)) {
            queryWrapper.like("device_name", deviceName.trim());
        }
        if (oConvertUtils.isNotEmpty(deviceCode)) {
            queryWrapper.like("device_code", deviceCode.trim());
        }
        queryWrapper.orderByDesc("create_time");
        List<DeviceInfo> list = deviceInfoService.list(queryWrapper);
        ModelAndView mv = new ModelAndView(new org.jeecgframework.poi.excel.view.JeecgEntityExcelView());
        mv.addObject(org.jeecgframework.poi.excel.def.NormalExcelConstants.FILE_NAME, "设备信息");
        mv.addObject(org.jeecgframework.poi.excel.def.NormalExcelConstants.CLASS, DeviceInfo.class);
        mv.addObject(org.jeecgframework.poi.excel.def.NormalExcelConstants.DATA_LIST, list);
        return mv;
    }
}
