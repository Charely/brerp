package org.br.erp.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IErpBaseService {

    void updateTableStatus(String tableName,String statusField,String statusValue,String idField,String idValue);

    void saveCustomFieldValues(Map<Object,Object> customFields,String tablename,String idvalue);


    void createTmpTable();
    void inserttotmptable(List<String> p);
}
