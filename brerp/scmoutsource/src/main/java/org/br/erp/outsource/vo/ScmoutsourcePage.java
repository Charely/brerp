package org.br.erp.outsource.vo;

import java.util.List;
import org.br.erp.outsource.entity.Scmoutsource;
import org.br.erp.outsource.entity.Scmoutsourceitem;
import lombok.Data;
import org.br.erp.outsource.entity.Scmoutsourceitembom;
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
 * @Description: 委外订单
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
@Data
@ApiModel(value="scmoutsourcePage对象", description="委外订单")
public class ScmoutsourcePage {

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
	/**委外类型*/
	@Excel(name = "委外类型", width = 15)
	@ApiModelProperty(value = "委外类型")
    private java.lang.String outsourcetypeid;
	/**业务日期*/
	@Excel(name = "业务日期", width = 15)
	@ApiModelProperty(value = "业务日期")
    private java.lang.String billdate;

	private String deptid;

	private String emptid;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
	@ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**委外供应商*/
	@Excel(name = "委外供应商", width = 15)
	@ApiModelProperty(value = "委外供应商")
    private java.lang.String vendorid;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String remarks;

	@ExcelCollection(name="委外订单分录")
	@ApiModelProperty(value = "委外订单分录")
	private List<Scmoutsourceitem> scmoutsourceitemList;

	private List<Scmoutsourceitembom> scmoutsourceitembomList;

}
