<template>
  <a-table rowKey="id" size="middle" bordered :loading="loading" :columns="columns" :dataSource="dataSource"
    :pagination="false">

    <template slot="htmlSlot" slot-scope="text">
      <div v-html="text"></div>
    </template>

    <template slot="imgSlot" slot-scope="text,record">
      <div style="font-size: 12px;font-style: italic;">
        <span v-if="!text">无图片</span>
        <img v-else :src="getImgView(text)" :preview="record.id" alt="" style="max-width:80px;height:25px;" />
      </div>
    </template>

    <template slot="fileSlot" slot-scope="text">
      <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
      <a-button v-else ghost type="primary" icon="download" size="small" @click="downloadFile(text)">
        <span>下载</span>
      </a-button>
    </template>

  </a-table>
</template>

<script>
import { getAction } from '@api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'ScmcustomformatitemSubTable',
  mixins: [JeecgListMixin],
  props: {
    record: {
      type: Object,
      default: null,
    }
  },
  data () {
    return {
      description: '自定义格式表分录内嵌列表',
      disableMixinCreated: true,
      loading: false,
      dataSource: [],
      columns: [
        {
          title: '控件列id',
          align: 'center',
          dataIndex: 'colid',
        },
        {
          title: '控件列名称',
          align: 'center',
          dataIndex: 'colname',
        },
        {
          title: '控件列类型',
          align: 'center',
          dataIndex: 'coltype',
        },
        {
          title: '控件列宽度',
          align: 'center',
          dataIndex: 'colwidth',
        },
        {
          title: '控件列是否可见',
          align: 'center',
          dataIndex: 'colisvisable',
        },
        {
          title: '控件列顺序',
          align: 'center',
          dataIndex: 'colorder',
        },
        {
          title: '控件列字典',
          align: 'center',
          dataIndex: 'coldictcode',
        },
      ],
      url: {
        listByMainId: '/base/scmcustomformat/queryScmcustomformatitemByMainId',
      },
    }
  },
  watch: {
    record: {
      immediate: true,
      handler () {
        if (this.record != null) {
          this.loadData(this.record)
        }
      }
    }
  },
  methods: {

    loadData (record) {
      this.loading = true
      this.dataSource = []
      getAction(this.url.listByMainId, {
        id: record.id
      }).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records
        }
      }).finally(() => {
        this.loading = false
      })
    },

  },
}
</script>

<style scoped>

</style>
