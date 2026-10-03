<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :width="700">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from '../device.data';
  import { addDevice, editDevice, getDeviceById } from '../device.api';

  const emit = defineEmits(['success', 'register']);
  const isUpdate = ref(true);

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      const record = await getDeviceById({ id: data.record.id });
      await setFieldsValue({ ...record });
    }
  });

  const getTitle = computed(() => (!unref(isUpdate) ? '设备新增' : '设备编辑'));

  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      if (unref(isUpdate)) {
        await editDevice(values);
      } else {
        await addDevice(values);
      }
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
