package org.br.erp.po.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 收料申请单分录
 * @Author: jeecg-boot
 * @Date:   2022-10-18
 * @Version: V1.0
 */
@ApiModel(value="scmmatreceiptreqitem对象", description="收料申请单分录")
@Data
@TableName("scmmatreceiptreqitem")
@Entity
public class Scmmatreceiptreqitem implements Serializable {
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
    /*来源单据类型*/
    private String fromtype;

    @Excel(name = "含税单价", width = 15)
    @ApiModelProperty(value = "含税单价")
    private java.math.BigDecimal taxinprice;

    private BigDecimal taxrate;
    /**不含税单价*/
    @Excel(name = "不含税单价", width = 15)
    @ApiModelProperty(value = "不含税单价")
    private java.math.BigDecimal taxexprice;
    /**含税总价*/
    @Excel(name = "含税总价", width = 15)
    @ApiModelProperty(value = "含税总价")
    private java.math.BigDecimal taxinvalue;

    private BigDecimal taxexvalue;
}
