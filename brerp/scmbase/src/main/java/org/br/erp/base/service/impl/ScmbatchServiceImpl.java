package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Scmbatch;
import org.br.erp.base.mapper.ScmbatchMapper;
import org.br.erp.base.service.IMaterialService;
import org.br.erp.base.service.IScmbatchService;
import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.service.writebackapi.IBalanceIntf;
import org.br.erp.base.vo.MaterialVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 批号档案
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
@Service
public class ScmbatchServiceImpl extends ServiceImpl<ScmbatchMapper, Scmbatch> implements IScmbatchService {

    @Autowired
    ScmbatchMapper scmbatchMapper;

    @Autowired
    IScmbillcoderuleService scmbillcoderuleService;

    @Autowired
    IMaterialService materialService;


    @Override
    public Map saveBatchinfo(String companyid,String warehouseid,String materialid, String qty, String vendorid,
                             String vendorcode, String batchcreatedate,String batchenddate,String batchcode) {
        Scmbatch scmbatch=new Scmbatch();
        String billCode = scmbillcoderuleService.getBillCode("scmbatch");
        if(batchcode!=null && !batchcode.equalsIgnoreCase("")){
            billCode=batchcode;
        }
        if(billCode==null || billCode.equalsIgnoreCase("")){
            scmbatch.setBatchcode("nobillcode");
        }else {
            scmbatch.setBatchcode(billCode);
        }
        scmbatch.setCompanyid(companyid);
        scmbatch.setMaterialid(materialid);
        scmbatch.setWarehouseid(warehouseid);
        scmbatch.setVendorid(vendorid);
        scmbatch.setOrgbatchcode(vendorcode);
        scmbatch.setFromdate(batchcreatedate);
        scmbatch.setEnddate(batchenddate);
        scmbatch.setBatchqty(qty);
        scmbatchMapper.insert(scmbatch);
        Map res=new HashMap();
        res.put("batchid",scmbatch.getId());
        res.put("batchcode",scmbatch.getBatchcode());
        return res;

    }
    @Override
    @Deprecated
    public Scmbatch getscmbatchByCompanyandmaterial(String companyid,String warehouseid, String materialid) {
        MaterialVo materialVoInfoByMaterialID = materialService.getMaterialVoInfoByMaterialID(materialid);
        if(materialVoInfoByMaterialID!=null){
            if(!(materialVoInfoByMaterialID.getIsbatch()!=null
                    && materialVoInfoByMaterialID.getIsbatch().equalsIgnoreCase("true"))){
                return null;
            }
        }
        QueryWrapper<Scmbatch> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("companyid",companyid);
        if(warehouseid!=null && !warehouseid.equalsIgnoreCase("")){
            queryWrapper.eq("warehouseid",warehouseid);
        }
        if(materialid!=null && !materialid.equalsIgnoreCase("")) {
            queryWrapper.eq("materialid", materialid);
        }
        queryWrapper.ne("batchqty","0");
        queryWrapper.orderByAsc("create_time");
        List<Scmbatch> scmbatches = scmbatchMapper.selectList(queryWrapper);
        if(scmbatches!=null && scmbatches.size()>0){

        }
        return null;
    }

    @Override
    public List<Scmbatch> getBatchList(String companyid, String warehouseid, String materialid) {
        QueryWrapper<Scmbatch> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("companyid",companyid);
        queryWrapper.eq("wareshouseid",warehouseid);
        queryWrapper.eq("materialid",materialid);
        queryWrapper.orderByAsc("create_time");
        List<Scmbatch> scmbatches = scmbatchMapper.selectList(queryWrapper);
        return scmbatches;
    }
}
