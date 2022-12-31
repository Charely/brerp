import { queryMaterialById,queryMaterialVoInfoById,getscmbatchbycompanyidandmaterialid }  from '@/api/erp/commonapi'
import { setStore, getStore, clearStore } from "@/utils/storage"
import { Row } from 'ant-design-vue'


export function handleValueChange( event ){
    const { type, row, column, value, target } = event
      if (column.key === 'qty') {
        let currentPrice = row['price'] || row['taxinprice']
        target.setValues([{
          rowKey: row.id,
          values: { 
            'taxinvalue': (value * currentPrice).toFixed(2) ,
            'taxexvalue': (value * currentPrice * (100 - row['taxrate']) / 100 ).toFixed(2)
          }
        }])
      } else if (column.key === 'price') {
        let currentQty = row['qty'] || row['poqty']
        target.setValues([{
          rowKey: row.id,
          values: { 'taxinvalue': (value * currentQty).toFixed(2) }
        }])
      } else if (column.key === 'materialid'){
        //获取物料后自动赋值计量单位列
        let params={id:value}
        queryMaterialById(params).then(res=>{
            if(res.success){
                let material=res.result;
                if(row.hasOwnProperty('uomid')){
                  target.setValues([{
                      rowKey:row.id,
                      values:{'uomid':material.uom}
                  }]);
                }
                if(row.hasOwnProperty('taxrate')){
                  target.setValues([{
                    rowKey:row.id,
                    values:{'taxrate':material.taxrate}
                  }])
                }
                target.setValues([{
                  rowKey:row.id,
                  values:{'materialcode':material.materialcode}
                }])
            }
        })
      } else if(column.key === 'taxinprice'){
        let newQty=row['qty']
        let newRate=row['taxrate']
        target.setValues([{
          rowKey:row.id,
          values:{'taxinvalue':(value * newQty).toFixed(2),
                  'taxexvalue':(value * newQty * (100-newRate) /100).toFixed(2),
                  'taxexprice':(value * (100 - newRate) / 100).toFixed(2)
                }
        }])
      } else if(column.key==='taxexprice'){
        target.setValues([{
          rowKey:row.id,
          values:{
              'taxinvalue':(value * 100 * row['qty'] / (100 - row['taxrate'])).toFixed(2),
              'taxinprice':(value * 100 /(100 - row['taxrate'])).toFixed(2),
              'taxexvalue':(value * row['qty']).toFixed(2)
          }
        }])
      } else if(column.key === 'taxrate'){
        target.setValues([{
          rowKey:row.id,
          values:{
            'taxexprice':(row['taxinprice'] * (100 - value)/100).toFixed(2),
            'taxexvalue':(row['taxinprice'] * row['qty'] * (100 -value)/100).toFixed(2)
          }
        }])
      }
}


export function handleJvxeValueChange( event ){
  const { type, row, column, value,oldValue, target,cellTarget,columnIndex,index,isSetValues } = event
    if (column.key === 'qty') {
      let curValue=value;
      if(isSetValues){
        return;
      }
      if(oldValue <0 ){
        if(value >0 ){
          curValue = value*(-1)
          target.setValues([{
            rowKey:row.id,
            values:{
              'qty':curValue
            }
          }])
        }
      }
      let currentPrice = 0
      if(row.hasOwnProperty("price")){
        currentPrice=row.price;
      } else if(row.hasOwnProperty("taxinprice")){
        currentPrice=row.taxinprice;
      }
      if(row.hasOwnProperty("taxinvalue")){
        target.setValues([{
          rowKey: row.id,
          values: { 
            'taxinvalue': (curValue * currentPrice).toFixed(2) 
          }
        }])
      }
      if(row.hasOwnProperty('taxexvalue')){
        target.setValues([{
          rowKey: row.id,
          values: { 
            'taxexvalue': (curValue * currentPrice * (100 - row['taxrate']) / 100 ).toFixed(2)
          }
        }])
      }
    } else if (column.key === 'price') {
      let currentQty = row['qty'] || row['poqty']
      target.setValues([{
        rowKey: row.id,
        values: { 'taxinvalue': (value * currentQty).toFixed(2) }
      }])
    } else if (column.key === 'materialid'){
      //获取物料后自动赋值计量单位列
      let params={id:value}

      queryMaterialById(params).then( res=>{
          if(res.success){
              let material=res.result;
              //获取到物料信息后，查看是否管理批次
             

              if(row.hasOwnProperty('uomid')){
                target.setValues([{
                    rowKey:row.id,
                    values:{'uomid':material.uom}
                }]);
              }
              if(row.hasOwnProperty('taxrate')){
                target.setValues([{
                  rowKey:row.id,
                  values:{'taxrate':material.taxrate}
                }])
              }
              target.setValues([{
                rowKey:row.id,
                values:{'materialcode':material.materialcode}
              }])
              if(row.hasOwnProperty('materialname')){
                target.setValues([{
                  rowKey:row.id,
                  values:{'materialname':material.materialname}
                }])
              }
              if(row.hasOwnProperty('stocktypeid')){
                target.setValues([{
                  rowKey:row.id,
                  values:{'stocktypeid':'S01'}
                }])
              }
              if(row.hasOwnProperty("inventorykindid")){
                target.setValues([{
                  rowKey:row.id,
                  values:{'inventorykindid':'01'}
                }])
              }
              if(cellTarget.cellProps.hasOwnProperty("isout")){
                let currentuser = this.$store.getters.userInfo
                let companyid=getStore(currentuser.id+"_outstock_companyid")
                if(companyid === 'undefind'){
                  return
                }
                let warehouseid=localStorage.getItem(currentuser.id+"_outstock_warehouseid") 
                if(warehouseid === 'undefind'){
                  return
                }
                getscmbatchbycompanyidandmaterialid({companyid:companyid,
                  warehouseid:warehouseid,materialid:value}).then(res=>{
                  if(res.success){
                    let scmbatch=res.result;
                    if(scmbatch!=null && scmbatch!= undefined){
                      if(row.hasOwnProperty("batchid")){
                        target.setValues([{
                          rowKey:row.id,
                          values:{'batchid':scmbatch.id}
                        }])
                      }
                    }
                  }
              })
            }
          }
      })
    } else if(column.key === 'taxinprice'){
      let newQty=row['qty']
      let newRate=row['taxrate']
      target.setValues([{
        rowKey:row.id,
        values:{'taxinvalue':(value * newQty).toFixed(2),
                'taxexvalue':(value * newQty * (100-newRate) /100).toFixed(2),
                'taxexprice':(value * (100 - newRate) / 100).toFixed(2)
              }
      }])
    } else if(column.key==='taxexprice'){
      target.setValues([{
        rowKey:row.id,
        values:{
            'taxinvalue':(value * 100 * row['qty'] / (100 - row['taxrate'])).toFixed(2),
            'taxinprice':(value * 100 /(100 - row['taxrate'])).toFixed(2),
            'taxexvalue':(value * row['qty']).toFixed(2)
        }
      }])
    } else if(column.key === 'taxrate'){
      target.setValues([{
        rowKey:row.id,
        values:{
          'taxexprice':(row['taxinprice'] * (100 - value)/100).toFixed(2),
          'taxexvalue':(row['taxinprice'] * row['qty'] * (100 -value)/100).toFixed(2)
        }
      }])
    } else if(column.key === 'vendorbatchcode'){
      //供应商批次信息
      //TODO 如果录入供应商批次，需要判断是否进行批次控制
      //获取物料信息
      let curmaterialid=row.materialid;
      if(curmaterialid=== null || curmaterialid === '' || curmaterialid === undefined){
        target.$message.error("请先选择物料");
        target.setValues([{
          rowKey:row.id,
          values:{
            "vendorbatchcode":oldValue
          }
        }])
        return;
      }
      if(row.hasOwnProperty("batchid")){
        let curbatchid=row.batchid;
        if(curbatchid!= null && curbatchid!= undefined && curbatchid!=""){
          target.$message.error("批次已创建成功，不允许修改批次编号");
          target.setValues([{
            rowKey:row.id,
            values:{
              "vendorbatchcode":oldValue
            }
          }])
        }
        return;
      }

      queryMaterialVoInfoById({materialid:curmaterialid}).then(res=>{
        if(res.success){
          if(res.result.isbatch !=null && res.result.isbatch === 'true'){

          }else{
            target.$message.error("当前物料不进行批次管理！");
            target.setValues([{
              rowKey:row.id,
              values:{
                "vendorbatchcode":""
              }
            }])
          }
        }
      })
    } else if(column.key === 'batchcode'){
      if(row === null || row === undefined){
        return;
      }
      let curmaterialid=row.materialid;
      if(curmaterialid=== null || curmaterialid === '' || curmaterialid === undefined){
        target.$message.error("请先选择物料");
        target.setValues([{
          rowKey:row.id,
          values:{
            "batchcode":oldValue
          }
        }])
        return;
      }
      if(row.hasOwnProperty("batchid")){
        let curbatchid=row.batchid;
        if(curbatchid!= null && curbatchid!= undefined && curbatchid!=""){
          target.$message.error("批次已创建成功，不允许修改批次编号");
          target.setValues([{
            rowKey:row.id,
            values:{
              "batchcode":oldValue
            }
          }])
        }
      }

      queryMaterialVoInfoById({materialid:curmaterialid}).then(res=>{
        if(res.success){
          if(res.result.isbatch !=null && res.result.isbatch === 'true'){

          }else{
            target.$message.error("当前物料不进行批次管理！");
            target.setValues([{
              rowKey:row.id,
              values:{
                "batchcode":""
              }
            }])
          }
        }
      })
    } else if(column.key === 'stocklocationname' || column.key === 'fromstocklocationname'){
      if(value){
        if(type === 'popup'){
          let currentuser = target.$store.getters.userInfo;
          if(row.hasOwnProperty("stocklocationid")){
            localStorage.setItem(currentuser.id+'_outstock_stocklocationid',row.stocklocationid);
          }else if(row.hasOwnProperty("fromstocklocationid")){
            localStorage.setItem(currentuser.id+"_outstock_stocklocationid",row.fromstocklocationid)
          }
        }
      }
    } else if(column.key === 'batchid'){
      //更新余额
      if(value){
        let currentuser = target.$store.getters.userInfo;
        let curQty=localStorage.getItem(currentuser.id+"_batchqty");
        if(row.hasOwnProperty('balanceqty')){
          target.setValues([{
            rowKey:row.id,
            values:{
              "balanceqty":curQty
            }
          }])
        }else if(row.hasOwnProperty('budgetqty')){
          target.setValues([{
            rowKey:row.id,
            values:{
              "budgetqty":curQty
            }
          }])
        }
        
        
      }
    }
}