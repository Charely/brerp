package org.br.erp.so.vo;

import lombok.Data;

@Data
public class ScmOutStockReferSoVo {
    private String mainid;
    private String itemid;
    private String customerid;
    private String billcode;
    private String billdate;
    private String materialid;
    private String materialcode;
    private String materialname;
    private String uomid;
    private String uomname;
    private String soqty;
    private String outqty;
    private String returnqty;
    private String customercode;
    private String customername;
    private String isred;
    private String invoiceqty;
}
