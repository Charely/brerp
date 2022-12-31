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
 * @Description: 库存盘点单
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@ApiModel(value="scminventorycheck对象", description="库存盘点单")
@Data
@TableName("scminventorycheck")
@Entity
public class Scminventorycheck implements Serializable {
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
	/**公司*/
	@Excel(name = "公司", width = 15)
    @ApiModelProperty(value = "公司")
    @Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
    private java.lang.String companyid;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15)
    @ApiModelProperty(value = "单据日期")
    private java.lang.String billdate;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    @Dict(dictTable = "warehouse",dicCode = "id",dicText = "name")
    private java.lang.String warehouseid;
	/**盘点部门*/
	@Excel(name = "盘点部门", width = 15)
    @ApiModelProperty(value = "盘点部门")
    @Dict(dictTable = "sys_depart",dicCode = "id",dicText = "depart_name")
    private java.lang.String deptid;
	/**盘点人员*/
	@Excel(name = "盘点人员", width = 15)
    @ApiModelProperty(value = "盘点人员")
    @Dict(dictTable = "sys_user",dicText = "realname",dicCode = "id")
    private java.lang.String emptid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;

    @ColumnDefault("0")
    //是否已盘点确认
    private String checkflag;

    @ColumnDefault("0")
    private String status;
}
