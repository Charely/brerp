package org.br.erp.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@TableName("materialstock")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="material对象", description="产品表")
@Entity
public class Materialstock {

    @Id
    private String id;
    private String parentid;
    private String stockuomid;
    private String isbatch;
    private String issn;
    private String isvalidcontrol;
    private String defwarehouseid;
}
