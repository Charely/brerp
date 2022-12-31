package org.br.erp.base.entityvo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class scmobjectvo implements Serializable {

    private String key;

    private String value;

    private String title;


    private String id;


    private String objectcode;

    private String objectname;

    private String objecttable;


    private String parentid;

    private List<scmobjectvo> children = new ArrayList<>();
}
