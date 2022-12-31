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
import org.hibernate.annotations.ColumnDefault;
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
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
@Data
@TableName("material")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="material对象", description="产品表")
@Entity
public class Material implements Serializable {
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
	/**物料类型*/
	@Excel(name = "物料类型", width = 15, dictTable = "materialtype", dicText = "materialtypename", dicCode = "id")
	@Dict(dictTable = "materialtype", dicText = "materialtypename", dicCode = "id")
    @ApiModelProperty(value = "物料类型")
    private java.lang.String materialtypeid;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
    private java.lang.String materialcode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private java.lang.String materialname;
	/**安全库存*/
	@Excel(name = "安全库存", width = 15)
    @ApiModelProperty(value = "安全库存")
    private java.math.BigDecimal safestock;
	/**型号*/
	@Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号")
    private java.lang.String model;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.String mspecs;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    @Dict(dictTable = "scmuom",dicText = "uomname",dicCode = "id")
    private java.lang.String uom;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    @ColumnDefault("0")
    private java.lang.String enablestatus;
	/**删除标记*/
	@Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    private java.lang.String deleteflag;
	/**c1*/
	@Excel(name = "c1", width = 15)
    @ApiModelProperty(value = "c1")
    private java.lang.String c1;
	/**c2*/
	@Excel(name = "c2", width = 15)
    @ApiModelProperty(value = "c2")
    private java.lang.String c2;
	/**c3*/
	@Excel(name = "c3", width = 15)
    @ApiModelProperty(value = "c3")
    private java.lang.String c3;

    @ColumnDefault("0")
    private BigDecimal taxrate;




    private String issale;

    private String ispo;

    private String isgd;
}
