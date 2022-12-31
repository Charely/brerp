package org.br.erp.inventory.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 库存单据分录
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
@ApiModel(value="scminventoyitem对象", description="库存单据分录")
@Data
@TableName("scminventoryitem")
@Entity
public class Scminventoryitem implements Serializable {
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
	/**行号*/
	@Excel(name = "行号", width = 15)
    @ApiModelProperty(value = "行号")
    private java.lang.String itemcode;

    /*仓库id*/
    private String warehouseid;

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
	/**批号*/
	@Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号")
    private java.lang.String batchid;

    private String batchcode;

    /*供应商原始批次*/
    private String vendorbatchcode;

    /*批次生产日期*/
    private Date batchstartdate;
    private Date batchenddate;

    /*货位编号*/
    private String stocklocationid;

    private String stocklocationname;

	/**入库数量*/
	@Excel(name = "入库数量", width = 15)
    @ApiModelProperty(value = "入库数量")
    private java.lang.String qty;
    private String uomid;

	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private BigDecimal taxinprice;

    private BigDecimal taxrate;

    private BigDecimal taxexprice;

    private BigDecimal taxexvalue;
	/**总价*/
	@Excel(name = "总价", width = 15)
    @ApiModelProperty(value = "总价")
    private BigDecimal taxinvalue;
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private java.lang.String parentid;
    private String fromid;
    private String fromitemid;
    /*库存状态id*/
    private String stocktypeid;
    /*库存类型id*/
    private String inventorykindid;

    private String instock;
    private String outstock;

    @ColumnDefault("0")
    private String invoiceflag;
    @ColumnDefault("0")
    private String invoiceqty;
}
