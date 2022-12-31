package org.jeecg.modules.base.objectEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

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
public class Scmbillcoderule implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
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
	/**业务对象ID*/
	@Excel(name = "业务对象ID", width = 15)
    @ApiModelProperty(value = "业务对象ID")
    private String objectid;
	/**编号规则*/
	@Excel(name = "编号规则", width = 15)
    @ApiModelProperty(value = "编号规则")
    private String billrulecode;
	/**是否包含年*/
	@Excel(name = "是否包含年", width = 15)
    @ApiModelProperty(value = "是否包含年")
    private String billruleyear;
	/**是否包含月*/
	@Excel(name = "是否包含月", width = 15)
    @ApiModelProperty(value = "是否包含月")
    private String billrulemonth;
	/**是否包含日*/
	@Excel(name = "是否包含日", width = 15)
    @ApiModelProperty(value = "是否包含日")
    private String billruleday;
	/**流水号位数*/
	@Excel(name = "流水号位数", width = 15)
    @ApiModelProperty(value = "流水号位数")
    private Integer billrulenum;
	/**断号重用*/
	@Excel(name = "断号重用", width = 15)
    @ApiModelProperty(value = "断号重用")
    private String billruledhsy;
	/**固定前缀*/
	@Excel(name = "固定前缀", width = 15)
    @ApiModelProperty(value = "固定前缀")
    private String fixedcode;
}
