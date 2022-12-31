package org.br.erp.im.vo;

import java.util.List;
import org.br.erp.im.entity.Scmiminvoucher;
import org.br.erp.im.entity.Scmiminvoucheritem;
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
 * @Description: 入库凭证
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@Data
@ApiModel(value="scmiminvoucherPage对象", description="入库凭证")
public class ScmiminvoucherPage {

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
	/**业务日期*/
	@Excel(name = "业务日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "业务日期")
    private java.util.Date billdate;
	/**公司*/
	@Excel(name = "公司", width = 15)
	@ApiModelProperty(value = "公司")
    private java.lang.String companyid;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
	@ApiModelProperty(value = "单据编号")
    private java.lang.String billcode;
	/**部门*/
	@Excel(name = "部门", width = 15)
	@ApiModelProperty(value = "部门")
    private java.lang.String vdeptid;
	/**人员*/
	@Excel(name = "人员", width = 15)
	@ApiModelProperty(value = "人员")
    private java.lang.String vemptid;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String remarks;
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
	@ApiModelProperty(value = "单据类型")
    private java.lang.String fromtype;

	private String status;

	private String warehouseid;

	@ExcelCollection(name="入库凭证分录")
	@ApiModelProperty(value = "入库凭证分录")
	private List<Scmiminvoucheritem> scmiminvoucheritemList;

}
