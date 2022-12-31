package org.br.erp.base.service;

import org.br.erp.base.entity.Scmbatch;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 批号档案
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
public interface IScmbatchService extends IService<Scmbatch> {


    public Map saveBatchinfo(String companyid,
                             String warehouseid,
                             String materid,
                             String qty,
                             String vendorid,
                             String vendorcode,
                             String batchcreatedate,
                             String batchenddate,
                             String batchcode);


    public Scmbatch getscmbatchByCompanyandmaterial(String companyid,String warehouseid,String materialid);


    public List<Scmbatch> getBatchList(String companyid,String warehouseid,String materialid);
}
