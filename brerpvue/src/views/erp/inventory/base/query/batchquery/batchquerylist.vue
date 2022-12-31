<template>
    <a-card class="j-inner-table-wrapper" :bordered="false">
  
      <!-- 查询区域 begin -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
          </a-row>
        </a-form>
      </div>
      <!-- 查询区域 end -->
  
      <!-- 操作按钮区域 begin -->
      <div class="table-operator">
        <!-- 高级查询区域 -->
        <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery">
        </j-super-query>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel">
              <a-icon type="delete" />
              <span>删除</span>
            </a-menu-item>
          </a-menu>
          <a-button>
            <span>批量操作</span>
            <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </div>
      <!-- 操作按钮区域 end -->
  
      <!-- table区域 begin -->
      <div>
  
        <a-alert type="info" showIcon style="margin-bottom: 16px;">
          <template slot="message">
            <span>已选择</span>
            <a style="font-weight: 600;padding: 0 4px;">{{ selectedRowKeys.length }}</a>
            <span>项</span>
            <a style="margin-left: 24px" @click="onClearSelected">清空</a>
          </template>
        </a-alert>
  
        <a-table ref="table" size="middle" bordered rowKey="id" class="j-table-force-nowrap" :scroll="{ x: true }"
          :loading="loading" :columns="columns" :dataSource="dataSource" :pagination="ipagination"
          :expandedRowKeys="expandedRowKeys" :rowSelection="{ selectedRowKeys, onChange: onSelectChange }"
          @expand="handleExpand" @change="handleTableChange">
        </a-table>
      </div>
      <!-- table区域 end -->
  
    </a-card>
  </template>
    
  <script>
  
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  
  export default {
    name: 'balancequerylist',
    mixins: [JeecgListMixin],
    components: {
    },
    data () {
      return {
        description: '批次档案查询界面',
        // 表头
        columns: [
          {
            title: '#',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: (t, r, index) => parseInt(index) + 1
          },
          {
            title: '公司',
            align: 'center',
            dataIndex: 'companyid_dictText',
          },
          {
            title: '物料名称',
            align: 'center',
            dataIndex: 'materialid_dictText',
          },
          {
            title: '批次编号',
            align: 'center',
            dataIndex: 'batchcode',
          },
          {
            title: '批次数量',
            align: 'center',
            dataIndex: 'batchqty',
          },
          {
            title: '供应商名称',
            align: 'center',
            dataIndex: 'vendorid_dictText',
          },
          {
            title: '仓库名称',
            align: 'center',
            dataIndex: 'warehouseid_dictText',
          },
          {
            title: '生效日期',
            align: 'center',
            dataIndex: 'fromdate',
          },
          {
            title: '失效日期',
            align: 'center',
            dataIndex: 'enddate',
          },
        ],
        // 字典选项
        dictOptions: {},
        // 展开的行test
        expandedRowKeys: [],
        url: {
          list: '/base/scmbatch/list',
        },
        superFieldList: [],
      }
    },
    created () {
      var index = this.columns.findIndex(item => item.dataIndex === "action");
      if (index != -1) {
        this.columns.splice(index, 1);
      }
    },
    computed: {
    },
    methods: {
      initDictConfig () {
      },
  
      handleExpand (expanded, record) {
        this.expandedRowKeys = []
        if (expanded === true) {
          this.expandedRowKeys.push(record.id)
        }
      },
    }
  }
  </script>
  <style lang="less" scoped>
  
  </style>