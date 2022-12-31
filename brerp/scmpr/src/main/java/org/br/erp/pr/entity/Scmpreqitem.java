package org.br.erp.pr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.CustomCode;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 采购申请表体
 * @Author: jeecg-boot
 * @Date:   2022-07-27
 * @Version: V1.0
 */
@ApiModel(value="scmpreqitem对象", description="采购申请表体")
@Data
@TableName("scmpreqitem")
@Entity
@CustomCode(objectCode = "scmpreqitem")
public class Scmpreqitem implements Serializable {
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
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private String parentid;
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

    private String uomid;
	/**申请数量*/
	@Excel(name = "申请数量", width = 15)
    @ApiModelProperty(value = "申请数量")
    private java.math.BigDecimal qty;
	/**计划单价*/
	@Excel(name = "计划单价", width = 15)
    @ApiModelProperty(value = "计划单价")
    private java.math.BigDecimal taxinprice;
	/**总金额*/
	@Excel(name = "总金额", width = 15)
    @ApiModelProperty(value = "总金额")
    private java.math.BigDecimal taxinvalue;
	/**需求日期*/
	@Excel(name = "需求日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "需求日期")
    private Date preqdate;

    /*是否已生成采购计划*/
    private String isPr;

    /*生成的采购计划分录id*/
    private String pritemid;

}
