package org.br.erp.outsource.vo;

import lombok.Data;

@Data
public class ScmReceiptReqReferWwVo {

    private String id;
    private String itemid;
    private String materialid;
    private String materialitemid;
    private String materialcode;
    private String materialname;
    private String billcode;
    private String billdate;
    private String companyid;
    private String vendorid;
    private String vendorcode;
    private String vendorname;
    private String qty;
    private String taxinprice;
    private String taxexprice;
    private String taxinvalue;
    private String taxexvalue;
    /*入库数量*/
    private String receiptqty;
    /*收获申请数量*/
    private String receiptreqqty;
}
