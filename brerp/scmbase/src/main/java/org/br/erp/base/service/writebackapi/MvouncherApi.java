package org.br.erp.base.service.writebackapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface MvouncherApi {
    public void createMvouncher(JSONArray jsonArray);

    public void writeBackMvouncherFromInstockApi(JSONObject jsonObject);
}
