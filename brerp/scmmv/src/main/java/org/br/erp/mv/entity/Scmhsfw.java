package org.br.erp.mv.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 存货核算范围
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@ApiModel(value="scmhsfw对象", description="存货核算范围")
@Data
@TableName("scmhsfw")
public class Scmhsfw implements Serializable {
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
	/**核算组织*/
	@Excel(name = "核算组织", width = 15)
    @ApiModelProperty(value = "核算组织")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private java.lang.String companyid;
	/**核算范围编号*/
	@Excel(name = "核算范围编号", width = 15)
    @ApiModelProperty(value = "核算范围编号")
    private java.lang.String hscode;
	/**核算范围名称*/
	@Excel(name = "核算范围名称", width = 15)
    @ApiModelProperty(value = "核算范围名称")
    private java.lang.String hsname;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
    private java.lang.String hsisvalid;
	/**核算层次*/
	@Excel(name = "核算层次", width = 15)
    @ApiModelProperty(value = "核算层次")
    @Dict(dictTable = "scmhslevel",dicCode = "id",dicText = "name")
    private java.lang.String hslevel;
    /*统一计价方法*/
    @Dict(dictTable = "scmjjmethod",dicText = "jjname",dicCode = "jjcode")
    private String jjmethod;
}
