package org.br.erp.base.service;

import org.br.erp.base.entity.IMperiod;
import org.br.erp.base.entity.Mvperiod;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 库存期间
 * @Author: jeecg-boot
 * @Date:   2022-11-27
 * @Version: V1.0
 */
public interface IIMperiodService extends IService<IMperiod> {

    public void addIMPerioid(String companyid);

    public void changeIMPeriodState(String companyid,String perioddate,String periodState);


    public boolean checkPeriodState(String companyid,String periodDate);
}
