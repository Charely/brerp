package org.br.erp.pr.entity;

import lombok.Data;

@Data
public class ScmpreqVo {

    public String mainId;
    public String preqCode;
    public String CreateBy;
    public String preqDeptName;
    public String preqEmptName;
    public String remarks;
    public String totalqty;
    public String totalValue;
    public String itemid;
    public String materialcode;
    public String materialname;
    public String qty;
    public String taxinprice;
    public String itemValue;
    private String billdate;

}
