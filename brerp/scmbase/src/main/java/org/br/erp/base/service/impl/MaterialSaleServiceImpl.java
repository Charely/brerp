package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.br.erp.base.entity.Materialsale;
import org.br.erp.base.mapper.MaterialSaleMapper;
import org.br.erp.base.service.IMaterialSaleService;
import org.springframework.stereotype.Service;

@Service
public class MaterialSaleServiceImpl  extends ServiceImpl<MaterialSaleMapper, Materialsale>  implements IMaterialSaleService {
}
