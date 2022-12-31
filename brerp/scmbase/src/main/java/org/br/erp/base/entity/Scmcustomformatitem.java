package org.br.erp.base.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 自定义格式表分录
 * @Author: jeecg-boot
 * @Date:   2022-10-28
 * @Version: V1.0
 */
@ApiModel(value="scmcustomformatitem对象", description="自定义格式表分录")
@Data
@TableName("scmcustomformatitem")
@Entity
public class Scmcustomformatitem implements Serializable {
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
	/**parentid*/
    @ApiModelProperty(value = "parentid")
    private java.lang.String parentid;
	/**控件列id*/
	@Excel(name = "控件列id", width = 15)
    @ApiModelProperty(value = "控件列id")
    private java.lang.String colid;

    private String colname;

	/**控件列类型*/
	@Excel(name = "控件列类型", width = 15)
    @ApiModelProperty(value = "控件列类型")
    private java.lang.String coltype;
	/**控件列宽度*/
	@Excel(name = "控件列宽度", width = 15)
    @ApiModelProperty(value = "控件列宽度")
    private java.lang.String colwidth;
	/**控件列是否可见*/
	@Excel(name = "控件列是否可见", width = 15)
    @ApiModelProperty(value = "控件列是否可见")
    private java.lang.String colisvisable;
	/**控件列顺序*/
	@Excel(name = "控件列顺序", width = 15)
    @ApiModelProperty(value = "控件列顺序")
    private java.lang.Integer colorder;
	/**控件列字典*/
	@Excel(name = "控件列字典", width = 15)
    @ApiModelProperty(value = "控件列字典")
    private java.lang.String coldictcode;

    /**
     * 是否只读
     */
    private String onlyread;
}
