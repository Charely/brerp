package org.br.erp.so.entity;

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
 * @Description: 销售发票
 * @Author: jeecg-boot
 * @Date:   2022-11-08
 * @Version: V1.0
 */
@ApiModel(value="scmsaleinvoice对象", description="销售发票")
@Data
@TableName("scmsaleinvoice")
@Entity
public class Scmsaleinvoice implements Serializable {
    private static final long serialVersionUID = 1L;

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
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "单据日期")
    private java.util.Date billdate;
	/**销售客户*/
	@Excel(name = "销售客户", width = 15)
    @ApiModelProperty(value = "销售客户")
    @Dict(dictTable = "scmpartner",dicText = "partnername",dicCode = "id")
    private java.lang.String customerid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	/**销售部门*/
	@Excel(name = "销售部门", width = 15)
    @ApiModelProperty(value = "销售部门")
    private java.lang.String departid;
	/**销售人员*/
	@Excel(name = "销售人员", width = 15)
    @ApiModelProperty(value = "销售人员")
    private java.lang.String emptid;
    @ColumnDefault("0")
    private String status;

    @Dict(dictTable = "sys_depart",dicCode = "id",dicText = "depart_name")
    private String companyid;
}
