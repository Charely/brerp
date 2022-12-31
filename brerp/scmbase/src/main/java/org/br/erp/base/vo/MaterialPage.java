package org.br.erp.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.br.erp.base.entity.Materialpo;
import org.br.erp.base.entity.Materialsale;
import org.br.erp.base.entity.Materialstock;
import org.hibernate.annotations.ColumnDefault;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MaterialPage {
    private String id;

    @Excel(name = "物料类型", width = 15, dictTable = "materialtype", dicText = "materialtypename", dicCode = "id")
    @Dict(dictTable = "materialtype", dicText = "materialtypename", dicCode = "id")
    @ApiModelProperty(value = "物料类型")
    private java.lang.String materialtypeid;
    /**编号*/
    @Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
    private java.lang.String materialcode;
    /**物料名称*/
    @Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private java.lang.String materialname;
    /**安全库存*/
    @Excel(name = "安全库存", width = 15)
    @ApiModelProperty(value = "安全库存")
    private java.math.BigDecimal safestock;
    /**型号*/
    @Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号")
    private java.lang.String model;
    /**规格*/
    @Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.String mspecs;
    /**单位*/
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    @Dict(dictTable = "scmuom",dicText = "uomname",dicCode = "id")
    private java.lang.String uom;
    /**状态*/
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    @ColumnDefault("0")
    private java.lang.String enablestatus;
    /**删除标记*/
    @Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    private java.lang.String deleteflag;
    /**c1*/
    @Excel(name = "c1", width = 15)
    @ApiModelProperty(value = "c1")
    private java.lang.String c1;
    /**c2*/
    @Excel(name = "c2", width = 15)
    @ApiModelProperty(value = "c2")
    private java.lang.String c2;
    /**c3*/
    @Excel(name = "c3", width = 15)
    @ApiModelProperty(value = "c3")
    private java.lang.String c3;

    @ColumnDefault("0")
    private BigDecimal taxrate;

    private String issale;

    private String ispo;

    private String isgd;

    private List<Materialsale> materialSaleList;

    private List<Materialpo> materialPoList;

    private List<Materialstock> materialStockList;
}
