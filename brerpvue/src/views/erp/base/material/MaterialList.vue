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
      <a-button @click="handleAdd" type="primary">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('产品表')">导出</a-button>
      <a-button @click="handlestatus('0')" type="primary"> 启用</a-button>
      <a-button @click="handlestatus('1')" type="primary"> 停用</a-button>
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

    <material-modal ref="modalForm" @ok="modalFormOk"></material-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import MaterialModal from './modules/MaterialModal'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import { getAction, postAction } from '../../../../api/manage'

export default {
  name: 'MaterialList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    MaterialModal
  },
  data () {
    return {
      description: '产品表管理页面',
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
        {
          title: '物料类型',
          align: "center",
          dataIndex: 'materialtypeid_dictText'
        },
        {
          title: '编号',
          align: "center",
          dataIndex: 'materialcode'
        },
        {
          title: '物料名称',
          align: "center",
          dataIndex: 'materialname'
        },
        {
          title: '安全库存',
          align: "center",
          dataIndex: 'safestock'
        },
        {
          title: '型号',
          align: "center",
          dataIndex: 'model'
        },
        {
          title: '规格',
          align: "center",
          dataIndex: 'mspecs'
        },
        {
          title: '单位',
          align: "center",
          dataIndex: 'uom_dictText'
        },
        {
          title: '销售',
          align: "center",
          dataIndex: 'issale',
          customRender(text){
            if(text){
              if(text ==="true"){
                return '是';
              }else {
                return '否'
              }
            }
          }
        },
        {
          title: '采购',
          align: "center",
          dataIndex: 'ispo',
          customRender(text){
            if(text){
              if(text ==="true"){
                return '是';
              }else {
                return '否'
              }
            }
          }
        },
        {
          title: '库存',
          align: "center",
          dataIndex: 'isgd',
          customRender(text){
            if(text){
              if(text ==="true"){
                return '是';
              }else {
                return '否'
              }
            }
          }
        },
        {
          title: '状态',
          align: "center",
          dataIndex: 'enablestatus',
          customRender: function (t, r, index) {
            if (t == "0") {
              return "启用";
            } else {
              return "停用";
            }
          }
        },
        // {
        //   title: '删除标记',
        //   align: "center",
        //   dataIndex: 'deleteflag',
        //   customRender: function (t, r, index) {
        //     if (t == "0") {
        //       return "未删除";
        //     } else {
        //       return "已删除";
        //     }
        //   }
        // },
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
        list: "/base/material/list",
        delete: "/base/material/delete",
        deleteBatch: "/base/material/deleteBatch",
        exportXlsUrl: "/base/material/exportXls",
        importExcelUrl: "base/material/importExcel",
        updateStatus: "/base/material/updatestatus",
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
      fieldList.push({ type: 'string', value: 'materialtypeid', text: '物料类型', dictCode: "materialtype,materialtypename,id" })
      fieldList.push({ type: 'string', value: 'materialcode', text: '编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'materialname', text: '物料名称', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'safestock', text: '安全库存', dictCode: '' })
      fieldList.push({ type: 'string', value: 'model', text: '型号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'mspecs', text: '规格', dictCode: '' })
      fieldList.push({ type: 'string', value: 'uom', text: '单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'enablestatus', text: '状态', dictCode: '' })
      fieldList.push({ type: 'string', value: 'deleteflag', text: '删除标记', dictCode: '' })
      this.superFieldList = fieldList
    },
    handlestatus (status) {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！');
        return;
      } else {
        var ids = "";
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ",";
        }
        if (ids != "") {
          let p1 = { "id": ids, "status": status };
          let params = p1;
          postAction(this.url.updateStatus, params).then(res => {
            if (res.success) {
              this.$message.success("更新成功");
              this.loadData();
            }
          })
        }
      }
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>