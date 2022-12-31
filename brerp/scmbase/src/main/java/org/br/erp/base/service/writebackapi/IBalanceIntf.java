package org.br.erp.base.service.writebackapi;

import java.util.Map;

@Deprecated
public interface IBalanceIntf {
    public Map getBalanceByCompanyAndWarehouse(String companyid,String warehouseid,String batchid,String materialid);
}
