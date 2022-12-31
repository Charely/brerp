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
import org.jeecg.common.aspect.annotation.CustomCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 采购订单
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
@ApiModel(value="scmpo对象", description="采购订单")
@Data
@TableName("scmpo")
@Entity
@CustomCode(objectCode = "scmpo")
public class Scmpo implements Serializable {
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

    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private String companyid;

	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;

    @ApiModelProperty(value = "单据编号")
    private String billcode;
	/**采购日期*/
	@Excel(name = "采购日期", width = 15)
    @ApiModelProperty(value = "采购日期")
    private String podate;
	/**采购部门*/
	@Excel(name = "采购部门", width = 15)
    @ApiModelProperty(value = "采购部门")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    private String podeptid;
	/**采购人员*/
	@Excel(name = "采购人员", width = 15)
    @ApiModelProperty(value = "采购人员")
    @Dict(dictTable = "sys_user",dicText = "realname",dicCode = "id")
    private String poemptid;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    @Dict(dictTable = "scmpartner", dicText = "partnername", dicCode = "id")
    private String vendorid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;

    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    @ApiModelProperty(value = "采购组织")
    private String purorgid;

    @ApiModelProperty(value = "单据状态")
    @ColumnDefault("0")
    private String status;
    /*入库标志*/
    @ColumnDefault("0")
    private String receiptflag;
    //付款标志
    @ColumnDefault("0")
    private String paymentflag;
    //结算标志
    @ColumnDefault("0")
    private String invoiceflag;
    //退货标志
    @ColumnDefault("0")
    private String returnflag;
    //收货申请标志
    @Column(columnDefinition="varchar(255) default '0'")
    private String receiptreqflag;

    @ColumnDefault("0")
    private String isred;
}
