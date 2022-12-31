package org.br.erp.inventory.vo;

import java.util.List;
import org.br.erp.inventory.entity.Scminventorycheck;
import org.br.erp.inventory.entity.Scminventorycheckitem;
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
 * @Description: 库存盘点单
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Data
@ApiModel(value="scminventorycheckPage对象", description="库存盘点单")
public class ScminventorycheckPage {

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
	/**公司*/
	@Excel(name = "公司", width = 15)
	@ApiModelProperty(value = "公司")
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
    private java.lang.String warehouseid;
	/**盘点部门*/
	@Excel(name = "盘点部门", width = 15)
	@ApiModelProperty(value = "盘点部门")
    private java.lang.String deptid;
	/**盘点人员*/
	@Excel(name = "盘点人员", width = 15)
	@ApiModelProperty(value = "盘点人员")
    private java.lang.String emptid;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String remarks;

	@ExcelCollection(name="盘点单分录")
	@ApiModelProperty(value = "盘点单分录")
	private List<Scminventorycheckitem> scminventorycheckitemList;

}
