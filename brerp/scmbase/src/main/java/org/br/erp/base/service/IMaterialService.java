package org.br.erp.base.service;

import org.br.erp.base.entity.Material;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.base.entity.Materialpo;
import org.br.erp.base.entity.Materialsale;
import org.br.erp.base.entity.Materialstock;
import org.br.erp.base.vo.MaterialPage;
import org.br.erp.base.vo.MaterialVo;

import java.util.List;

/**
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
public interface IMaterialService extends IService<Material> {

    public List<Material> getMaterialByMatrerialTypeid(String materialTypeid);

    Material getMaterialbyMaterialId(String materialid);

    interface IerpBaseCommonService {
    }

    void updatestatus(List<String> ids,String statusFlag);


    void saveMainObject(MaterialPage materialPage);


    List<Materialsale> getMaterisaleByMaterialid(String materialid);

    List<Materialpo> getMaterialpoByMaterialid(String materialid);


    List<Materialstock> getMaterialstockByMaterialid(String materialid);

    void updateByMainId(MaterialPage materialPage);


    MaterialVo getMaterialVoInfoByMaterialID(String materialID);
}
