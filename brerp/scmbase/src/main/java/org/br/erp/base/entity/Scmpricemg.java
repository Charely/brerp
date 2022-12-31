package org.br.erp.base.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 供应链价格管理
 * @Author: jeecg-boot
 * @Date:   2022-11-13
 * @Version: V1.0
 */
@Data
@TableName("scmpricemg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmpricemg对象", description="供应链价格管理")
public class Scmpricemg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
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
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private java.lang.String vendorid;
	/**客户*/
	@Excel(name = "客户", width = 15)
    @ApiModelProperty(value = "客户")
    private java.lang.String customid;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private java.lang.String materialid;
	/**采购价*/
	@Excel(name = "采购价", width = 15)
    @ApiModelProperty(value = "采购价")
    private java.lang.String poprice;
	/**销售价*/
	@Excel(name = "销售价", width = 15)
    @ApiModelProperty(value = "销售价")
    private java.lang.String soprice;
	/**计划价*/
	@Excel(name = "计划价", width = 15)
    @ApiModelProperty(value = "计划价")
    private java.lang.String planprice;
	/**采购组织*/
	@Excel(name = "采购组织", width = 15)
    @ApiModelProperty(value = "采购组织")
    private java.lang.String poorgid;
	/**销售组织*/
	@Excel(name = "销售组织", width = 15)
    @ApiModelProperty(value = "销售组织")
    private java.lang.String soorgid;
	/**存货组织*/
	@Excel(name = "存货组织", width = 15)
    @ApiModelProperty(value = "存货组织")
    private java.lang.String mvorgid;
}
