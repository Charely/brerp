package org.br.erp.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jeecgframework.poi.excel.annotation.Excel;

public class SysForm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表单主键 */
    private Long formId;

    /** 表单名称 */
    @Excel(name = "表单名称")
    private String formName;

    /** 表单内容 */
    @Excel(name = "表单内容")
    private String formContent;

    public void setFormId(Long formId)
    {
        this.formId = formId;
    }

    public Long getFormId()
    {
        return formId;
    }
    public void setFormName(String formName)
    {
        this.formName = formName;
    }

    public String getFormName()
    {
        return formName;
    }
    public void setFormContent(String formContent)
    {
        this.formContent = formContent;
    }

    public String getFormContent()
    {
        return formContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("formId", getFormId())
                .append("formName", getFormName())
                .append("formContent", getFormContent())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .toString();
    }
}