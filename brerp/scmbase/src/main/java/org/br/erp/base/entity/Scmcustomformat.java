package org.br.erp.base.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 自定义格式表
 * @Author: jeecg-boot
 * @Date:   2022-10-28
 * @Version: V1.0
 */
@ApiModel(value="scmcustomformat对象", description="自定义格式表")
@Data
@TableName("scmcustomformat")
@Entity
public class Scmcustomformat implements Serializable {
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
	/**系统配置*/
	@Excel(name = "系统配置", width = 15)
    @ApiModelProperty(value = "系统配置")
    private java.lang.String issys;
	/**格式编号*/
	@Excel(name = "格式编号", width = 15)
    @ApiModelProperty(value = "格式编号")
    private java.lang.String formatcode;
	/**格式名称*/
	@Excel(name = "格式名称", width = 15)
    @ApiModelProperty(value = "格式名称")
    private java.lang.String formatname;
	/**业务对象*/
	@Excel(name = "业务对象", width = 15)
    @ApiModelProperty(value = "业务对象")
    private java.lang.String objectid;
}
