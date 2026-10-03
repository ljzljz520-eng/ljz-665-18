import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';

const device: AppRouteModule = {
  path: '/device',
  name: 'Device',
  component: LAYOUT,
  redirect: '/device/list',
  meta: {
    orderNo: 1500,
    icon: 'ant-design:hdd-outlined',
    title: '设备管理',
  },
  children: [
    {
      path: 'list',
      name: 'DeviceList',
      meta: {
        title: '设备列表',
        // 关键：缓存列表页，翻页/查看详情再返回时保留搜索条件
        ignoreKeepAlive: false,
      },
      component: () => import('/@/views/device/index.vue'),
    },
    {
      path: 'detail/:id',
      name: 'DeviceDetail',
      meta: {
        title: '设备详情',
        hideMenu: true,
        ignoreKeepAlive: true,
        currentActiveMenu: '/device/list',
      },
      component: () => import('/@/views/device/DeviceDetail.vue'),
    },
  ],
};

export default device;
