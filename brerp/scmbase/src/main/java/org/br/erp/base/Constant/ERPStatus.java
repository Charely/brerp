package org.br.erp.base.Constant;

public enum ERPStatus {

    Create("0","制单"),
    Audit("1","审批通过"),
    Auditing("2","审批中"),
    NotPassed("3","审批不通过");

    String code;
    String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ERPStatus(String code, String value) {
    }
}
