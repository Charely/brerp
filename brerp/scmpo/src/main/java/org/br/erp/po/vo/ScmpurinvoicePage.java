package org.br.erp.po.vo;

import java.util.List;
import org.br.erp.po.entity.Scmpurinvoice;
import org.br.erp.po.entity.Scmpurinvoiceitem;
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
 * @Description: 采购发票
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
@Data
@ApiModel(value="scmpurinvoicePage对象", description="采购发票")
public class ScmpurinvoicePage {


	@Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
	private String companyid;


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
	/**采购组织*/
	@Excel(name = "采购组织", width = 15)
	@ApiModelProperty(value = "采购组织")
    private java.lang.String purorgid;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
	@ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15)
	@ApiModelProperty(value = "单据日期")
    private java.lang.String billdate;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
	@ApiModelProperty(value = "供应商")
    private java.lang.String vendorid;
	/**采购部门*/
	@Excel(name = "采购部门", width = 15)
	@ApiModelProperty(value = "采购部门")
    private java.lang.String deptid;
	/**采购人员*/
	@Excel(name = "采购人员", width = 15)
	@ApiModelProperty(value = "采购人员")
    private java.lang.String emptid;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	
	@ExcelCollection(name="采购发票分录")
	@ApiModelProperty(value = "采购发票分录")
	private List<Scmpurinvoiceitem> scmpurinvoiceitemList;
	
}
