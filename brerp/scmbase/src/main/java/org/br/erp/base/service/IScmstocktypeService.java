package org.br.erp.base.service;

import org.br.erp.base.entity.Scmstocktype;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 库存状态
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
public interface IScmstocktypeService extends IService<Scmstocktype> {


    void saveMain(Scmstocktype scmstocktype);
}
