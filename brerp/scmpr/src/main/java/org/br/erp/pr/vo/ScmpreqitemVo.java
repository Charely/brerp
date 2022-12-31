package org.br.erp.pr.vo;
import lombok.Data;

import java.util.Date;

@Data
public class ScmpreqitemVo {
    private java.lang.String id;
    private java.lang.String parentid;
    private java.lang.String materialid;
    private java.lang.String materialcode;
    private java.lang.String materialname;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal taxinprice;
    private java.math.BigDecimal taxinvalue;
    private java.util.Date preqdate;
    /*是否已生成采购计划*/
    private String isPr;
    private Date orderdate;
    private String uomid;
}
