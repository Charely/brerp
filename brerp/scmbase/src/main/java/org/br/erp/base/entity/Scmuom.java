package org.br.erp.base.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 计量单位定义
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
@Data
@TableName("scmuom")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmuom对象", description="计量单位定义")
@Entity
public class Scmuom implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    @Id
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
	/**计量单位编号*/
	@Excel(name = "计量单位编号", width = 15)
    @ApiModelProperty(value = "计量单位编号")
    private java.lang.String uomcode;
	/**计量单位名称*/
	@Excel(name = "计量单位名称", width = 15)
    @ApiModelProperty(value = "计量单位名称")
    private java.lang.String uomname;
	/**计量单位精度*/
	@Excel(name = "计量单位精度", width = 15)
    @ApiModelProperty(value = "计量单位精度")
    private java.math.BigDecimal uomrate;
}
