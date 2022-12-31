package org.br.erp.base.service.impl;

import org.br.erp.base.mapper.ErpBaseMapper;
import org.br.erp.base.service.IErpBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ErpBaseServiceImpl implements IErpBaseService {

    @Autowired
    ErpBaseMapper erpBaseMapper;


    @Override
    @Transactional
    public void updateTableStatus(String tableName, String statusField, String statusValue, String idField, String idValue) {
         erpBaseMapper.updateTableStatus(tableName,statusField,statusValue,idField,idValue);
    }

    @Override
    @Transactional
    public void saveCustomFieldValues(Map<Object, Object> customFields, String tablename, String idvalue) {
        if(customFields!=null){
            for(Object key : customFields.keySet()){
                Object keyvalue = customFields.get(key);
                if(keyvalue!=null){
                    erpBaseMapper.updateTableStatus(tablename, (String) key, (String) keyvalue,"id",idvalue);
                }
            }
        }
    }

    @Override
    public void createTmpTable() {
        erpBaseMapper.createTmpTable();
       // erpBaseMapper.inserttotmptable(p);
    }

    @Override
    public void inserttotmptable(List<String> p) {
        erpBaseMapper.createTmpTable();
        erpBaseMapper.inserttotmptable(p);
    }
}
