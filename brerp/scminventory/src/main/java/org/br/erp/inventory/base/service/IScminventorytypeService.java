package org.br.erp.inventory.base.service;

import org.br.erp.inventory.base.entity.Scminventorytype;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 库存单据类型
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
public interface IScminventorytypeService extends IService<Scminventorytype> {

    String getInventoryType(String inventorytypeid);

    Scminventorytype getInventoryTypeByTypecode(String typecode);
}
