package org.br.erp.inventory.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 移库单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-07
 * @Version: V1.0
 */
@ApiModel(value="scmtransferbillitem对象", description="移库单分录")
@Data
@TableName("scmtransferbillitem")
@Entity
public class Scmtransferbillitem implements Serializable {
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
	/**移库数量*/
	@Excel(name = "移库数量", width = 15)
    @ApiModelProperty(value = "移库数量")
    private java.lang.String qty;
	/**批次编号*/
	@Excel(name = "批次编号", width = 15)
    @ApiModelProperty(value = "批次编号")
    private java.lang.String batchid;
	/**账本数量*/
	@Excel(name = "账本数量", width = 15)
    @ApiModelProperty(value = "账本数量")
    private java.lang.String balanceqty;
	/**库存状态*/
	@Excel(name = "库存状态", width = 15)
    @ApiModelProperty(value = "库存状态")
    private java.lang.String stocktypeid;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private java.lang.String parentid;

    private String fromid;
    private String fromitemid;
    /*库存类型*/
    private String inventorykindid;

    private String fromstocklocationid;
    private String fromstocklocationname;
    private String tostocklocationid;
    private String tostocklocationname;
}
