package org.br.erp.so.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 销售发票分录
 * @Author: jeecg-boot
 * @Date:   2022-11-08
 * @Version: V1.0
 */
@ApiModel(value="scmsalesinvoiceitem对象", description="销售发票分录")
@Data
@TableName("scmsalesinvoiceitem")
@Entity
public class Scmsalesinvoiceitem implements Serializable {
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
	/**物料编号*/
	@Excel(name = "物料编号", width = 15)
    @ApiModelProperty(value = "物料编号")
    private java.lang.String materialcode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private java.lang.String materialname;
	/**销售数量*/
	@Excel(name = "销售数量", width = 15)
    @ApiModelProperty(value = "销售数量")
    private java.lang.String qty;
	/**含税单价*/
	@Excel(name = "含税单价", width = 15)
    @ApiModelProperty(value = "含税单价")
    private java.lang.String taxinprice;
	/**不含税单价*/
	@Excel(name = "不含税单价", width = 15)
    @ApiModelProperty(value = "不含税单价")
    private java.lang.String taxexprice;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private java.lang.String taxrate;
	/**含税金额*/
	@Excel(name = "含税金额", width = 15)
    @ApiModelProperty(value = "含税金额")
    private java.lang.String taxinvalue;
	/**不含税金额*/
	@Excel(name = "不含税金额", width = 15)
    @ApiModelProperty(value = "不含税金额")
    private java.lang.String taxexvalue;
    private String uomid;
	/**fromitemid*/
	@Excel(name = "fromitemid", width = 15)
    @ApiModelProperty(value = "fromitemid")
    private java.lang.String fromitemid;
	/**fromid*/
	@Excel(name = "fromid", width = 15)
    @ApiModelProperty(value = "fromid")
    private java.lang.String fromid;
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private java.lang.String parentid;
}
