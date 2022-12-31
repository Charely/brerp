package org.br.erp.outsource.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 委外订单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
@ApiModel(value="scmoutsourceitem对象", description="委外订单分录")
@Data
@TableName("scmoutsourceitem")
@Entity
public class Scmoutsourceitem implements Serializable {
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
	/**委外数量*/
	@Excel(name = "委外数量", width = 15)
    @ApiModelProperty(value = "委外数量")
    private java.math.BigDecimal qty;
	/**计量单位*/
	@Excel(name = "计量单位", width = 15)
    @ApiModelProperty(value = "计量单位")
    private java.lang.String uomid;
	/**含税单价*/
	@Excel(name = "含税单价", width = 15)
    @ApiModelProperty(value = "含税单价")
    private java.math.BigDecimal taxinprice;
	/**不含税单价*/
	@Excel(name = "不含税单价", width = 15)
    @ApiModelProperty(value = "不含税单价")
    private java.math.BigDecimal taxexprice;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private java.math.BigDecimal taxrate;
	/**含税金额*/
	@Excel(name = "含税金额", width = 15)
    @ApiModelProperty(value = "含税金额")
    private java.math.BigDecimal taxinvalue;
	/**不含税金额*/
	@Excel(name = "不含税金额", width = 15)
    @ApiModelProperty(value = "不含税金额")
    private java.math.BigDecimal taxexvalue;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
    @ColumnDefault("0")
    private String receiptreqqty;
    @ColumnDefault("0")
    private String receiptqty;
    @ColumnDefault("0")
    private String invoiceqty;
    @ColumnDefault("0")
    private String receiptreqflag;
    @ColumnDefault("0")
    private String receiptflag;
    @ColumnDefault("0")
    private String invoiceflag;
    private String bomitemid;
}
