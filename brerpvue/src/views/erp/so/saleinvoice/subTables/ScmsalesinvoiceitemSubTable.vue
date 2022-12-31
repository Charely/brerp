<template>
  <a-table
    rowKey="id"
    size="middle"
    bordered
    :loading="loading"
    :columns="columns"
    :dataSource="dataSource"
    :pagination="false"
  >

    <template slot="htmlSlot" slot-scope="text">
      <div v-html="text"></div>
    </template>

    <template slot="imgSlot" slot-scope="text,record">
      <div style="font-size: 12px;font-style: italic;">
        <span v-if="!text">无图片</span>
        <img v-else :src="getImgView(text)" :preview="record.id" alt="" style="max-width:80px;height:25px;"/>
      </div>
    </template>

    <template slot="fileSlot" slot-scope="text">
      <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
      <a-button
              v-else
              ghost
              type="primary"
              icon="download"
              size="small"
              @click="downloadFile(text)"
      >
        <span>下载</span>
      </a-button>
    </template>

  </a-table>
</template>

<script>
  import { getAction } from '@api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: 'ScmsalesinvoiceitemSubTable',
    mixins: [JeecgListMixin],
    props: {
      record: {
        type: Object,
        default: null,
      }
    },
    data() {
      return {
        description: '销售发票分录内嵌列表',
        disableMixinCreated: true,
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '物料',
            align: 'center',
            dataIndex: 'materialid',
          },
          {
            title: '物料编号',
            align: 'center',
            dataIndex: 'materialcode',
          },
          {
            title: '物料名称',
            align: 'center',
            dataIndex: 'materialname',
          },
          {
            title: '销售数量',
            align: 'center',
            dataIndex: 'qty',
          },
          {
            title: '含税单价',
            align: 'center',
            dataIndex: 'taxinprice',
          },
          {
            title: '不含税单价',
            align: 'center',
            dataIndex: 'taxexprice',
          },
          {
            title: '税率',
            align: 'center',
            dataIndex: 'taxrate',
          },
          {
            title: '含税金额',
            align: 'center',
            dataIndex: 'taxinvalue',
          },
          {
            title: '不含税金额',
            align: 'center',
            dataIndex: 'taxexvalue',
          },
          {
            title: 'fromitemid',
            align: 'center',
            dataIndex: 'fromitemid',
          },
          {
            title: 'fromid',
            align: 'center',
            dataIndex: 'fromid',
          },
          {
            title: 'parentid',
            align: 'center',
            dataIndex: 'parentid',
          },
        ],
        url: {
          listByMainId: '/so/scmsaleinvoice/queryScmsalesinvoiceitemByMainId',
        },
      }
    },
    watch: {
      record: {
        immediate: true,
        handler() {
          if (this.record != null) {
            this.loadData(this.record)
          }
        }
      }
    },
    methods: {

      loadData(record) {
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
