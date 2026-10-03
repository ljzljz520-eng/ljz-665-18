package org.jeecg.modules.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.device.entity.DeviceInfo;
import org.jeecg.modules.device.mapper.DeviceInfoMapper;
import org.jeecg.modules.device.service.IDeviceInfoService;
import org.springframework.stereotype.Service;

/**
 * @Description: 设备信息
 * @Author: jeecg-boot
 * @Date: 2026-10-03
 * @Version: V1.0
 */
@Service
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements IDeviceInfoService {

}
