import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/device/deviceInfo/list',
  save = '/device/deviceInfo/add',
  edit = '/device/deviceInfo/edit',
  get = '/device/deviceInfo/queryById',
  delete = '/device/deviceInfo/delete',
  deleteBatch = '/device/deviceInfo/deleteBatch',
}

/**
 * 查询设备列表（查询条件由后端过滤，返回当前页数据）
 * @param params
 */
export const getDeviceList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 保存或者更新设备
 * @param params
 */
export const saveOrUpdateDevice = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};

/**
 * 查询设备详情
 * @param params
 */
export const getDeviceById = (params) => {
  return defHttp.get({ url: Api.get, params });
};

/**
 * 删除设备
 * @param params
 */
export const deleteDevice = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 批量删除设备
 * @param params
 */
export const batchDeleteDevice = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
