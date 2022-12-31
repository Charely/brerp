package org.br.erp.pr.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.br.erp.pr.entity.Scmpritem;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采购计划
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
@Data
@ApiModel(value="scmprPage对象", description="采购计划")
public class ScmprPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**计划编号*/
	@Excel(name = "计划编号", width = 15)
	@ApiModelProperty(value = "计划编号")
    private String prcode;
	/**采购部门*/
	@Excel(name = "采购部门", width = 15)
	@ApiModelProperty(value = "采购部门")
    private String prdept;
	/**采购人员*/
	@Excel(name = "采购人员", width = 15)
	@ApiModelProperty(value = "采购人员")
    private String prempid;
	/**计划备注*/
	@Excel(name = "计划备注", width = 15)
	@ApiModelProperty(value = "计划备注")
    private String remark;

	private String companyid;
	/**计划日期*/
	@Excel(name = "计划日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "计划日期")
    private Date prdate;

	@ExcelCollection(name="采购计划分录")
	@ApiModelProperty(value = "采购计划分录")
	private List<Scmpritem> scmpritemList;

	@ApiModelProperty(value ="自定义字段值")
	private Map<Object, Object> customdata;

	/*采购组织*/
	private String purorg;

}
