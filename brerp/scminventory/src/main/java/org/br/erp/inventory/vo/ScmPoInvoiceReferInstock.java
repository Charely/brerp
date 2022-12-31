package org.br.erp.inventory.vo;

import lombok.Data;

@Data
public class ScmPoInvoiceReferInstock {

    private String id;
    private String itemid;
    private String billcode;
    private String billdate;
    private String vendorid;
    private String vendorcode;
    private String vendorname;
    private String remarks;
    private String materialid;
    private String materialcode;
    private String materialname;
    private String qty;//总数量
    private String invoiceqty;//已开票数量
    private String batchid;//批号
    private String warehouseid;//仓库
    private String batchcode;
    private String taxinprice;
    private String taxrate;
    private String taxexprice;
}
