import { BasicColumn, FormSchema } from '/@/components/Table';

/**
 * 设备列表列定义
 */
export const columns: BasicColumn[] = [
  {
    title: '设备名称',
    dataIndex: 'deviceName',
    width: 180,
    align: 'left',
  },
  {
    title: '设备编号',
    dataIndex: 'deviceCode',
    width: 160,
  },
  {
    title: '所属部门',
    dataIndex: 'departId_dictText',
    width: 160,
  },
  {
    title: '设备状态',
    dataIndex: 'status',
    width: 100,
    slots: { customRender: 'status' },
  },
  {
    title: '规格型号',
    dataIndex: 'model',
    width: 140,
  },
  {
    title: '存放位置',
    dataIndex: 'location',
    width: 140,
  },
  {
    title: '负责人',
    dataIndex: 'chargePerson',
    width: 100,
  },
  {
    title: '购置日期',
    dataIndex: 'purchaseDate',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
];

/**
 * 状态映射（与数据字典 device_status 保持一致）
 */
export const statusMap: Recordable = {
  1: { value: 1, text: '在用', color: 'green' },
  2: { value: 2, text: '闲置', color: 'default' },
  3: { value: 3, text: '维修中', color: 'orange' },
  4: { value: 4, text: '已报废', color: 'red' },
};

/**
 * 列表搜索条件
 * 说明：所有搜索参数随分页请求提交到后端 /device/deviceInfo/list 进行筛选
 */
export const searchFormSchema: FormSchema[] = [
  {
    field: 'deviceName',
    label: '设备名称',
    component: 'Input',
    componentProps: { placeholder: '请输入设备名称', allowClear: true },
    colProps: { span: 6 },
  },
  {
    field: 'deviceCode',
    label: '设备编号',
    component: 'Input',
    componentProps: { placeholder: '请输入设备编号', allowClear: true },
    colProps: { span: 6 },
  },
  {
    field: 'departId',
    label: '所属部门',
    component: 'JDictSelectTag',
    componentProps: {
      placeholder: '请选择部门',
      dictCode: 'sys_depart,depart_name,id',
      allowClear: true,
      showSearch: true,
    },
    colProps: { span: 6 },
  },
  {
    field: 'status',
    label: '设备状态',
    component: 'JDictSelectTag',
    componentProps: {
      placeholder: '请选择状态',
      dictCode: 'device_status',
      allowClear: true,
    },
    colProps: { span: 6 },
  },
  {
    field: 'createTime',
    label: '创建时间',
    component: 'RangePicker',
    componentProps: {
      valueType: 'Date',
    },
    colProps: { span: 6 },
  },
];

/**
 * 新增/编辑表单
 */
export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: '',
    component: 'Input',
    show: false,
  },
  {
    field: 'deviceName',
    label: '设备名称',
    required: true,
    component: 'Input',
    componentProps: { placeholder: '请输入设备名称' },
    colProps: { span: 24 },
  },
  {
    field: 'deviceCode',
    label: '设备编号',
    required: true,
    component: 'Input',
    componentProps: { placeholder: '请输入设备编号' },
    colProps: { span: 24 },
  },
  {
    field: 'departId',
    label: '所属部门',
    required: true,
    component: 'JDictSelectTag',
    componentProps: {
      placeholder: '请选择部门',
      dictCode: 'sys_depart,depart_name,id',
      showSearch: true,
    },
    colProps: { span: 24 },
  },
  {
    field: 'status',
    label: '设备状态',
    required: true,
    component: 'JDictSelectTag',
    defaultValue: '1',
    componentProps: {
      placeholder: '请选择状态',
      dictCode: 'device_status',
    },
    colProps: { span: 24 },
  },
  {
    field: 'model',
    label: '规格型号',
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'location',
    label: '存放位置',
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'chargePerson',
    label: '负责人',
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'purchaseDate',
    label: '购置日期',
    component: 'DatePicker',
    componentProps: { valueFormat: 'YYYY-MM-DD', style: { width: '100%' } },
    colProps: { span: 24 },
  },
  {
    field: 'remark',
    label: '备注',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },
];
