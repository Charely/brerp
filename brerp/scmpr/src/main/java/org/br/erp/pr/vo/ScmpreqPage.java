package org.br.erp.pr.vo;

import lombok.Data;
import org.br.erp.pr.entity.Scmpreqitem;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Description: 采购申请
 * @Author: jeecg-boot
 * @Date:   2022-07-27
 * @Version: V1.0
 */
@Data
@ApiModel(value="scmpreqPage对象", description="采购申请")
public class ScmpreqPage {

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
	/**申请编号*/
	@Excel(name = "申请编号", width = 15)
	@ApiModelProperty(value = "申请编号")
    private java.lang.String preqcode;

	private String billdate;

	private String companyid;
	/**申请部门*/
	@Excel(name = "申请部门", width = 15)
	@ApiModelProperty(value = "申请部门")
	@Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private java.lang.String preqdept;
	/**申请人*/
	@Excel(name = "申请人", width = 15)
	@ApiModelProperty(value = "申请人")
    private java.lang.String preqemp;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	/**申请总数量*/
	@Excel(name = "申请总数量", width = 15)
	@ApiModelProperty(value = "申请总数量")
    private java.math.BigDecimal sumqty;
	/**申请总金额*/
	@Excel(name = "申请总金额", width = 15)
	@ApiModelProperty(value = "申请总金额")
    private java.math.BigDecimal sumvalue;
	
	@ExcelCollection(name="采购申请表体")
	@ApiModelProperty(value = "采购申请表体")
	private List<Scmpreqitem> scmpreqitemList;
	
}
