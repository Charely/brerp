package org.br.erp.inventory.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 盘点单分录
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@ApiModel(value="scminventorycheckitem对象", description="盘点单分录")
@Data
@TableName("scminventorycheckitem")
@Entity
public class Scminventorycheckitem implements Serializable {
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
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private java.lang.String materialid;
	/**物料编号*/
	@Excel(name = "物料编号", width = 15)
    @ApiModelProperty(value = "物料编号")
    private java.lang.String materialcode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private java.lang.String materialname;
	/**计量单位*/
	@Excel(name = "计量单位", width = 15)
    @ApiModelProperty(value = "计量单位")
    private java.lang.String uomid;
	/**货位*/
	@Excel(name = "货位", width = 15)
    @ApiModelProperty(value = "货位")
    private java.lang.String stocklocationid;
	/**批号*/
	@Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号")
    private java.lang.String batchid;
	/**账本余额*/
	@Excel(name = "账本余额", width = 15)
    @ApiModelProperty(value = "账本余额")
    @Column(precision = 28,scale = 8)
    private java.math.BigDecimal budgetqty;
	/**实际余额*/
	@Excel(name = "实际余额", width = 15)
    @ApiModelProperty(value = "实际余额")
    @Column(precision = 28,scale = 8)
    private BigDecimal realityqty;
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private java.lang.String parentid;


    private String stocklocationname;


    /*库存状态id*/
    private String stocktypeid;
    /*库存类型id*/
    private String inventorykindid;
}
