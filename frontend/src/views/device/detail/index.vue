<template>
  <PageWrapper :title="'设备详情'" contentBackground>
    <template #extra>
      <a-button type="primary" @click="handleBack">
        <template #icon><Icon icon="ant-design:arrow-left-outlined" /></template>
        返回列表
      </a-button>
    </template>
    <a-spin :spinning="loading">
      <a-descriptions bordered :column="2" size="small">
        <a-descriptions-item label="设备名称">{{ detail.deviceName }}</a-descriptions-item>
        <a-descriptions-item label="设备编号">{{ detail.deviceCode }}</a-descriptions-item>
        <a-descriptions-item label="所属部门">{{ detail.departId_dictText }}</a-descriptions-item>
        <a-descriptions-item label="设备状态">
          <a-tag :color="statusColorMap[detail.status] || 'default'">
            {{ statusTextMap[detail.status] || detail.status }}
          </a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="规格型号">{{ detail.model }}</a-descriptions-item>
        <a-descriptions-item label="存放位置">{{ detail.location }}</a-descriptions-item>
        <a-descriptions-item label="负责人">{{ detail.chargePerson }}</a-descriptions-item>
        <a-descriptions-item label="购置日期">{{ detail.purchaseDate }}</a-descriptions-item>
        <a-descriptions-item label="创建人">{{ detail.createBy }}</a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark }}</a-descriptions-item>
      </a-descriptions>
    </a-spin>
  </PageWrapper>
</template>
<script lang="ts" name="device-detail" setup>
  import { ref, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { PageWrapper } from '/@/components/Page';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getDeviceById } from '../device.api';
  import { statusMap } from '../device.data';

  const route = useRoute();
  const router = useRouter();
  const { createMessage } = useMessage();

  const loading = ref(false);
  const detail = ref<Recordable>({});

  const statusTextMap = computed(() => {
    const m = {};
    Object.keys(statusMap).forEach((k) => (m[k] = statusMap[k].text));
    return m;
  });
  const statusColorMap = computed(() => {
    const m = {};
    Object.keys(statusMap).forEach((k) => (m[k] = statusMap[k].color));
    return m;
  });

  async function loadDetail() {
    const id = route.params.id as string;
    if (!id) {
      createMessage.warning('缺少设备id');
      return;
    }
    loading.value = true;
    try {
      detail.value = await getDeviceById({ id });
    } finally {
      loading.value = false;
    }
  }

  /**
   * 返回列表：优先回到列表页携带原始搜索条件的地址（在列表页 query 中保存）
   */
  function handleBack() {
    const from = route.query.from as string;
    if (from) {
      router.push(from);
    } else {
      router.back();
    }
  }

  loadDetail();
</script>
