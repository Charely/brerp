<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('批号档案')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
        @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery">
      </j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
            selectedRowKeys.length
        }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table ref="table" size="middle" :scroll="{ x: true }" bordered rowKey="id" :columns="columns"
        :dataSource="dataSource" :pagination="ipagination" :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt=""
            style="max-width:80px;font-size: 12px;font-style: italic;" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多
              <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <scmbatch-modal ref="modalForm" @ok="modalFormOk"></scmbatch-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import ScmbatchModal from './modules/ScmbatchModal'

export default {
  name: 'ScmbatchList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    ScmbatchModal
  },
  data () {
    return {
      description: '批号档案管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        // {
        //   title: '库存组织',
        //   align: "center",
        //   dataIndex: 'imorgid',
        //   width: 0,
        // },
        {
          title: '仓库',
          align: "center",
          dataIndex: 'warehouseid_dictText'
        },
        {
          title: '物料',
          align: "center",
          dataIndex: 'materialid_dictText'
        },
        {
          title: '批号',
          align: "center",
          dataIndex: 'batchcode'
        },
        {
          title: '批号数量',
          align: "center",
          dataIndex: 'batchqty'
        },
        {
          title: '原始批号',
          align: "center",
          dataIndex: 'orgbatchcode'
        },
        {
          title: '供应商',
          align: "center",
          dataIndex: 'vendorid_dictText'
        },
        {
          title: '有效期从',
          align: "center",
          dataIndex: 'fromdate',
          customRender: function (text) {
            return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '有效期止',
          align: "center",
          dataIndex: 'enddate'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: "/base/scmbatch/list",
        delete: "/base/scmbatch/delete",
        deleteBatch: "/base/scmbatch/deleteBatch",
        exportXlsUrl: "/base/scmbatch/exportXls",
        importExcelUrl: "base/scmbatch/importExcel",

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created () {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig () {
    },
    getSuperFieldList () {
      let fieldList = [];
      fieldList.push({ type: 'string', value: 'imorgid', text: '库存组织', dictCode: '' })
      fieldList.push({ type: 'string', value: 'warehouseid', text: '仓库', dictCode: '' })
      fieldList.push({ type: 'string', value: 'materialid', text: '物料', dictCode: '' })
      fieldList.push({ type: 'string', value: 'batchcode', text: '批号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'batchqty', text: '批号数量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'orgbatchcode', text: '原始批号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'vendorid', text: '供应商', dictCode: '' })
      fieldList.push({ type: 'date', value: 'fromdate', text: '有效期从' })
      fieldList.push({ type: 'string', value: 'enddate', text: '有效期止', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>