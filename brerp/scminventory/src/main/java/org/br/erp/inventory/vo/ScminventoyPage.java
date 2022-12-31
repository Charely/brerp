package org.br.erp.inventory.vo;

import java.util.List;

import org.br.erp.inventory.entity.Scminventoryitem;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 库存单据
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
@Data
@ApiModel(value="scminventoyPage对象", description="库存单据")
public class ScminventoyPage {

	/**主键*/
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
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
	@ApiModelProperty(value = "单据类型")
    private java.lang.String inventorytype;
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
    private java.lang.String invdepartid;
	/**保管员*/
	@Excel(name = "保管员", width = 15)
	@ApiModelProperty(value = "保管员")
    private java.lang.String invemployid;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
	@ApiModelProperty(value = "供应商")
    private java.lang.String vendorid;
	/**客户*/
	@Excel(name = "客户", width = 15)
	@ApiModelProperty(value = "客户")
    private java.lang.String customerid;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String remark;

	private String warehouseid;

	private String isred;
	
	@ExcelCollection(name="库存单据分录")
	@ApiModelProperty(value = "库存单据分录")
	private List<Scminventoryitem> scminventoyitemList;
	
}
