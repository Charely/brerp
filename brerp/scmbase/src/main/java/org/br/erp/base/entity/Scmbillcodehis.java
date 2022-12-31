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
 * @Description: 单据编号历史记录
 * @Author: jeecg-boot
 * @Date:   2022-10-14
 * @Version: V1.0
 */
@Data
@TableName("scmbillcodehis")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmbillcodehis对象", description="单据编号历史记录")
@Entity
public class Scmbillcodehis implements Serializable {
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
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
    @ApiModelProperty(value = "是否有效")
    private java.lang.String isvaild;
	/**最大流水号*/
	@Excel(name = "最大流水号", width = 15)
    @ApiModelProperty(value = "最大流水号")
    private java.lang.Integer maxbillcode;
	/**年*/
	@Excel(name = "年", width = 15)
    @ApiModelProperty(value = "年")
    private java.lang.String year;
	/**月*/
	@Excel(name = "月", width = 15)
    @ApiModelProperty(value = "月")
    private java.lang.String month;
	/**日*/
	@Excel(name = "日", width = 15)
    @ApiModelProperty(value = "日")
    private java.lang.String day;
}
