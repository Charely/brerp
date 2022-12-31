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
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description: 条码表
 * @Author: jeecg-boot
 * @Date:   2022-10-20
 * @Version: V1.0
 */
@Data
@TableName("scmbarcode")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="scmbarcode对象", description="条码表")
@Entity
public class Scmbarcode implements Serializable {
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
	/**供应商id*/
	@Excel(name = "供应商id", width = 15)
    @ApiModelProperty(value = "供应商id")
    private java.lang.String vendorid;
	/**barcode*/
	@Excel(name = "barcode", width = 15)
    private transient java.lang.String barcodeString;


    public byte[] barcode;

    public byte[] getBarcode(){
        if(barcodeString==null){
            return null;
        }
        try {
            return barcodeString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBarcodeString(){
        if(barcode==null || barcode.length==0){
            return "";
        }
        try {
            return new String(barcode,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
	/**billitemid*/
	@Excel(name = "billitemid", width = 15)
    @ApiModelProperty(value = "billitemid")
    private java.lang.String billitemid;
	/**billid*/
	@Excel(name = "billid", width = 15)
    @ApiModelProperty(value = "billid")
    private java.lang.String billid;

    private String bartext;

    @ColumnDefault("0")
    private String isvalid;
}
