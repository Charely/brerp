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
import org.hibernate.annotations.ColumnDefault;
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
 * @Description: 往来单位
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
@Data
@TableName("scmpartner")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmpartner对象", description="往来单位")
@Entity
public class Scmpartner implements Serializable {
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
	/**编码*/
	@Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
    private java.lang.String partnercode;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String partnername;
	/**简称*/
	@Excel(name = "简称", width = 15)
    @ApiModelProperty(value = "简称")
    private java.lang.String partnerjc;
	/**往来性质*/
	@Excel(name = "往来性质", width = 15, dicCode = "wlxz")
	@Dict(dicCode = "wlxz")
    @ApiModelProperty(value = "往来性质")
    private java.lang.String partnertype;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    @Dict(dictTable = "scmcontact",dicCode = "id",dicText = "contactname")
    private java.lang.String partnerlinkman;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String partnerlinktel;
	/**联系地址*/
	@Excel(name = "联系地址", width = 15)
    @ApiModelProperty(value = "联系地址")
    private java.lang.String partneraddress;

    /*是否是运输单位*/
    private String iscarrier;

    private String isinternalpartner;
}
