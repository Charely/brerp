package org.br.erp.mv.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 存货账本
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
@Data
@TableName("scmmvbalance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmmvbalance对象", description="存货账本")
public class Scmmvbalance implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
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
    private java.lang.String companyid;
	/**账本期间*/
	@Excel(name = "账本期间", width = 15)
    @ApiModelProperty(value = "账本期间")
    private java.lang.String billdate;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private java.lang.String materialid;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private java.lang.String warehosueid;
	/**货位*/
	@Excel(name = "货位", width = 15)
    @ApiModelProperty(value = "货位")
    private java.lang.String stocklocationid;
	/**批号*/
	@Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号")
    private java.lang.String batchid;
	/**年初数量*/
	@Excel(name = "年初数量", width = 15)
    @ApiModelProperty(value = "年初数量")
    private java.math.BigDecimal yearqty;
	/**月初数量*/
	@Excel(name = "月初数量", width = 15)
    @ApiModelProperty(value = "月初数量")
    private java.math.BigDecimal monthqty;
	/**月初存货价值*/
	@Excel(name = "月初存货价值", width = 15)
    @ApiModelProperty(value = "月初存货价值")
    private java.math.BigDecimal monthvalue;
	/**月入库数量*/
	@Excel(name = "月入库数量", width = 15)
    @ApiModelProperty(value = "月入库数量")
    private java.math.BigDecimal curinqty;
	/**月出库数量*/
	@Excel(name = "月出库数量", width = 15)
    @ApiModelProperty(value = "月出库数量")
    private java.math.BigDecimal curoutqty;
	/**账本余量*/
	@Excel(name = "账本余量", width = 15)
    @ApiModelProperty(value = "账本余量")
    private java.math.BigDecimal balanceqty;
	/**月存货价值*/
	@Excel(name = "月存货价值", width = 15)
    @ApiModelProperty(value = "月存货价值")
    private java.math.BigDecimal balancevalue;
	/**月存货单价*/
	@Excel(name = "月存货单价", width = 15)
    @ApiModelProperty(value = "月存货单价")
    private java.math.BigDecimal balanceprice;
	/**库存类型*/
	@Excel(name = "库存类型", width = 15)
    @ApiModelProperty(value = "库存类型")
    private java.lang.String inventorykindid;
}
