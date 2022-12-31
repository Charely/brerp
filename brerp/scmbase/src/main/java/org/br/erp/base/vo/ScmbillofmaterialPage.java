package org.br.erp.base.vo;

import java.util.List;
import org.br.erp.base.entity.Scmbillofmaterial;
import org.br.erp.base.entity.Scmbillofmaterialitem;
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
 * @Description: 物料清单
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
@Data
@ApiModel(value="scmbillofmaterialPage对象", description="物料清单")
public class ScmbillofmaterialPage {

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
	/**清单编号*/
	@Excel(name = "清单编号", width = 15)
	@ApiModelProperty(value = "清单编号")
    private java.lang.String billcode;
	/**物料*/
	@Excel(name = "物料", width = 15)
	@ApiModelProperty(value = "物料")
    private java.lang.String materialid;
	/**版本*/
	@Excel(name = "版本", width = 15)
	@ApiModelProperty(value = "版本")
    private java.lang.Integer version;
	/**启用*/
	@Excel(name = "启用", width = 15)
	@ApiModelProperty(value = "启用")
    private java.lang.String isenable;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private java.lang.String remarks;

	private String companyid;

	@ExcelCollection(name="物料清单分录")
	@ApiModelProperty(value = "物料清单分录")
	private List<Scmbillofmaterialitem> scmbillofmaterialitemList;

}
