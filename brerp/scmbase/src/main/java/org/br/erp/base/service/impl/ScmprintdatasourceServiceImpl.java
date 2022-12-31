package org.br.erp.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import org.br.erp.base.entity.Scmprintdatasource;
import org.br.erp.base.entity.Scmprintformat;
import org.br.erp.base.mapper.ScmprintdatasourceMapper;
import org.br.erp.base.service.IScmprintdatasourceService;
import org.br.erp.base.utils.ERPUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: 打印数据源
 * @Author: jeecg-boot
 * @Date:   2022-12-17
 * @Version: V1.0
 */
@Service
public class ScmprintdatasourceServiceImpl extends ServiceImpl<ScmprintdatasourceMapper, Scmprintdatasource> implements IScmprintdatasourceService {

    @Autowired
    private ScmprintdatasourceMapper scmprintdatasourceMapper;

    @Override
    public List<Scmprintdatasource> queryPrintDataSourceByObjectCode(Map reqMap) {
        String objectcode = ERPUtils.getHttpReqParam(reqMap, "objectcode");
        String companyid = ERPUtils.getHttpReqParam(reqMap, "companyid");
        QueryWrapper<Scmprintdatasource> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("objectcode",objectcode);
        queryWrapper.eq("companyid",companyid);
        List<Scmprintdatasource> scmprintdatasources = scmprintdatasourceMapper.selectList(queryWrapper);
        if(scmprintdatasources==null || scmprintdatasources.size() ==0){
            queryWrapper.clear();
            queryWrapper.eq("objectcode",objectcode);
            queryWrapper.isNull("companyid");
            List<Scmprintdatasource> nocmpnayData = scmprintdatasourceMapper.selectList(queryWrapper);
            if(nocmpnayData!=null && nocmpnayData.size()>0){
                Scmprintdatasource scmprintdatasource = nocmpnayData.get(0);
                Scmprintdatasource newScmprintdatasource=new Scmprintdatasource();
                BeanUtils.copyProperties(scmprintdatasource,newScmprintdatasource);
                newScmprintdatasource.setId("");
                newScmprintdatasource.setCompanyid(companyid);
                scmprintdatasourceMapper.insert(newScmprintdatasource);
                ArrayList<Scmprintdatasource> res = new ArrayList<>();
                res.add(newScmprintdatasource);
                return res;
            }else{
                return null;
            }
        }else {
            return scmprintdatasources;
        }
    }

    @Override
    public JSONArray queryPrintDataByObject(String objectCode, String ids) {
        JSONArray jsonArray=new JSONArray();
        QueryWrapper<Scmprintdatasource> queryWrapper=new QueryWrapper<>();
        String[] split = ids.split(",");
        queryWrapper.eq("objectcode",objectCode);
        List<Scmprintdatasource> scmprintdatasources = scmprintdatasourceMapper.selectList(queryWrapper);
        if(scmprintdatasources!=null && scmprintdatasources.size()>0){
            Scmprintdatasource scmprintdatasource = scmprintdatasources.get(0);
            JSONObject printdatasource=JSONObject.parseObject(scmprintdatasource.getPrintdatasource());
            JSONObject jsmain = (JSONObject) printdatasource.get("main");
            //表头数据源
            String mainsql = (String) jsmain.get("datasource");
            String keyid = (String) jsmain.get("keyid");
            //处理表体item
            JSONObject items = (JSONObject) printdatasource.get("items");
            for (String firstid : split) {
                String runsql=mainsql+" where "+keyid +" = '"+firstid+"'";
                List<Map<String, Object>> maps = SqlRunner.db().selectList(runsql);
                if(maps!=null && maps.size()>0){
                    JSONObject res=new JSONObject();
                    //获取全部的数据，循环遍历
                    for (Map<String, Object> map : maps) {
                        map.keySet().forEach(item->{
                            res.put(item,map.get(item));
                        });
                    }
                    for (String itemkey : items.keySet()) {
                        JSONObject currentItem = (JSONObject) items.get(itemkey);
                        if(currentItem!=null){
                            String curItemdatasource = (String) currentItem.get("datasource");
                            String parentid = (String) currentItem.get("parentid");
                            curItemdatasource+=" where "+parentid+"='"+firstid+"'";
                            List<Map<String, Object>> itemMaps = SqlRunner.db().selectList(curItemdatasource);
                            if(itemMaps!=null && itemMaps.size()>0){
                                List<JSONObject> itemObjectList=new ArrayList<>();
                                for (Map<String, Object> itemMap : itemMaps) {
                                    JSONObject itemObject = new JSONObject();
                                    for (String itemKey : itemMap.keySet()) {
                                        itemObject.put(itemKey,itemMap.get(itemKey));
                                    }
                                    itemObjectList.add(itemObject);
                                }
                                res.put(itemkey,itemObjectList);
                            }
                        }
                    }

                    if(res!=null){
                        jsonArray.add(res);
                    }
                }
            }
        }

        return jsonArray;
    }
}
