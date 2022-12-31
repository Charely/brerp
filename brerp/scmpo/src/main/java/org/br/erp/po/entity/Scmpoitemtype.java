package org.br.erp.po.entity;

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
 * @Description: 采购行类别定义
 * @Author: jeecg-boot
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Data
@TableName("scmpoitemtype")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmpoitemtype对象", description="采购行类别定义")
public class Scmpoitemtype implements Serializable {
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
	/**行类别编号*/
	@Excel(name = "行类别编号", width = 15)
    @ApiModelProperty(value = "行类别编号")
    private java.lang.String itemtypecode;
	/**行类别名称*/
	@Excel(name = "行类别名称", width = 15)
    @ApiModelProperty(value = "行类别名称")
    private java.lang.String itemtypename;
	/**库存类型*/
	@Excel(name = "库存类型", width = 15)
    @ApiModelProperty(value = "库存类型")
    private java.lang.String itemstocktype;
	/**交货控制*/
	@Excel(name = "交货控制", width = 15)
    @ApiModelProperty(value = "交货控制")
    private java.lang.String inboundcontrol;
	/**需要到货*/
	@Excel(name = "需要到货", width = 15, dicCode = "yes_or_not")
	@Dict(dicCode = "yes_or_not")
    @ApiModelProperty(value = "需要到货")
    private java.lang.String needinbound;
	/**需要入库*/
	@Excel(name = "需要入库", width = 15, dicCode = "yes_or_not")
	@Dict(dicCode = "yes_or_not")
    @ApiModelProperty(value = "需要入库")
    private java.lang.String needgr;
	/**开票依据*/
	@Excel(name = "开票依据", width = 15, dicCode = "poinvoicecontrol")
	@Dict(dicCode = "poinvoicecontrol")
    @ApiModelProperty(value = "开票依据")
    private java.lang.String invoicecontrol;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**启用*/
	@Excel(name = "启用", width = 15, dicCode = "yes_or_not")
	@Dict(dicCode = "yes_or_not")
    @ApiModelProperty(value = "启用")
    private java.lang.String canenable;
}
