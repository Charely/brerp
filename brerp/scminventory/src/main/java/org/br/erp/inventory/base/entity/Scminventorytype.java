package org.br.erp.inventory.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 库存单据类型
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
@Data
@TableName("scminventorytype")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Scminventorytype", description="库存单据类型")
@Entity
public class Scminventorytype implements Serializable {
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
	/**类型编号*/
	@Excel(name = "类型编号", width = 15)
    @ApiModelProperty(value = "类型编号")
    private java.lang.String typecode;
	/**类型名称*/
	@Excel(name = "类型名称", width = 15)
    @ApiModelProperty(value = "类型名称")
    private java.lang.String typename;
	/**是否系统*/
	@Excel(name = "是否系统", width = 15)
    @ApiModelProperty(value = "是否系统")
    private java.lang.String issys;
	/**系统类型id*/
	@Excel(name = "系统类型id", width = 15)
    @ApiModelProperty(value = "系统类型id")
    private java.lang.String sysid;


    /*
    入库方向
     */
    @ColumnDefault("false")
    private String instock;

    /*
    出库
     */
    @ColumnDefault("false")
    private String outstock;
}
