package org.br.erp.im.entity;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 入库凭证
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@ApiModel(value="scmiminvoucher对象", description="入库凭证")
@Data
@TableName("scmiminvoucher")
@Entity
public class Scmiminvoucher implements Serializable {
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
	/**业务日期*/
	@Excel(name = "业务日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "业务日期")
    private String  billdate;
	/**公司*/
	@Excel(name = "公司", width = 15)
    @ApiModelProperty(value = "公司")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private java.lang.String companyid;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**部门*/
	@Excel(name = "部门", width = 15)
    @ApiModelProperty(value = "部门")
    @Dict(dicCode = "id",dicText = "depart_name",dictTable = "sys_depart")
    private java.lang.String vdeptid;
	/**人员*/
	@Excel(name = "人员", width = 15)
    @ApiModelProperty(value = "人员")
    @Dict(dictTable = "sys_user",dicText = "realname",dicCode = "id")
    private java.lang.String vemptid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
    @ApiModelProperty(value = "单据类型")
    private java.lang.String fromtype;

    /**
     * 是否暂估
     * 0暂估
     * 1不暂估
     */
    @ColumnDefault("0")
    @Column(length = 1)
    private String iszg;

    /**
     * 0代表入库凭证
     * 1代表出库凭证
     */
    @ColumnDefault("0")
    private String voucherkind;

    /**
     * 成本确认标识
     * 0未确认成本
     * 1已确认成本
     */
    @ColumnDefault("0")
    private String vouncherFlag;

    @Column(length = 32)
    private String warehouseid;

    /**
     * 0制单
     * 1审批中
     * 2审批通过
     */
    @ColumnDefault("0")
    @Column(length = 1)
    private String status;

    @ColumnDefault("0")
    @Column(length = 1)
    private String tallyFlag;

}
