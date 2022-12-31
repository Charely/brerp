<template>
    <j-vxe-table
      ref="vTable"
      toolbar
      row-number
      row-selection
      keep-source
      async-remove
      :height="484"
      :loading="loading"
      :dataSource="dataSource"
      :columns="columns"
      :pagination="pagination"
      style="margin-top: 8px;"
      @pageChange="handlePageChange"
      @save="handleTableSave"
      @remove="handleTableRemove"
      @edit-closed="handleEditClosed"
      @selectRowChange="handleSelectRowChange"
    >
  
      <template v-slot:toolbarSuffix>
        <a-button @click="handleTableGet">获取值</a-button>
      </template>
  
    </j-vxe-table>
  </template>
  
  /**供应链价格管理功能 */
  <script>
    import moment from 'moment'
    import { randomNumber, randomUUID } from '@/utils/util'
    import { JVXETypes } from '@/components/jeecg/JVxeTable'
import { getAction } from '../../../../api/manage'
  
    export default {
      name: 'pricemg',
      data() {
        return {
          loading: false,
          columns: [
            {
              title: '物料编号',
              key: 'materialid',
              type: JVXETypes.materialSelect,
              width: '100px',
            },
            {
              title: '供应商',
              key: 'vendorid',
              type: JVXETypes.selectDictSearch,
              width: '180px',
              dict:"scmpartner where partnertype like '%0%',partnername,id",
            },
            {
              title: '客户',
              key: 'customerid',
              type: JVXETypes.selectDictSearch,
              width: '100px',
              dict:"scmpartner where partnertype like '%1%',partnername,id",
            },
            {
              title: '采购价',
              key: 'poprice',
              type: JVXETypes.inputNumber,
              width: '80px',
            },
            {
              title: '销售价',
              key: 'soprice',
              type: JVXETypes.inputNumber,
              width: '80px',
            },
            {
              title: '成本价',
              key: 'planprice',
              type: JVXETypes.inputNumber,
              width: '80px',
            },
          ],
          dataSource: [],
          url:{
            list:'/base/scmpricemg/list',
            add:'/base/scmpricemg/add',
          },
          pagination: {
            current: 1,
            pageSize: 10,
            pageSizeOptions: ['10', '20', '30'],
            total:0,
          },
        }
  
      },
  
      created() {
        
      },
      methods: {
  
        // 当分页参数变化时触发的事件
        handlePageChange(event) {
          // 重新赋值
          this.pagination.current = event.current
          this.pagination.pageSize = event.pageSize
          getPriceDatalist(event.current,event.pageSize,true);
          // 查询数据
       //   this.randomPage(event.current, event.pageSize, true)
        },

        getPriceDatalist(current,pageSize,loading=false){
            if(loading){
                this.loading = true
            }
            let params={
                pageNo:current,
                pageSize:pageSize
            };
            getAction(this.url.list,params).then(res=>{
                if(res.success){
                    this.dataSource=res.result.records||res.result;
                }
            })
        },
  
        /** 获取值，忽略表单验证 */
        handleTableGet() {
          const values = this.$refs.vTable.getTableData()
          console.log('获取值:', {values})
          this.$message.success('获取值成功，请看控制台输出')
        },
        handleEditClosed(event) {
        let {$table, row, column} = event

        let field = column.property
        let cellValue = row[field]
        // 判断单元格值是否被修改
        if ($table.isUpdateByRow(row, field)) {
          // 校验当前行
          $table.validate(row).then((errMap) => {
            // 校验通过
            if (!errMap) {
              // 【模拟保存】
              let hideLoading = this.$message.loading(`正在保存"${column.title}"`, 0)
              console.log('即时保存数据：', row)
              putAction(this.url.add, row).then(res => {
                if (res.success) {
                  this.$message.success(`"${column.title}"保存成功！`)
                  // 局部更新单元格为已保存状态
                  $table.reloadRow(row, null, field)
                } else {
                  this.$message.warn(`"${column.title}"保存失败：` + res.message)
                }
              }).finally(() => {
                hideLoading()
              })
            }
          })
        }
      }
      }
    }
  </script>
  
  <style scoped>
  
  </style>