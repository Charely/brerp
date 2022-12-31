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
    name: 'MaterialvendorlinkitemSubTable',
    mixins: [JeecgListMixin],
    props: {
      record: {
        type: Object,
        default: null,
      }
    },
    data() {
      return {
        description: '货源清单分录内嵌列表',
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
            title: '供应商编号',
            align: 'center',
            dataIndex: 'vendorcode',
          },
          {
            title: '供应商名称',
            align: 'center',
            dataIndex: 'vendorname',
          },
          {
            title: '最大供货数量',
            align: 'center',
            dataIndex: 'maxqty',
          },
          {
            title: '最小供货数量',
            align: 'center',
            dataIndex: 'minqty',
          },
          {
            title: '订货次数',
            align: 'center',
            dataIndex: 'orderbatch',
          },
          {
            title: '累计订货数量',
            align: 'center',
            dataIndex: 'sumorderqty',
          },
          {
            title: '累计订货金额',
            align: 'center',
            dataIndex: 'sumordervalue',
          },
          {
            title: '累计订货次数',
            align: 'center',
            dataIndex: 'sumorderbatch',
          },
          {
            title: '最近订单编号',
            align: 'center',
            dataIndex: 'lastpocode',
          },
        ],
        url: {
          listByMainId: '/po/materialvendorlink/queryMaterialvendorlinkitemByMainId',
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
