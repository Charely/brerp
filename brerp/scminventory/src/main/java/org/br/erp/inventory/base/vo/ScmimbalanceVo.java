package org.br.erp.inventory.base.vo;

import lombok.Data;

@Data
public class ScmimbalanceVo {
    private String id;
    private String materialid;
    private String materialcode;
    private String materialname;
    private String vendorid;
    private String vendorcode;
    private String vendorname;
    private String warehouseid;
    private String warehousecode;
    private String warehousename;
    private String beginyearqty;
    private String beginmonthqty;
    private String inamount;
    private String outamount;
    private String endmonthqty;
    private String endyearqty;
    private String amount;
    private String balancedate;
    private String batchid;
    private String batchcode;
    private String stockname;
    private String kindname;
    private String companyid;
    private String companyname;
    private String stocklocationid;
    private String stocklocationname;
}
