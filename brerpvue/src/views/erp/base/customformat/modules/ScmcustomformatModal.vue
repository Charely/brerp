<template>
  <j-modal :title="title" :width="1200" :visible="visible" :maskClosable="false" switchFullscreen @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }" @cancel="handleCancel">
    <scmcustomformat-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit" :objectid="objectid" />
  </j-modal>
</template>

<script>
import ScmcustomformatForm from './ScmcustomformatForm'
export default {
  name: 'ScmcustomformatModal',
  components: {
    ScmcustomformatForm
  },
  props: {
    objectid: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      title: '',
      visible: false,
      disableSubmit: false
    }
  },
  methods: {
    add () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.add();
      })
    },
    edit (record) {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.edit(record);
      })
    },
    close () {
      this.$emit('close');
      this.visible = false;
    },
    handleOk () {
      this.$refs.realForm.handleOk();
    },
    submitCallback () {
      this.$emit('ok');
      this.visible = false;
    },
    handleCancel () {
      this.close()
    }
  }
}
</script>

<style scoped>

</style>