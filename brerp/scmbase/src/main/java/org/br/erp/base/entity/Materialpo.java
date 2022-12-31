package org.br.erp.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("materialpo")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="material对象", description="产品表")
@Entity
public class Materialpo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private BigDecimal poprice;

    private String pouomid;
    private String pocurrencyid;
    private Date lastpodate;
    private BigDecimal lastpoprice;

    private String parentid;

    private String defvendorid;

    /*固定采购周期*/
    private Integer fixedPoDate;

    /*日供量*/
    private BigDecimal fixedGl;

    /*最小起订量*/
    private BigDecimal minPoQty;
    private String isqtcontrol;

}
