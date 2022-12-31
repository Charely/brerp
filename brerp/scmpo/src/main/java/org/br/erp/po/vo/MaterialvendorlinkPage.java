package org.br.erp.po.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.br.erp.po.entity.Materialvendorlinkitem;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 货源清单
 * @Author: jeecg-boot
 * @Date:   2022-08-07
 * @Version: V1.0
 */
@Data
@ApiModel(value="materialvendorlinkPage对象", description="货源清单")
public class MaterialvendorlinkPage {

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

	@Dict(dictTable = "sys_depart",dicText = "depart_name",dicCode = "id")
	private String companyid;

	/**清单编码*/
	@Excel(name = "清单编码", width = 15)
	@ApiModelProperty(value = "清单编码")
    private String code;
	/**清单名称*/
	@Excel(name = "清单名称", width = 15)
	@ApiModelProperty(value = "清单名称")
    private String name;
	/**供应商*/
	@Excel(name = "物料", width = 15)
	@ApiModelProperty(value = "物料")
    private String materialid;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;
	/**有效*/
	@Excel(name = "有效", width = 15)
	@ApiModelProperty(value = "有效")
    private String canenable;
	/**供应商编码*/
	@Excel(name = "物料编号", width = 15)
	@ApiModelProperty(value = "物料编号")
    private String materialcode;
	/**供应商名称*/
	@Excel(name = "物料名称", width = 15)
	@ApiModelProperty(value = "物料名称")
    private String matreialname;
	/**有效期从*/
	@Excel(name = "有效期从", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "有效期从")
    private Date fromdate;
	/**有效期止*/
	@Excel(name = "有效期止", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "有效期止")
    private Date enddate;
	
	@ExcelCollection(name="货源清单分录")
	@ApiModelProperty(value = "货源清单分录")
	private List<Materialvendorlinkitem> materialvendorlinkitemList;
	
}
