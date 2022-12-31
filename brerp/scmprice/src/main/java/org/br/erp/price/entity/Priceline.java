package org.br.erp.price.entity;

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
 * @Description: 价格管理
 * @Author: jeecg-boot
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Data
@TableName("priceline")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="priceline对象", description="价格管理")
public class Priceline implements Serializable {
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
	/**价格类型*/
	@Excel(name = "价格类型", width = 15, dicCode = "pricetype")
	@Dict(dicCode = "pricetype")
    @ApiModelProperty(value = "价格类型")
    private java.lang.String pricetype;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    @Dict(dictTable = "scmpartner",dicText = "partnername",dicCode = "id")
    private java.lang.String vendorid;
	/**客户*/
	@Excel(name = "客户", width = 15)
    @ApiModelProperty(value = "客户")
    @Dict(dictTable = "scmpartner",dicText = "partnername",dicCode = "id")
    private java.lang.String customerid;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    @Dict(dictTable = "material",dicText = "materialname",dicCode = "id")
    private java.lang.String materialid;
	/**价格*/
	@Excel(name = "价格", width = 15)
    @ApiModelProperty(value = "价格")
    private java.math.BigDecimal price;
}
