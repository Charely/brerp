package org.br.erp.base.entityvo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScmOutSourceToOutStockModel {

    private String companyid;
    private String batchid;
    private String warehouseid;
    private String vendorid;
    private String billdate;
    private String billcode;
    private String fromid;
    private String fromitemid;
    private String outqty;
    private String materialid;
    private String materialcode;
    private String materialname;
}
