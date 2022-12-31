package org.br.erp.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("onl_cgform_field")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="onl_cgform_field", description="产品表")
public class onlcgformfield {
    private String id;
    private String dbfieldname;//列key
    private String cgformheadid;
    private String dbfieldtxt;//显示名称
    private String fieldshowtype;//显示格式
    private String fieldlength;//显示长度
    private String isshowlist;//是否显示
    private String ordernum;//显示顺序
}
