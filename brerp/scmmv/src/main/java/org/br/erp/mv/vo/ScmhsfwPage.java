package org.br.erp.mv.vo;

import java.util.List;
import org.br.erp.mv.entity.Scmhsfw;
import org.br.erp.mv.entity.Scmhsfwitem;
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
 * @Description: 存货核算范围
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@Data
@ApiModel(value="scmhsfwPage对象", description="存货核算范围")
public class ScmhsfwPage {

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
	/**核算组织*/
	@Excel(name = "核算组织", width = 15)
	@ApiModelProperty(value = "核算组织")
    private java.lang.String companyid;
	/**核算范围编号*/
	@Excel(name = "核算范围编号", width = 15)
	@ApiModelProperty(value = "核算范围编号")
    private java.lang.String hscode;
	/**核算范围名称*/
	@Excel(name = "核算范围名称", width = 15)
	@ApiModelProperty(value = "核算范围名称")
    private java.lang.String hsname;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15)
	@ApiModelProperty(value = "是否启用")
    private java.lang.String hsisvalid;
	/**核算层次*/
	@Excel(name = "核算层次", width = 15)
	@ApiModelProperty(value = "核算层次")
    private java.lang.String hslevel;

	@ExcelCollection(name="核算范围分录")
	@ApiModelProperty(value = "核算范围分录")
	private List<Scmhsfwitem> scmhsfwitemList;

}
