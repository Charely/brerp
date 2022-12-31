package org.br.erp.po.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 收料申请单
 * @Author: jeecg-boot
 * @Date:   2022-10-18
 * @Version: V1.0
 */
@ApiModel(value="scmmatreceiptreq对象", description="收料申请单")
@Data
@TableName("scmmatreceiptreq")
@Entity
public class Scmmatreceiptreq implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    @Id
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;

    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private String companyid;

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
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**业务日期*/
	@Excel(name = "业务日期", width = 15)
    @ApiModelProperty(value = "业务日期")
    private java.lang.String billdate;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    @Dict(dictTable = "scmpartner",dicText = "partnername",dicCode = "id")
    private java.lang.String vendorid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;

    @ColumnDefault("0")
    private String status;

    @ColumnDefault("0")
    private String receiptflag;
}
