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
 * @Description: 联系人
 * @Author: jeecg-boot
 * @Date:   2022-11-19
 * @Version: V1.0
 */
@Data
@TableName("scmcontact")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmcontact对象", description="联系人")
public class Scmcontact implements Serializable {
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
	/**联系人头像*/
	@Excel(name = "联系人头像", width = 15)
    @ApiModelProperty(value = "联系人头像")
    private java.lang.String contactimg;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private java.lang.String contactname;
	/**所属公司*/
	@Excel(name = "所属公司", width = 15)
    @ApiModelProperty(value = "所属公司")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private java.lang.String companyid;
	/**关联供应商*/
	@Excel(name = "关联供应商", width = 15)
    @ApiModelProperty(value = "关联供应商")
    @Dict(dictTable = "scmpartner",dicText = "partnername",dicCode = "id")
    private java.lang.String mainpartnerid;
	/**固定电话*/
	@Excel(name = "固定电话", width = 15)
    @ApiModelProperty(value = "固定电话")
    private java.lang.String fixedphone;
	/**移动电话*/
	@Excel(name = "移动电话", width = 15)
    @ApiModelProperty(value = "移动电话")
    private java.lang.String mobilephone;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
    private java.lang.String email;
	/**打印*/
	@Excel(name = "打印", width = 15)
    @ApiModelProperty(value = "打印")
    private java.lang.String fax;
	/**地址*/
	@Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private java.lang.String address;
}
