<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleAdd">新增</a-button>
        <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>
            批量操作
            <Icon icon="ant-design:down-outlined"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <template #status="{ record }">
        <a-tag :color="statusColorMap[record.status] || 'default'">{{ statusTextMap[record.status] || record.status }}</a-tag>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>
    <DeviceModal @register="registerModal" @success="reload" />
  </div>
</template>
<script lang="ts" name="device-list" setup>
  import { computed, onMounted } from 'vue';
  import dayjs from 'dayjs';
  import { useRouter, useRoute } from 'vue-router';
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { getDeviceList, deleteDevice, batchDeleteDevice, getExportUrl } from './device.api';
  import { columns, searchFormSchema, statusMap } from './device.data';
  import DeviceModal from './components/DeviceModal.vue';
  import { useListPage } from '/@/hooks/system/useListPage';

  const router = useRouter();
  const route = useRoute();
  const [registerModal, { openModal }] = useModal();

  // 可用于 URL 持久化的搜索字段
  const SEARCH_FIELDS = ['deviceName', 'deviceCode', 'departId', 'status', 'createTime_begin', 'createTime_end'];

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

  /**
   * 将当前搜索条件与页码同步到浏览器地址栏，
   * 进入详情后再返回（或刷新页面）时可从 URL 还原条件与页码
   */
  function syncQueryToUrl(searchInfo: Recordable, pageNo?: number) {
    const query: Recordable = { ...route.query };
    SEARCH_FIELDS.forEach((f) => delete query[f]);
    Object.keys(searchInfo || {}).forEach((k) => {
      const v = searchInfo[k];
      if (v !== undefined && v !== null && v !== '' && SEARCH_FIELDS.includes(k)) {
        query[k] = Array.isArray(v) ? v.join(',') : String(v);
      }
    });
    const currentPage = pageNo ?? route.query.pageNo;
    if (currentPage && Number(currentPage) > 1) {
      query.pageNo = String(currentPage);
    } else {
      delete query.pageNo;
    }
    router.replace({ path: route.path, query });
  }

  /**
   * 从 URL 中恢复搜索条件
   */
  function getSearchInfoFromUrl() {
    const info: Recordable = {};
    SEARCH_FIELDS.forEach((f) => {
      const v = route.query[f];
      if (v !== undefined && v !== null && v !== '') {
        info[f] = Array.isArray(v) ? v[0] : String(v);
      }
    });
    return info;
  }

  // 列表页面公共参数、方法
  const { prefixCls, onExportXls, tableContext } = useListPage({
    designScope: 'device-list',
    tableProps: {
      title: '设备列表',
      api: getDeviceList,
      columns,
      formConfig: {
        schemas: searchFormSchema,
        // RangePicker 的值在提交时转换为后端区间参数 createTime_begin / createTime_end
        fieldMapToTime: [['createTime', ['createTime_begin', 'createTime_end'], 'YYYY-MM-DD']],
      },
      actionColumn: {
        width: 180,
      },
      showIndexColumn: true,
      // 关闭默认立即加载，统一在 onMounted 中按 URL 恢复的条件与页码发起首次请求
      immediate: false,
      // 请求前：把实际提交到后端的搜索参数同步到 URL
      beforeFetch: (params) => {
        syncQueryToUrl(params, params.pageNo);
        return params;
      },
    },
    exportConfig: {
      name: '设备列表',
      url: getExportUrl,
    },
  });

  const [registerTable, { reload, getForm }, { rowSelection, selectedRowKeys }] = tableContext;

  /**
   * 页面挂载时：从 URL 还原搜索条件与页码（从详情页返回、刷新、翻页后返回均生效）
   */
  onMounted(async () => {
    const info = getSearchInfoFromUrl();
    const restorePageNo = route.query.pageNo && Number(route.query.pageNo) > 1 ? Number(route.query.pageNo) : 1;
    if (Object.keys(info).length > 0) {
      const formValues: Recordable = { ...info };
      // 将区间参数还原为 RangePicker 的 dayjs 数组
      if (info.createTime_begin || info.createTime_end) {
        formValues.createTime = [
          info.createTime_begin ? dayjs(info.createTime_begin) : null,
          info.createTime_end ? dayjs(info.createTime_end) : null,
        ];
        delete formValues.createTime_begin;
        delete formValues.createTime_end;
      }
      if (info.status !== undefined) {
        formValues.status = String(info.status);
      }
      await getForm().setFieldsValue(formValues);
    }
    // 按恢复后的条件与页码发起首次请求（无条件、无页码时即默认第一页查询）
    reload({ page: restorePageNo });
  });

  function getActions(record) {
    return [
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      },
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }

  function handleAdd() {
    openModal(true, {
      isUpdate: false,
    });
  }

  function handleEdit(record) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  /**
   * 查看详情：把当前条件与页码带过去，详情页返回时仍可还原
   */
  function handleDetail(record) {
    router.push({
      path: `/device/detail/${record.id}`,
      query: { from: route.fullPath },
    });
  }

  async function handleDelete(record) {
    await deleteDevice(record.id);
    selectedRowKeys.value = [];
    reload();
  }

  async function batchHandleDelete() {
    await batchDeleteDevice(selectedRowKeys.value.join(','));
    selectedRowKeys.value = [];
    reload();
  }
</script>
