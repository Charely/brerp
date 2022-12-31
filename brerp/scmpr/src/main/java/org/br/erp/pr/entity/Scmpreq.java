package org.br.erp.pr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 采购申请
 * @Author: jeecg-boot
 * @Date:   2022-07-27
 * @Version: V1.0
 */
@ApiModel(value="scmpreq对象", description="采购申请")
@Data
@TableName("scmpreq")
@Entity
public class Scmpreq implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    @Id
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

    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private String companyid;

	/**申请编号*/
	@Excel(name = "申请编号", width = 15)
    @ApiModelProperty(value = "申请编号")
    private String preqcode;


    private String billdate;

	/**申请部门*/
	@Excel(name = "申请部门", width = 15)
    @ApiModelProperty(value = "申请部门")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private String preqdept;
	/**申请人*/
	@Excel(name = "申请人", width = 15)
    @ApiModelProperty(value = "申请人")
    @Dict(dictTable = "sys_user",dicText = "realname",dicCode = "id")
    private String preqemp;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
	/**申请总数量*/
	@Excel(name = "申请总数量", width = 15)
    @ApiModelProperty(value = "申请总数量")
    private java.math.BigDecimal sumqty;
	/**申请总金额*/
	@Excel(name = "申请总金额", width = 15)
    @ApiModelProperty(value = "申请总金额")
    private java.math.BigDecimal sumvalue;
    @ApiModelProperty(value = "单据状态")
    private String status;
}
