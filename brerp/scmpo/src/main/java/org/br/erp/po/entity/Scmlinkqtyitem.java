package org.br.erp.po.entity;

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
import java.io.UnsupportedEncodingException;

/**
 * @Description: 配额协议分录
 * @Author: jeecg-boot
 * @Date:   2022-08-27
 * @Version: V1.0
 */
@ApiModel(value="scmlinkqtyitem对象", description="配额协议分录")
@Data
@TableName("scmlinkqtyitem")
public class Scmlinkqtyitem implements Serializable {
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
	/**供应商编号*/
	@Excel(name = "供应商编号", width = 15)
    @ApiModelProperty(value = "供应商编号")
    private java.lang.String vendorcode;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private java.lang.String vendorname;
	/**配额比例*/
	@Excel(name = "配额比例", width = 15)
    @ApiModelProperty(value = "配额比例")
    private java.lang.Double quotarate;
	/**最大比例*/
	@Excel(name = "最大比例", width = 15)
    @ApiModelProperty(value = "最大比例")
    private java.math.BigDecimal maxrate;
	/**最大数量*/
	@Excel(name = "最大数量", width = 15)
    @ApiModelProperty(value = "最大数量")
    private java.math.BigDecimal maxqty;
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private java.lang.String parentid;
}
