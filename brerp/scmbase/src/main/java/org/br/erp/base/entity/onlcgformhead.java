package org.br.erp.base.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@TableName("onl_cgform_head")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="onl_cgform_head", description="产品表")
public class onlcgformhead {
    private String id;
    private String table_name;
}
