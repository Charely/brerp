package org.br.erp.po.vo;

import java.util.List;
import lombok.Data;
import org.br.erp.po.entity.Scmpoitem;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购订单
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
@Data
@ApiModel(value="scmpoPage对象", description="采购订单")
public class ScmpoPage {

	/**主键*/

	@Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
	private String companyid;


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
	/**采购日期*/
	@Excel(name = "采购日期", width = 15)
	@ApiModelProperty(value = "采购日期")
    private String podate;
	/**采购部门*/
	@Excel(name = "采购部门", width = 15)
	@ApiModelProperty(value = "采购部门")
    private String podeptid;
	/**采购人员*/
	@Excel(name = "采购人员", width = 15)
	@ApiModelProperty(value = "采购人员")
    private String poemptid;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
	@ApiModelProperty(value = "供应商")
    private String vendorid;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;

	@ApiModelProperty(value = "采购组织")
	private String purorgid;

	private String isred;
	
	@ExcelCollection(name="采购订单分录")
	@ApiModelProperty(value = "采购订单分录")
	private List<Scmpoitem> scmpoitemList;
	
}
