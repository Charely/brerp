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
 * @Description: 仓库物料安全库存定义
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
@Data
@TableName("scmorgwarelink")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmorgwarelink对象", description="仓库物料安全库存定义")
public class Scmorgwarelink implements Serializable {
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
	/**库存组织*/
	@Excel(name = "库存组织", width = 15)
    @ApiModelProperty(value = "库存组织")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private java.lang.String imorgid;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    @Dict(dictTable = "warehouse",dicText = "name",dicCode = "id")
    private java.lang.String warehoseid;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    @Dict(dictTable = "material",dicCode = "id",dicText = "materialname")
    private java.lang.String materialid;
	/**物料计量单位*/
	@Excel(name = "物料计量单位", width = 15)
    @ApiModelProperty(value = "物料计量单位")
    @Dict(dictTable = "scmuom",dicText = "uomname",dicCode = "id")
    private java.lang.String uomid;
	/**最小库存*/
	@Excel(name = "最小库存", width = 15)
    @ApiModelProperty(value = "最小库存")
    private java.lang.String minqty;
	/**最大库存*/
	@Excel(name = "最大库存", width = 15)
    @ApiModelProperty(value = "最大库存")
    private java.lang.String maxqty;
	/**安全库存*/
	@Excel(name = "安全库存", width = 15)
    @ApiModelProperty(value = "安全库存")
    private java.lang.String safeqty;
	/**再订货点*/
	@Excel(name = "再订货点", width = 15)
    @ApiModelProperty(value = "再订货点")
    private java.lang.String orderqty;
	/**经济批量*/
	@Excel(name = "经济批量", width = 15)
    @ApiModelProperty(value = "经济批量")
    private java.lang.String economicbatch;
}
