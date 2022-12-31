import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'


export  const queryMaterialById=(params)=>{
    return getAction("/base/material/queryById",params);
}

export const queryMaterialVoInfoById=(params)=>{
    return getAction("/base/material/getmaterialinfobymaterialid",params);
}


export const queryWarehouseInfoById=(params)=>{
  return getAction("/base/warehouse/queryById",params);
}
/**获取库存状态 */
export const getStocktype=()=>{
    return getAction("/base/scmstocktype/list");
}

export const getInventoryKind=()=>{
    return getAction("/base/scminventorykinds/list");
}

export const getscmbatchbycompanyidandmaterialid=(params)=>{
  return getAction("/inventory.base/scmimbalance/getscmbatchbycompanyidandmaterialid",params);
}

export const getscmbatchlist=(params)=>{
  return getAction("/inventory.base/scmimbalance/batchlist",params);
}

export  function uuid() {
  var s = [];
  var hexDigits = "0123456789abcdef";
  for (var i = 0; i < 32; i++) {
  s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23];
  var uuid = s.join("");
  return uuid;
}

export const queryprintdatasourcebyobjectcode=(params)=>{
  return getAction("/base/scmprintdatasource/queryprintdatasourcebyobjectcode",params);
}