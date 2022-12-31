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

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 批号档案
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
@Data
@TableName("scmbatch")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmbatch对象", description="批次档案")
@Entity
public class Scmbatch implements Serializable {
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
	/**库存组织*/
	@Excel(name = "库存组织", width = 15)
    @ApiModelProperty(value = "库存组织")
    private java.lang.String imorgid;

    @Dict(dictTable = "sys_depart",dicCode = "id",dicText = "depart_name")
    private String companyid;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    @Dict(dictTable = "warehouse",dicCode = "id",dicText = "name")
    private java.lang.String warehouseid;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    @Dict(dictTable = "material",dicText = "materialname",dicCode = "id")
    private java.lang.String materialid;
	/**批号*/
	@Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号")
    private java.lang.String batchcode;
	/**批号数量*/
	@Excel(name = "批号数量", width = 15)
    @ApiModelProperty(value = "批号数量")
    private java.lang.String batchqty;
	/**原始批号*/
	@Excel(name = "原始批号", width = 15)
    @ApiModelProperty(value = "原始批号")
    private java.lang.String orgbatchcode;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    @Dict(dictTable = "scmpartner",dicCode = "id",dicText = "partnername")
    private java.lang.String vendorid;
	/**有效期从*/
	@Excel(name = "有效期从", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "有效期从")
    private java.lang.String fromdate;
	/**有效期止*/
	@Excel(name = "有效期止", width = 15)
    @ApiModelProperty(value = "有效期止")
    private java.lang.String enddate;
}
