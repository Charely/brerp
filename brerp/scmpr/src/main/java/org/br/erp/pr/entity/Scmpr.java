package org.br.erp.pr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.BillCodeRule;
import org.jeecg.common.aspect.annotation.CustomCode;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 采购计划
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
@ApiModel(value="scmpr对象", description="采购计划")
@Data
@TableName("scmpr")
@Entity
@CustomCode(objectCode = "scmpr")
public class Scmpr implements Serializable {
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
    /*所属公司*/
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private String companyid;
	/**计划编号*/
	@Excel(name = "计划编号", width = 15)
    @ApiModelProperty(value = "计划编号")
    @BillCodeRule(billcodeRule = "scmpr")
    private String prcode;
	/**采购部门*/
	@Excel(name = "采购部门", width = 15)
    @ApiModelProperty(value = "采购部门")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private String prdept;
	/**采购人员*/
	@Excel(name = "采购人员", width = 15)
    @ApiModelProperty(value = "采购人员")
    @Dict(dictTable = "sys_user",dicText = "realname",dicCode = "id")
    private String prempid;
	/**计划备注*/
	@Excel(name = "计划备注", width = 15)
    @ApiModelProperty(value = "计划备注")
    private String remark;
	/**计划日期*/
	@Excel(name = "计划日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划日期")
    private Date prdate;

    @ApiModelProperty(value = "单据状态")
    @GeneratedValue
    private String status;

    private String ispo;

    /**
     * 采购组织
     */
    @Dict(dictTable = "sys_depart",dicCode = "id",dicText = "depart_name")
    private String purorg;
}
