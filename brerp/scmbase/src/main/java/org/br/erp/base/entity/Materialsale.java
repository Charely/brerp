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

@Data
@TableName("materialsale")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="material对象", description="产品表")
@Entity
public class Materialsale implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String parentid;

    private BigDecimal saleprice;

    private String saleuomid;

    private String salecurrencyid;

}
