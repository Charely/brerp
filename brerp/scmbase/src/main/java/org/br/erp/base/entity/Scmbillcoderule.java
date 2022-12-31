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
 * @Description: 单据编号规则表
 * @Author: jeecg-boot
 * @Date:   2022-10-12
 * @Version: V1.0
 */
@Data
@TableName("scmbillcoderule")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmbillcoderule对象", description="单据编号规则表")
@Entity
public class Scmbillcoderule implements Serializable {
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
	/**业务对象ID*/
	@Excel(name = "业务对象ID", width = 15)
    @ApiModelProperty(value = "业务对象ID")
    private java.lang.String objectid;
	/**编号规则*/
	@Excel(name = "编号规则", width = 15)
    @ApiModelProperty(value = "编号规则")
    private java.lang.String billrulecode;
	/**是否包含年*/
	@Excel(name = "是否包含年", width = 15)
    @ApiModelProperty(value = "是否包含年")
    private java.lang.String billruleyear;
	/**是否包含月*/
	@Excel(name = "是否包含月", width = 15)
    @ApiModelProperty(value = "是否包含月")
    private java.lang.String billrulemonth;
	/**是否包含日*/
	@Excel(name = "是否包含日", width = 15)
    @ApiModelProperty(value = "是否包含日")
    private java.lang.String billruleday;
	/**流水号位数*/
	@Excel(name = "流水号位数", width = 15)
    @ApiModelProperty(value = "流水号位数")
    private java.lang.Integer billrulenum;
	/**断号重用*/
	@Excel(name = "断号重用", width = 15)
    @ApiModelProperty(value = "断号重用")
    private java.lang.String billruledhsy;
	/**固定前缀*/
	@Excel(name = "固定前缀", width = 15)
    @ApiModelProperty(value = "固定前缀")
    private java.lang.String fixedcode;
}
