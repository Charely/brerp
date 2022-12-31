package org.br.erp.po.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 货源清单分录
 * @Author: jeecg-boot
 * @Date:   2022-08-07
 * @Version: V1.0
 */
@ApiModel(value="materialvendorlinkitem对象", description="货源清单分录")
@Data
@TableName("materialvendorlinkitem")
@Entity
public class Materialvendorlinkitem implements Serializable {
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
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private String vendorid;
	/**物料编码*/
	@Excel(name = "供应商编码", width = 15)
    @ApiModelProperty(value = "供应商编码")
    private String vendorcode;
	/**物料名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private String vendorname;
	/**最大供货数量*/
	@Excel(name = "最大供货数量", width = 15)
    @ApiModelProperty(value = "最大供货数量")
    private java.math.BigDecimal maxqty;
	/**最小供货数量*/
	@Excel(name = "最小供货数量", width = 15)
    @ApiModelProperty(value = "最小供货数量")
    private java.math.BigDecimal minqty;
	/**订货次数*/
	@Excel(name = "订货次数", width = 15)
    @ApiModelProperty(value = "订货次数")
    private Integer orderbatch;
	/**累计订货数量*/
	@Excel(name = "累计订货数量", width = 15)
    @ApiModelProperty(value = "累计订货数量")
    private java.math.BigDecimal sumorderqty;
	/**累计订货金额*/
	@Excel(name = "累计订货金额", width = 15)
    @ApiModelProperty(value = "累计订货金额")
    private java.math.BigDecimal sumordervalue;
	/**累计订货次数*/
	@Excel(name = "累计订货次数", width = 15)
    @ApiModelProperty(value = "累计订货次数")
    private java.math.BigDecimal sumorderbatch;
	/**最近订单编号*/
	@Excel(name = "最近订单编号", width = 15)
    @ApiModelProperty(value = "最近订单编号")
    private String lastpocode;
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private String parentid;

    @ApiModelProperty(value = "fixedvendor")
    private String fixedvendor;
}
