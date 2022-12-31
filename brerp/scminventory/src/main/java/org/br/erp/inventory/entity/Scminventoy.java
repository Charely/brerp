package org.br.erp.inventory.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 库存单据
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
@ApiModel(value="scminventoy对象", description="库存单据")
@Data
@TableName("scminventoy")
@Entity
public class Scminventoy implements Serializable {
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
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
    @ApiModelProperty(value = "单据类型")
    @Dict(dictTable = "scminventorytype",dicCode = "typecode",dicText = "typename")
    private java.lang.String inventorytype;

    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private String companyid;

	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**业务日期*/
	@Excel(name = "业务日期", width = 15)
    @ApiModelProperty(value = "业务日期")
    private java.lang.String billdate;
	/**库存部门*/
	@Excel(name = "库存部门", width = 15)
    @ApiModelProperty(value = "库存部门")
    @Dict(dictTable = "sys_depart",dicCode = "id",dicText = "depart_name")
    private java.lang.String invdepartid;

    /*仓库id*/
    private String warehouseid;

	/**保管员*/
	@Excel(name = "保管员", width = 15)
    @ApiModelProperty(value = "保管员")
    @Dict(dictTable = "sys_user",dicText = "realname",dicCode = "id")
    private java.lang.String invemployid;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    @Dict(dictTable = "scmpartner",dicCode = "id",dicText = "partnername")
    private java.lang.String vendorid;
	/**客户*/
	@Excel(name = "客户", width = 15)
    @ApiModelProperty(value = "客户")
    @Dict(dictTable = "scmpartner",dicCode = "id",dicText = "partnername")
    private java.lang.String customerid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;

    @ColumnDefault("0")
    private String status;

    /**
     * 入库标识
     */
    @ColumnDefault(value = "0")
    private String inventoryflag;

    @ColumnDefault("0")
    private String invoiceflag;

    @ColumnDefault("false")
    private Boolean isred;

    /**
     * 生成存货凭证标识
     * 0未生成
     * 1已生成
     */
    @ColumnDefault("0")
    private String invouncherflag;
}
