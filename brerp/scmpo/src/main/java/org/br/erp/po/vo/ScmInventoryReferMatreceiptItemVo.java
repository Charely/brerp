package org.br.erp.po.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.math.BigDecimal;

@Data
public class ScmInventoryReferMatreceiptItemVo {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    @Id
    private java.lang.String id;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**parentid*/
    @ApiModelProperty(value = "parentid")
    private java.lang.String parentid;
    /**行号*/
    @Excel(name = "行号", width = 15)
    @ApiModelProperty(value = "行号")
    private java.lang.String itemcode;
    /**物料*/
    @Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private java.lang.String materialid;
    /**物料编号*/
    @Excel(name = "物料编号", width = 15)
    @ApiModelProperty(value = "物料编号")
    private java.lang.String materialcode;
    /**物料名称*/
    @Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private java.lang.String materialname;

    @ApiModelProperty(value = "收货数量")
    private BigDecimal qty;
    /**批次编号*/
    @Excel(name = "批次编号", width = 15)
    @ApiModelProperty(value = "批次编号")
    private java.lang.String batchid;
    private String batchcode;
    private String warehouseid;
    /**供应商批次*/
    @Excel(name = "供应商批次", width = 15)
    @ApiModelProperty(value = "供应商批次")
    private java.lang.String vendorbatchcode;
    /**批次有效日期*/
    @Excel(name = "批次有效日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "批次有效日期")
    private java.util.Date batchstartdate;
    /**批次失效日期*/
    @Excel(name = "批次失效日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "批次失效日期")
    private java.util.Date batchenddate;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;

    private String fromid;

    private String fromitemid;

    /**
     * 入库标识
     */
    @ColumnDefault("0")
    private String receiptflag;

    /**
     * 入库数量
     */
    @ColumnDefault("0")
    private BigDecimal receiptqty;
    private String uomid;
    private BigDecimal taxinprice;
    private BigDecimal taxexprice;
    private BigDecimal taxrate;
}
