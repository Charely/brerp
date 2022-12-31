package org.br.erp.inventory.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.br.erp.base.entity.Scmbatch;
import org.br.erp.inventory.base.entity.Scmimbalance;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.inventory.base.vo.ScmimbalanceVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 库存账本表
 * @Author: jeecg-boot
 * @Date:   2022-10-24
 * @Version: V1.0
 */
public interface IScmimbalanceService extends IService<Scmimbalance> {

    String addToBalance(List<String> ids);

    String uoaddToBalance(List<String> ids);



    List<ScmimbalanceVo> getBalanceAmount(IPage<ScmimbalanceVo> page, Map queryMap);


    String transferBalance(List<String> ids);

    String untransferBalance(List<String> ids);


    Scmbatch getScmbatchInfoByCompanyAndWarehouse(String companyid,String warehouseid,String materialid);

    List<Scmbatch> getBatchList(Integer pageNo,Integer pageSize,String companyid,String warehouseid,String materialid);

    List<Scmbatch> getBatchListByMap(Integer pageNo,Integer pageSize,Map paramMap);

    String stockCheck(List<String> ids);

}
