package org.br.erp.base.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface ErpBaseMapper {

    void updateTableStatus(@Param("tableName") String tableName,
                           @Param("statusField") String statusField,
                           @Param("statusValue") String statusValue,
                           @Param("idField") String idField,
                           @Param("idValue") String idValue);


    void createTmpTable();

    void inserttotmptable(@Param("p") List<String> p);
}
