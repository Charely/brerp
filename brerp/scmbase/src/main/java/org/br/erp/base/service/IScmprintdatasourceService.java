package org.br.erp.base.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.br.erp.base.entity.Scmprintdatasource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 打印数据源
 * @Author: jeecg-boot
 * @Date:   2022-12-17
 * @Version: V1.0
 */
public interface IScmprintdatasourceService extends IService<Scmprintdatasource> {


    public List<Scmprintdatasource> queryPrintDataSourceByObjectCode(Map reqMap);


    public JSONArray queryPrintDataByObject(String objectCode, String ids);
}
