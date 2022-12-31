package org.br.erp.inventory.base.entity;

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
 * @Description: 库存期间表
 * @Author: jeecg-boot
 * @Date:   2022-11-14
 * @Version: V1.0
 */
@Data
@TableName("scmmvperiod")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmmvperiod对象", description="库存期间表")
public class Scmmvperiod implements Serializable {
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
    private java.lang.String orgid;
	/**库存期间*/
	@Excel(name = "库存期间", width = 15)
    @ApiModelProperty(value = "库存期间")
    private java.lang.String periodcode;
	/**起始日期*/
	@Excel(name = "起始日期", width = 15)
    @ApiModelProperty(value = "起始日期")
    private java.lang.String fromdate;
	/**截止日期*/
	@Excel(name = "截止日期", width = 15)
    @ApiModelProperty(value = "截止日期")
    private java.lang.String enddate;
	/**期间状态*/
	@Excel(name = "期间状态", width = 15)
    @ApiModelProperty(value = "期间状态")
    private java.lang.String mvstate;
}
