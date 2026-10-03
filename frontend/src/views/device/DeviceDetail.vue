<template>
  <div class="p-2">
    <a-card title="设备详情">
      <template #extra>
        <a-button pre-icon="ant-design:arrow-left-outlined" @click="goBack">返回</a-button>
      </template>
      <a-descriptions v-if="detail" :column="2" bordered size="small">
        <a-descriptions-item label="设备名称">{{ detail.deviceName }}</a-descriptions-item>
        <a-descriptions-item label="设备编号">{{ detail.deviceCode }}</a-descriptions-item>
        <a-descriptions-item label="所属部门">{{ detail.sysOrgName || detail.sysOrgCode }}</a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag :color="getStatusColor(detail.status)">{{ getStatusLabel(detail.status) }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="规格型号">{{ detail.model }}</a-descriptions-item>
        <a-descriptions-item label="安装位置">{{ detail.location }}</a-descriptions-item>
        <a-descriptions-item label="启用日期">{{ detail.useDate }}</a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark }}</a-descriptions-item>
      </a-descriptions>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { getDeviceById } from './device.api';
  import { getStatusColor, getStatusLabel } from './device.data';

  defineOptions({ name: 'DeviceDetail' });

  const route = useRoute();
  const router = useRouter();
  const detail = ref<any>(null);

  async function loadDetail() {
    detail.value = await getDeviceById({ id: route.params.id as string });
  }

  /** 返回列表：列表页开启了 keepAlive，router.back() 直接恢复已缓存的条件与页码；
   *  无历史记录时（如直达详情页）回退到列表路由，并透传跳转时携带的查询参数 */
  function goBack() {
    if (window.history.state?.back) {
      router.back();
      return;
    }
    const { pageNo, deviceName, deviceCode, sysOrgCode, status, beginDate, endDate } = route.query;
    const query: Recordable = { pageNo, deviceName, deviceCode, sysOrgCode, status, beginDate, endDate };
    Object.keys(query).forEach((key) => query[key] === undefined && delete query[key]);
    router.push({
      path: '/device/list',
      query,
    });
  }

  loadDetail();
</script>
