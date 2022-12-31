package org.br.erp.so.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 销售订单分录
 * @Author: jeecg-boot
 * @Date:   2022-10-30
 * @Version: V1.0
 */
@ApiModel(value="scmsoitem对象", description="销售订单分录")
@Data
@TableName("scmsoitem")
@Entity
public class Scmsoitem implements Serializable {
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
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private java.lang.String materialid;

    private String materialcode;

    private String materialname;
	/**计量单位*/
	@Excel(name = "计量单位", width = 15)
    @ApiModelProperty(value = "计量单位")
    private java.lang.String uomid;
	/**销售数量*/
	@Excel(name = "销售数量", width = 15)
    @ApiModelProperty(value = "销售数量")
    private BigDecimal qty;
	/**销售单价*/
	@Excel(name = "销售单价", width = 15)
    @ApiModelProperty(value = "销售单价")
    private BigDecimal taxinprice;
	/**含税单价*/
	@Excel(name = "含税单价", width = 15)
    @ApiModelProperty(value = "含税单价")
    private BigDecimal taxinvalue;

    private BigDecimal taxexprice;

    private BigDecimal taxexvalue;

    private BigDecimal taxrate;

    @ColumnDefault("0")
    private String isred;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private java.lang.String parentid;

    /*出库标志*/
    @ColumnDefault("0")
    private String outflag;
    /*出库数量*/
    @ColumnDefault("0")
    private BigDecimal outqty;

    /*退货处理标志*/
    @ColumnDefault("0")
    private String returnflag;
    /*退货数量*/
    @ColumnDefault("0")
    private BigDecimal returnqty;

    @ColumnDefault("0")
    private String invoiceflag;

    @ColumnDefault("0")
    private BigDecimal invoiceqty;

    private String fromitemid;

    private String fromid;
}
