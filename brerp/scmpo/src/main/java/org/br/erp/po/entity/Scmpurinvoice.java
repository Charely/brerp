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
 * @Description: 采购发票
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
@ApiModel(value="scmpurinvoice对象", description="采购发票")
@Data
@TableName("scmpurinvoice")
@Entity
public class Scmpurinvoice implements Serializable {
    private static final long serialVersionUID = 1L;


    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private String companyid;


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
	/**采购组织*/
	@Excel(name = "采购组织", width = 15)
    @ApiModelProperty(value = "采购组织")
    private java.lang.String purorgid;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15)
    @ApiModelProperty(value = "单据日期")
    private java.lang.String billdate;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    @Dict(dictTable = "scmpartner",dicCode = "id",dicText = "partnername")
    private java.lang.String vendorid;
	/**采购部门*/
	@Excel(name = "采购部门", width = 15)
    @ApiModelProperty(value = "采购部门")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private java.lang.String deptid;
	/**采购人员*/
	@Excel(name = "采购人员", width = 15)
    @ApiModelProperty(value = "采购人员")
    @Dict(dictTable = "sys_user",dicText = "realname",dicCode = "id")
    private java.lang.String emptid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;

    @ColumnDefault("0")
    private String status;
}
