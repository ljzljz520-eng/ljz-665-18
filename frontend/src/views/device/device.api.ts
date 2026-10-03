import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/device/deviceInfo/list',
  queryById = '/device/deviceInfo/queryById',
  save = '/device/deviceInfo/add',
  edit = '/device/deviceInfo/edit',
  remove = '/device/deviceInfo/delete',
  deleteBatch = '/device/deviceInfo/deleteBatch',
  exportXls = '/device/deviceInfo/exportXls',
}

/**
 * 设备分页列表（查询条件全部走后端接口筛选）
 * @param params
 */
export const getDeviceList = (params?) => defHttp.get({ url: Api.list, params });

/**
 * 设备详情
 * @param params
 */
export const getDeviceById = (params) => defHttp.get({ url: Api.queryById, params });

/**
 * 新增设备
 * @param params
 */
export const addDevice = (params?) => defHttp.post({ url: Api.save, params });

/**
 * 编辑设备
 * @param params
 */
export const editDevice = (params?) => defHttp.put({ url: Api.edit, params });

/**
 * 删除设备
 * @param id
 */
export const deleteDevice = (id) => defHttp.delete({ url: Api.remove, data: { id } }, { joinParamsToUrl: true });

/**
 * 批量删除
 * @param ids
 */
export const batchDeleteDevice = (ids) => defHttp.delete({ url: Api.deleteBatch, data: { ids } }, { joinParamsToUrl: true });

/**
 * 导出地址
 */
export const getExportUrl = Api.exportXls;
