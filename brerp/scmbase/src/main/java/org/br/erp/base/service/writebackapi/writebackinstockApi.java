package org.br.erp.base.service.writebackapi;

import com.alibaba.fastjson.JSONObject;

public interface writebackinstockApi {

    void WriteBackInstockFromPurinvoice(JSONObject jsonObject);
}
