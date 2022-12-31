<template>
  <a-card class="j-inner-table-wrapper" :bordered="false">

    <!-- 查询区域 begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">

            <a-form-item label="公司">
              <j-search-select-tag v-model="queryParam.companyid" placeholder="请选择公司"
                dict="sys_depart where org_type='1',depart_name,id"></j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">

            <a-form-item label="物料编号">
              <c-select-material v-model="queryParam.materialid" placeholder="请选择物料" :buttons="false"></c-select-material>
            </a-form-item>
            </a-col>
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </a-col>
          </span>
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
import CSelectMaterial from '../../../../base/custom/CSelectMaterial.vue';

export default {
  name: 'balancequerylist',
  mixins: [JeecgListMixin],
  components: {
    CSelectMaterial
  },
  data () {
    return {
      description: '库存账本查询界面',
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
          align: 'left',
          dataIndex: 'companyname',
        },
        {
          title: '期间',
          align: 'left',
          dataIndex: 'balancedate',
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
          title: '批次编号',
          align: 'center',
          dataIndex: 'batchcode',
        },
        {
          title: '供应商名称',
          align: 'center',
          dataIndex: 'vendorname',
        },
        {
          title: '仓库编号',
          align: 'center',
          dataIndex: 'warehousecode',
        },
        {
          title: '仓库名称',
          align: 'center',
          dataIndex: 'warehousename',
        },
        {
          title:'货位',
          align:"center",
          dataIndex:'stocklocationname',
        },
        {
          title: '库存状态',
          align: 'center',
          dataIndex: 'stockname',
        },
        {
          title: '库存类型',
          align: 'center',
          dataIndex: 'kindname',
        },
        {
          title: '年初数量',
          align: 'center',
          dataIndex: 'beginyearqty',
        },
        {
          title: '月初数量',
          align: 'center',
          dataIndex: 'beginmonthqty',
        },
        {
          title: '当月入库数量',
          align: 'center',
          dataIndex: 'inamount',
        },
        {
          title: '当月出库数量',
          align: 'center',
          dataIndex: 'outamount',
        },
        {
          title: '库存余额',
          align: 'center',
          dataIndex: 'amount',
          // customRender:(text,record,index)=>{
          //     return record['beginyearqty']+ record['beginmonthqty']+ record['inamount'] - record['outamount'] - record['endmonthqty']- record['endyearqty']
          // }
        }
      ],
      // 字典选项
      dictOptions: {},
      // 展开的行test
      expandedRowKeys: [],
      url: {
        list: '/inventory.base/scmimbalance/getbalanceamount',
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