package org.jeecg.modules.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.device.entity.Device;
import org.jeecg.modules.device.mapper.DeviceMapper;
import org.jeecg.modules.device.service.IDeviceService;
import org.springframework.stereotype.Service;

/**
 * @Description: 设备信息
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}
