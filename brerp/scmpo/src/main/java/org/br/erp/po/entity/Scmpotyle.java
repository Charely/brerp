package org.br.erp.po.entity;

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
 * @Description: 采购订单类型
 * @Author: jeecg-boot
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Data
@TableName("scmpotyle")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmpotyle对象", description="采购订单类型")
public class Scmpotyle implements Serializable {
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
	/**类型编号*/
	@Excel(name = "类型编号", width = 15)
    @ApiModelProperty(value = "类型编号")
    private java.lang.String typecode;
	/**类型名称*/
	@Excel(name = "类型名称", width = 15)
    @ApiModelProperty(value = "类型名称")
    private java.lang.String typename;
	/**默认行类别*/
	@Excel(name = "默认行类别", width = 15)
    @ApiModelProperty(value = "默认行类别")
    @Dict(dictTable = "scmpoitemtype", dicText = "itemtypename", dicCode = "id")
    private java.lang.String defaultlinktype;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
    @Dict(dicCode = "yes_or_not")
    private java.lang.String canenable;
	/**允许他人修改*/
	@Excel(name = "允许他人修改", width = 15)
    @ApiModelProperty(value = "允许他人修改")
    @Dict(dicCode = "yes_or_not")
    private java.lang.String canedit;
	/**允许修改行类别*/
	@Excel(name = "允许修改行类别", width = 15)
    @ApiModelProperty(value = "允许修改行类别")
    @Dict(dicCode = "yes_or_not")
    private java.lang.String caneditlinktype;
}
