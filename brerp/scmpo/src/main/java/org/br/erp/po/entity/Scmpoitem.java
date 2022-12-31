package org.br.erp.po.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.ColumnDefault;
import org.jeecg.common.aspect.annotation.CustomCode;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 采购订单分录
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
@ApiModel(value="scmpoitem对象", description="采购订单分录")
@Data
@TableName("scmpoitem")
@Entity
@CustomCode(objectCode = "scmpoitem")
public class Scmpoitem implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private String materialid;
	/**物料编号*/
	@Excel(name = "物料编号", width = 15)
    @ApiModelProperty(value = "物料编号")
    private String materialcode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private String materialname;
	/**采购数量*/
	@Excel(name = "采购数量", width = 15)
    @ApiModelProperty(value = "采购数量")
    private java.math.BigDecimal qty;
	/**含税单价*/
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
	/**需求日期*/
	@Excel(name = "需求日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "需求日期")
    private String preqdate;
	/**关闭*/
	@Excel(name = "关闭", width = 15)
    @ApiModelProperty(value = "关闭")
    private String isfinish;
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private String parentid;

    /*交货日期*/
    private String deliverydate;

    private String fromitemid;
    @Dict(dictTable = "warehouse",dicText = "name",dicCode = "id")
    private String warehouseid;
    @Dict(dictTable = "scmstocklocation",dicCode = "id",dicText = "locationname")
    private String stocklocationid;

    private String itemcode;

    /*入库申请标志 0 未生成 1部分生成 2全部生成*/
    @ColumnDefault("0")
    private String receiptreqflag;
    /*入库数量*/
    @ColumnDefault("0")
    private BigDecimal receiptreqqty;
    /*实际入库标志*/
    @ColumnDefault("0")
    private String receiptflag;
    /*实际入库数量*/
    @ColumnDefault("0")
    private String receiptqty;

    private String uomid;

    /*退货标志*/
    @ColumnDefault("0")
    private String returnflag;
    /*退货数量*/
    @ColumnDefault("0")
    private String returnqty;
    @ColumnDefault("0")
    private String invoiceflag;
    @ColumnDefault("0")
    private String invoiceqty;

    /*付款标记*/
    @ColumnDefault("0")
    private String paymentflag;
    /*付款数量*/
    @ColumnDefault("0")
    private String paymentqty;

    @ColumnDefault("0")
    private String isred;

}
