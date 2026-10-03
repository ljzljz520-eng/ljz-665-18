<template>
  <div class="p-2">
    <!-- 查询区域：所有条件均作为参数提交后端接口过滤 -->
    <div class="jeecg-basic-table-form-container">
      <a-form :model="queryParam" @keyup.enter="handleQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="8" :md="12" :sm="24">
            <a-form-item label="设备名称" :label-col="{ span: 7 }" :wrapper-col="{ span: 17 }">
              <a-input v-model:value="queryParam.deviceName" placeholder="请输入设备名称" allow-clear />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="8" :md="12" :sm="24">
            <a-form-item label="设备编号" :label-col="{ span: 7 }" :wrapper-col="{ span: 17 }">
              <a-input v-model:value="queryParam.deviceCode" placeholder="请输入设备编号" allow-clear />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="8" :md="12" :sm="24">
            <a-form-item label="所属部门" :label-col="{ span: 7 }" :wrapper-col="{ span: 17 }">
              <a-tree-select
                v-model:value="queryParam.sysOrgCode"
                :tree-data="deptTreeData"
                :field-names="{ label: 'title', value: 'value', children: 'children' }"
                :loading="deptLoading"
                placeholder="请选择所属部门"
                allow-clear
                tree-node-filter-prop="title"
                show-search
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="8" :md="12" :sm="24">
              <a-form-item label="状态" :label-col="{ span: 7 }" :wrapper-col="{ span: 17 }">
                <a-select v-model:value="queryParam.status" placeholder="请选择状态" allow-clear :options="STATUS_OPTIONS" />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="8" :md="12" :sm="24">
              <a-form-item label="创建日期" :label-col="{ span: 7 }" :wrapper-col="{ span: 17 }">
                <a-range-picker
                  v-model:value="dateRange"
                  value-format="YYYY-MM-DD"
                  :placeholder="['开始日期', '结束日期']"
                  style="width: 100%"
                />
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="8" :md="12" :sm="24">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" pre-icon="ant-design:search-outlined" @click="handleQuery">查询</a-button>
              <a-button style="margin-left: 8px" pre-icon="ant-design:reload-outlined" @click="handleReset">重置</a-button>
              <a style="margin-left: 8px" @click="toggleSearchStatus = !toggleSearchStatus">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <Icon :icon="toggleSearchStatus ? 'ant-design:up-outlined' : 'ant-design:down-outlined'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button type="primary" pre-icon="ant-design:plus-outlined" @click="handleAdd">新增</a-button>
        <a-button type="primary" pre-icon="ant-design:delete-outlined" :disabled="!checkedKeys.length" @click="batchHandleDelete">
          批量删除
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>

    <DeviceModal @register="registerModal" @success="reload" />
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getDeviceList, deleteDevice, batchDeleteDevice } from './device.api';
  import { columns, STATUS_OPTIONS } from './device.data';
  import DeviceModal from './components/DeviceModal.vue';
  import { queryDepartTreeSync } from '/@/api/common/api';

  // 组件名与路由 name 一致，keepAlive 才能正确缓存列表页
  defineOptions({ name: 'DeviceList' });

  const route = useRoute();
  const router = useRouter();
  const { createConfirm, createMessage } = useMessage();

  /** 查询条件初始值（重置时统一回到该状态） */
  const getDefaultQueryParam = () => ({
    deviceName: '',
    deviceCode: '',
    sysOrgCode: undefined,
    status: undefined,
  });

  // 查询条件（与后端接口参数一一对应）
  const queryParam = reactive<Recordable>(getDefaultQueryParam());
  // 日期范围（提交时拆分为 createTime_begin / createTime_end）
  const dateRange = ref<string[]>([]);
  // 展开/收起更多查询项
  const toggleSearchStatus = ref(true);
  // 当前页码（随路由一起保留，翻页后查看详情再返回可恢复）
  const currentPage = ref(Number(route.query.pageNo) || 1);

  const checkedKeys = ref<Array<string | number>>([]);
  const [registerModal, { openModal }] = useModal();

  // ==================== 部门树 ====================
  const deptTreeData = ref<any[]>([]);
  const deptLoading = ref(false);

  function transformDeptTree(nodes: any[]): any[] {
    if (!Array.isArray(nodes)) return [];
    return nodes.map((node) => ({
      title: node.departName || node.title,
      value: node.orgCode || node.id,
      key: node.orgCode || node.id,
      children: node.children?.length ? transformDeptTree(node.children) : undefined,
    }));
  }

  async function loadDeptTree() {
    deptLoading.value = true;
    try {
      const result = await queryDepartTreeSync();
      deptTreeData.value = transformDeptTree(Array.isArray(result) ? result : []);
    } finally {
      deptLoading.value = false;
    }
  }

  // ==================== 表格 ====================
  /**
   * 组装提交给后端的查询参数：
   * 名称/编号模糊、部门/状态精确、创建时间区间，均在后端过滤后再分页
   */
  function buildSearchInfo() {
    const info: Recordable = {
      deviceName: queryParam.deviceName,
      deviceCode: queryParam.deviceCode,
      sysOrgCode: queryParam.sysOrgCode,
      status: queryParam.status,
    };
    if (dateRange.value && dateRange.value.length) {
      // 只传 yyyy-MM-dd，后端 QueryGenerator 的区间规则会自动补 00:00:00 / 23:59:59
      if (dateRange.value[0]) {
        info.createTime_begin = dateRange.value[0];
      }
      if (dateRange.value[1]) {
        info.createTime_end = dateRange.value[1];
      }
    }
    // 剔除空值，避免空串作为查询条件
    Object.keys(info).forEach((key) => {
      if (info[key] === '' || info[key] === null || info[key] === undefined) {
        delete info[key];
      }
    });
    return info;
  }

  const [registerTable, { reload, getPaginationRef }] = useTable({
    title: '设备列表',
    api: getDeviceList,
    columns,
    striped: true,
    bordered: true,
    showIndexColumn: false,
    showTableSetting: true,
    rowKey: 'id',
    canResize: true,
    // 兜底：当从详情页“无历史记录返回”（如新开页/刷新后直达详情）回到列表时，
    // 条件由详情页透传到列表 URL，这里读取并恢复；正常翻页/返回流程由 keepAlive 缓存保留
    searchInfo: restoreFromRoute(),
    // 同上，恢复页码
    pagination: {
      current: currentPage.value > 1 ? currentPage.value : 1,
      pageSize: 10,
      showSizeChanger: true,
      showTotal: (total: number) => `共 ${total} 条`,
    },
    actionColumn: {
      width: 180,
      title: '操作',
      dataIndex: 'action',
      slots: { customRender: 'action' },
    },
  });

  const rowSelection = {
    type: 'checkbox',
    columnWidth: 40,
    selectedRowKeys: checkedKeys,
    onChange: (keys: (string | number)[]) => {
      checkedKeys.value = keys;
    },
  };

  function getActions(record) {
    return [
      { label: '详情', onClick: handleDetail.bind(null, record) },
      { label: '编辑', onClick: handleEdit.bind(null, record) },
      {
        label: '删除',
        popConfirm: {
          title: '确认删除该设备吗？',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }

  function handleAdd() {
    openModal(true, { isUpdate: false, showFooter: true });
  }

  function handleEdit(record) {
    openModal(true, { record, isUpdate: true, showFooter: true });
  }

  function handleDetail(record) {
    // 记录当前真实页码（翻页后表格内部页码）
    const pagination = getPaginationRef();
    if (pagination && typeof pagination !== 'boolean') {
      currentPage.value = pagination.current || 1;
    }
    // 把当前查询条件和页码带到详情页：
    // - 正常流程下列表页有 keepAlive，router.back() 直接恢复缓存；
    // - 新开详情页等无历史场景下，详情页返回时会把这些条件透传回列表 URL 进行兜底恢复
    const query = buildRouteQuery(currentPage.value);
    router.push({
      path: `/device/detail/${record.id}`,
      query,
    });
  }

  async function handleDelete(record) {
    await deleteDevice({ id: record.id }, () => {
      createMessage.success('删除成功');
      reload();
    });
  }

  function batchHandleDelete() {
    createConfirm({
      iconType: 'warning',
      title: '确认删除',
      content: `是否删除选中的 ${checkedKeys.value.length} 条数据？`,
      onOk: async () => {
        await batchDeleteDevice({ ids: checkedKeys.value.join(',') }, () => {
          createMessage.success('批量删除成功');
          checkedKeys.value = [];
          reload();
        });
      },
    });
  }

  // ==================== 查询 / 重置 ====================
  /** 查询：回到第一页并带上全部条件请求后端。
   *  状态由 keepAlive 保留，故不需要在每次查询时改 URL（避免 fullPath 变化重建组件） */
  function handleQuery() {
    currentPage.value = 1;
    reload({ page: 1, searchInfo: buildSearchInfo() });
  }

  /** 重置：一次清空全部条件（含日期范围、页码），并立即按无条件重新查询 */
  function handleReset() {
    Object.assign(queryParam, getDefaultQueryParam());
    dateRange.value = [];
    currentPage.value = 1;
    reload({ page: 1, searchInfo: {} });
    // 同时清掉详情页回退可能写入的 URL 参数
    if (Object.keys(route.query).length) {
      router.replace({ path: route.path });
    }
  }

  // ==================== 条件兜底恢复 ====================
  /** 依据当前表单 + 日期范围 + 页码生成 query 参数（用于跳详情时透传） */
  function buildRouteQuery(pageNo?: number): Recordable {
    return {
      deviceName: queryParam.deviceName || undefined,
      deviceCode: queryParam.deviceCode || undefined,
      sysOrgCode: queryParam.sysOrgCode || undefined,
      status: queryParam.status || undefined,
      beginDate: dateRange.value?.[0] || undefined,
      endDate: dateRange.value?.[1] || undefined,
      pageNo: pageNo && pageNo > 1 ? String(pageNo) : undefined,
    };
  }

  /** 从路由 query 恢复查询表单 */
  function restoreFromRoute() {
    const q = route.query || {};
    queryParam.deviceName = (q.deviceName as string) || '';
    queryParam.deviceCode = (q.deviceCode as string) || '';
    queryParam.sysOrgCode = (q.sysOrgCode as string) || undefined;
    queryParam.status = (q.status as string) || undefined;
    if (q.beginDate || q.endDate) {
      dateRange.value = [q.beginDate as string, q.endDate as string].filter(Boolean) as string[];
    }
    const info: Recordable = {
      deviceName: queryParam.deviceName,
      deviceCode: queryParam.deviceCode,
      sysOrgCode: queryParam.sysOrgCode,
      status: queryParam.status,
    };
    if (q.beginDate) {
      info.createTime_begin = q.beginDate as string;
    }
    if (q.endDate) {
      info.createTime_end = q.endDate as string;
    }
    Object.keys(info).forEach((key) => {
      if (!info[key]) delete info[key];
    });
    return Object.keys(info).length ? info : undefined;
  }

  loadDeptTree();
</script>

<style lang="less" scoped>
  .jeecg-basic-table-form-container {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 8px;
    .table-page-search-submitButtons {
      display: inline-block;
      white-space: nowrap;
    }
  }
</style>
