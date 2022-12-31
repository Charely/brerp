package org.br.erp.base.service;

import org.br.erp.base.entity.Scmcustomfields;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 自定义表字段
 * @Author: jeecg-boot
 * @Date:   2022-09-30
 * @Version: V1.0
 */
public interface IScmcustomfieldsService extends IService<Scmcustomfields> {

     boolean cusinsert(Scmcustomfields entity);

     List<Scmcustomfields> getCustomfieldsByobjectcode(String objectcode);
}
