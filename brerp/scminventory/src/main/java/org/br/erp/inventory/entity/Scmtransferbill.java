package org.br.erp.inventory.entity;

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
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2022-11-07
 * @Version: V1.0
 */
@ApiModel(value="scmtransferbill对象", description="移库单")
@Data
@TableName("scmtransferbill")
@Entity
public class Scmtransferbill implements Serializable {
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
	/**库存组织*/
	@Excel(name = "库存组织", width = 15)
    @ApiModelProperty(value = "库存组织")
    private java.lang.String imorgid;


    @Dict(dictTable = "sys_depart",dicCode = "id",dicText = "depart_name")
    private String companyid;

	/**移出仓库*/
	@Excel(name = "移出仓库", width = 15)
    @ApiModelProperty(value = "移出仓库")
    @Dict(dictTable = "warehouse",dicText = "name",dicCode = "id")
    private java.lang.String fromwarehouseid;
	/**移入仓库*/
	@Excel(name = "移入仓库", width = 15)
    @ApiModelProperty(value = "移入仓库")
    @Dict(dictTable = "warehouse",dicText = "name",dicCode = "id")
    private java.lang.String towarehouseid;

    private String billcode;

    private String billdate;
	/**库存部门*/
	@Excel(name = "库存部门", width = 15)
    @ApiModelProperty(value = "库存部门")
    private java.lang.String transferdeptid;
	/**库存人员*/
	@Excel(name = "库存人员", width = 15)
    @ApiModelProperty(value = "库存人员")
    private java.lang.String transferemptid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;

    @ColumnDefault("0")
    private String status;

    @ColumnDefault("0")
    private String transferflag;
}
