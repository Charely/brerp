package org.br.erp.po.vo;

import lombok.Data;

@Data
public class ScmMatReceiptReferPoVo {
    private String billid;
    private String billcode;
    private String billdate;
    private String vendorid;
    private String vendorcode;
    private String vendorname;
    private String materialid;
    private String materialcode;
    private String materialname;
    private String poqty;
    private String receiptreqqty;
    private String createby;
    private String itemid;
    private String returnqty;
    private String receiptqty;
    private String invoiceqty;
    private String isred;
}
