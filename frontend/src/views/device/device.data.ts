import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { BasicColumn, FormSchema } from '/@/components/Table';

/**
 * 设备状态选项：value 与后端 device_info.status 对应
 */
export const STATUS_OPTIONS = [
  { value: '1', label: '运行中', color: 'green' },
  { value: '2', label: '停用', color: 'red' },
  { value: '3', label: '维修中', color: 'orange' },
  { value: '0', label: '闲置', color: 'default' },
];

export function getStatusLabel(value?: string) {
  return STATUS_OPTIONS.find((item) => item.value === String(value))?.label || '';
}

export function getStatusColor(value?: string) {
  return STATUS_OPTIONS.find((item) => item.value === String(value))?.color || 'default';
}

export const columns: BasicColumn[] = [
  {
    title: '设备名称',
    dataIndex: 'deviceName',
    width: 180,
    align: 'left',
    resizable: true,
  },
  {
    title: '设备编号',
    dataIndex: 'deviceCode',
    width: 160,
    resizable: true,
  },
  {
    title: '所属部门',
    dataIndex: 'sysOrgName',
    width: 160,
    customRender: ({ record }) => record.sysOrgName || record.sysOrgCode || '',
    resizable: true,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ record }) => {
      if (!record.status && record.status !== '0') return '';
      return h(Tag, { color: getStatusColor(record.status) }, () => getStatusLabel(record.status));
    },
  },
  {
    title: '规格型号',
    dataIndex: 'model',
    width: 140,
    resizable: true,
  },
  {
    title: '安装位置',
    dataIndex: 'location',
    width: 160,
    resizable: true,
  },
  {
    title: '启用日期',
    dataIndex: 'useDate',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 170,
    sorter: { multiple: 1 },
  },
];

/**
 * 新增/编辑设备表单
 */
export const formSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '设备名称',
    field: 'deviceName',
    component: 'Input',
    required: true,
    componentProps: { placeholder: '请输入设备名称' },
  },
  {
    label: '设备编号',
    field: 'deviceCode',
    component: 'Input',
    required: true,
    componentProps: { placeholder: '请输入设备编号' },
  },
  {
    label: '所属部门',
    field: 'sysOrgCode',
    component: 'JSelectDept',
    componentProps: {
      multiple: false,
      rowKey: 'orgCode',
      sync: true,
      placeholder: '请选择所属部门',
    },
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    required: true,
    defaultValue: '0',
    componentProps: {
      placeholder: '请选择状态',
      options: STATUS_OPTIONS,
    },
  },
  {
    label: '规格型号',
    field: 'model',
    component: 'Input',
    componentProps: { placeholder: '请输入规格型号' },
  },
  {
    label: '安装位置',
    field: 'location',
    component: 'Input',
    componentProps: { placeholder: '请输入安装位置' },
  },
  {
    label: '启用日期',
    field: 'useDate',
    component: 'DatePicker',
    componentProps: { valueFormat: 'YYYY-MM-DD', style: 'width: 100%' },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    componentProps: { rows: 3, placeholder: '请输入备注' },
  },
];
