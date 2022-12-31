package org.br.erp.pr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.jeecg.common.aspect.annotation.CustomCode;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 采购计划分录
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
@ApiModel(value="scmpritem对象", description="采购计划分录")
@Data
@TableName("scmpritem")
@Entity
@CustomCode(objectCode = "scmpritem")
public class Scmpritem implements Serializable {
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

	/**计划数量*/
	@Excel(name = "计划数量", width = 15)
    @ApiModelProperty(value = "计划数量")
    private String qty;
	/**计划单价*/
	@Excel(name = "计划单价", width = 15)
    @ApiModelProperty(value = "计划单价")
    private String taxinprice;
	/**需求日期*/
	@Excel(name = "需求日期", width = 15)
    @ApiModelProperty(value = "需求日期")
    private String prdate;
	/**计划单价*/
	@Excel(name = "计划单价", width = 15)
    @ApiModelProperty(value = "计划单价")
    private String taxinvalue;
	/**建议供应商*/
	@Excel(name = "建议供应商", width = 15)
    @ApiModelProperty(value = "建议供应商")
    private String prvendorid;
    /*来源单据主表id*/
    private String fromid;
    /*来源单据分录id*/
    private String fromitemid;

    /*最早订单下达订货日期*/
    private String prorderDate;

    @ColumnDefault("0")
    private String ispo;

    private String poqty;


}
