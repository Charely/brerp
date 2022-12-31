package org.br.erp.inventory.base.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.hpsf.Decimal;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 库存账本表
 * @Author: jeecg-boot
 * @Date:   2022-10-24
 * @Version: V1.0
 */
@Data
@TableName("scmimbalance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmimbalance对象", description="库存账本表")
@Entity
public class Scmimbalance implements Serializable {
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
    /*供应商*/
    private String vendorid;
    /*期间*/
    private String balancedate;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private java.lang.String materialid;
	/**批次*/
	@Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    @Dict(dictTable = "scmbatch",dicText = "batchcode",dicCode = "id")
    private java.lang.String batchid;
	/**组织*/
	@Excel(name = "组织", width = 15)
    @ApiModelProperty(value = "组织")
    private java.lang.String orgid;
    /*所属公司*/
    private String companyid;
    /*库存状态*/
    private String stocktypeid;
    /*库存类型*/
    private String inventorykindid;

    private String projectid;

    private String wbsid;

    /*货位*/
    private String stocklocationid;

	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private java.lang.String warehouseid;

	/**年初数量*/
	@Excel(name = "年初数量", width = 15)
    @ApiModelProperty(value = "年初数量")
    private BigDecimal beginyearqty;
	/**月初数量*/
	@Excel(name = "月初数量", width = 15)
    @ApiModelProperty(value = "月初数量")
    private BigDecimal beginmonthqty;
	/**年末数量*/
	@Excel(name = "年末数量", width = 15)
    @ApiModelProperty(value = "年末数量")
    private BigDecimal endyearqty;
//	/**月末数量*/
//	@Excel(name = "月末数量", width = 15)
//    @ApiModelProperty(value = "月末数量")
//    private BigDecimal endmonthqty;
	/**本次发生数量*/
	@Excel(name = "本次发生数量", width = 15)
    @ApiModelProperty(value = "本月发生入库数量")
    private BigDecimal inamount;

    @ApiModelProperty(value = "本月发生出库数量")
    private BigDecimal outamount;

    private BigDecimal amount;
}
