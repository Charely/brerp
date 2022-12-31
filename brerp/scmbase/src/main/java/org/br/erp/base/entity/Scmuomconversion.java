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
 * @Description: 计量单位换算率
 * @Author: jeecg-boot
 * @Date:   2022-11-18
 * @Version: V1.0
 */
@Data
@TableName("scmuomconversion")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmuomconversion对象", description="计量单位换算率")
@Entity
public class Scmuomconversion implements Serializable {
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
	/**源计量单位*/
	@Excel(name = "源计量单位", width = 15)
    @ApiModelProperty(value = "源计量单位")
    @Dict(dictTable = "scmuom",dicCode = "id",dicText = "uomname")
    private java.lang.String fromunitid;
	/**目标计量单位*/
	@Excel(name = "目标计量单位", width = 15)
    @ApiModelProperty(value = "目标计量单位")
    @Dict(dictTable = "scmuom",dicCode = "id",dicText = "uomname")
    private java.lang.String tounitid;
	/**换算率*/
	@Excel(name = "换算率", width = 15)
    @ApiModelProperty(value = "换算率")
    @ColumnDefault("0.0000")
    private java.math.BigDecimal conversion;
}
