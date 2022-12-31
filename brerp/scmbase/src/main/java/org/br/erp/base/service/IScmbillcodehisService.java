package org.br.erp.base.service;

import org.br.erp.base.entity.Scmbillcodehis;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 单据编号历史记录
 * @Author: jeecg-boot
 * @Date:   2022-10-14
 * @Version: V1.0
 */
public interface IScmbillcodehisService extends IService<Scmbillcodehis> {


    List<Scmbillcodehis> selectScmBillCodeHisByObjectId(String objectId);
}
