package org.br.erp.inventory.vo;

import java.util.List;
import org.br.erp.inventory.entity.Scmtransferbill;
import org.br.erp.inventory.entity.Scmtransferbillitem;
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
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2022-11-07
 * @Version: V1.0
 */
@Data
@ApiModel(value="scmtransferbillPage对象", description="移库单")
public class ScmtransferbillPage {

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
	/**库存组织*/
	@Excel(name = "库存组织", width = 15)
	@ApiModelProperty(value = "库存组织")
    private java.lang.String imorgid;
	/**移出仓库*/
	@Excel(name = "移出仓库", width = 15)
	@ApiModelProperty(value = "移出仓库")
    private java.lang.String fromwarehouseid;
	/**移入仓库*/
	@Excel(name = "移入仓库", width = 15)
	@ApiModelProperty(value = "移入仓库")
    private java.lang.String towarehouseid;
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

	private String companyid;
	private String billcode;
	private String billdate;
	
	@ExcelCollection(name="移库单分录")
	@ApiModelProperty(value = "移库单分录")
	private List<Scmtransferbillitem> scmtransferbillitemList;
	
}
