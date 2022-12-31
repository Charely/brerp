package org.br.erp.base.entityvo;

import java.util.ArrayList;
import java.util.List;

public class ScmobjectIdModel {

    private String key;

    public String getKey() {
        return key;
    }

    public String value;

    private String title;

    List<ScmobjectIdModel> child=new ArrayList<>();

    public ScmobjectIdModel convert(scmobjectvo scmobjectvo){
        this.key=scmobjectvo.getKey();
        this.value=scmobjectvo.getValue();
        this.title=scmobjectvo.getTitle();

        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ScmobjectIdModel> getChild() {
        return child;
    }

    public void setChild(List<ScmobjectIdModel> child) {
        this.child = child;
    }
}
