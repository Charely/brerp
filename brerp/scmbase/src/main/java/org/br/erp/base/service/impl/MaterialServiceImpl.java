package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Material;
import org.br.erp.base.entity.Materialpo;
import org.br.erp.base.entity.Materialsale;
import org.br.erp.base.entity.Materialstock;
import org.br.erp.base.mapper.MaterialMapper;
import org.br.erp.base.mapper.MaterialPoMapper;
import org.br.erp.base.mapper.MaterialSaleMapper;
import org.br.erp.base.mapper.MaterialStockMapper;
import org.br.erp.base.service.IMaterialService;
import org.br.erp.base.vo.MaterialPage;
import org.br.erp.base.vo.MaterialVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements IMaterialService {

    @Autowired
    MaterialMapper materialMapper;

    @Autowired
    MaterialSaleMapper materialSaleMapper;

    @Autowired
    MaterialPoMapper materialPoMapper;

    @Autowired
    MaterialStockMapper materialStockMapper;
    @Override
    public List<Material> getMaterialByMatrerialTypeid(String materialTypeid) {
        return materialMapper.getMaterialsByMaterialTypeid(materialTypeid);
    }

    @Override
    public Material getMaterialbyMaterialId(String materialid) {
        return materialMapper.getMaterialByMaterialId(materialid);
    }

    @Override
    @Transactional
    public void updatestatus(List<String> ids, String statusFlag) {
        materialMapper.selectBatchIds(ids).forEach(item->{
            item.setEnablestatus(statusFlag);
            materialMapper.updateById(item);
        });
    }

    @Override
    @Transactional
    public void saveMainObject(MaterialPage materialPage) {
        Material material=new Material();
        BeanUtils.copyProperties(materialPage,material);
        materialMapper.insert(material);

        List<Materialsale> materialSaleList = materialPage.getMaterialSaleList();
        if(materialSaleList!=null && materialSaleList.size()>0){
            Materialsale materialSale = materialSaleList.get(0);
            materialSale.setParentid(material.getId());
            materialSaleMapper.insert(materialSale);
        }
        List<Materialpo> materialPoList = materialPage.getMaterialPoList();
        if(materialPoList!=null && materialPoList.size()>0){
            Materialpo materialPo=materialPoList.get(0);
            materialPo.setParentid(material.getId());
            materialPoMapper.insert(materialPo);
        }

        List<Materialstock> materialStockList = materialPage.getMaterialStockList();
        if(materialStockList!=null && materialStockList.size()>0){
            Materialstock materialstock=materialStockList.get(0);
            materialstock.setParentid(material.getId());
            materialStockMapper.insert(materialstock);
        }
    }

    @Override
    public List<Materialsale> getMaterisaleByMaterialid(String materialid) {
        QueryWrapper<Materialsale> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parentid",materialid);
        List<Materialsale> materialsales = materialSaleMapper.selectList(queryWrapper);
        return materialsales;
    }

    @Override
    public List<Materialpo> getMaterialpoByMaterialid(String materialid) {
        QueryWrapper<Materialpo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parentid",materialid);
        List<Materialpo> materialpos = materialPoMapper.selectList(queryWrapper);
        return materialpos;
    }

    @Override
    public List<Materialstock> getMaterialstockByMaterialid(String materialid) {
        QueryWrapper<Materialstock> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parentid",materialid);
        List<Materialstock> Materialstocks = materialStockMapper.selectList(queryWrapper);
        return Materialstocks;
    }

    @Override
    @Transactional
    public void updateByMainId(MaterialPage materialPage) {
        Material material=new Material();
        BeanUtils.copyProperties(materialPage,material);
        materialMapper.updateById(material);
        if(materialPage.getMaterialSaleList()!=null && materialPage.getMaterialSaleList().size()>0){
            Materialsale materialsale=new Materialsale();
            BeanUtils.copyProperties(materialPage.getMaterialSaleList().get(0),materialsale);
            materialsale.setParentid(material.getId());
//            if(getMaterisaleByMaterialid(material.getId()) == null
//                    || getMaterisaleByMaterialid(material.getId()).size() == 0){
//                materialSaleMapper.insert(materialsale);
//            }else {
//                materialSaleMapper.updateById(materialsale);
//            }
            materialSaleMapper.deleteById(materialsale.getId());
            materialSaleMapper.insert(materialsale);
        }
        if(materialPage.getMaterialPoList()!=null && materialPage.getMaterialPoList().size()>0){
            Materialpo materialpo=new Materialpo();
            BeanUtils.copyProperties(materialPage.getMaterialPoList().get(0),materialpo);
            materialpo.setParentid(material.getId());
//            List<Materialpo> materialpoByMaterialid = getMaterialpoByMaterialid(material.getId());
//            if(materialpoByMaterialid!=null && materialpoByMaterialid.size()>0){
//                materialpoByMaterialid.stream().forEach(item->{
//                    materialPoMapper.deleteById(item.getId());
//                });
//            }
            materialPoMapper.deleteById(materialpo.getId());
            materialPoMapper.insert(materialpo);
        }
        if(materialPage.getMaterialStockList()!=null && materialPage.getMaterialStockList().size()>0){
            Materialstock materialstock=new Materialstock();
            BeanUtils.copyProperties(materialPage.getMaterialStockList().get(0),materialstock);
            materialStockMapper.deleteById(materialstock.getId());
            materialstock.setParentid(material.getId());
            materialStockMapper.insert(materialstock);
        }
    }

    @Override
    public MaterialVo getMaterialVoInfoByMaterialID(String materialID) {
        MaterialVo materialVo=new MaterialVo();
        Material material = materialMapper.selectById(materialID);
        if(material==null)
            return null;
        BeanUtils.copyProperties(material,materialVo);

        QueryWrapper<Materialsale> materialsaleQueryWrapper=new QueryWrapper<>();
        materialsaleQueryWrapper.eq("parentid",materialID);
        List<Materialsale> materialsales = materialSaleMapper.selectList(materialsaleQueryWrapper);
        if(materialsales!=null && materialsales.size()>0){
            BeanUtils.copyProperties(materialsales.get(0),materialVo);
        }

        QueryWrapper<Materialpo> materialpoQueryWrapper=new QueryWrapper<>();
        materialpoQueryWrapper.eq("parentid",materialID);
        List<Materialpo> materialpos = materialPoMapper.selectList(materialpoQueryWrapper);
        if(materialpos!=null && materialpos.size()>0){
            BeanUtils.copyProperties(materialpos.get(0),materialVo);
        }

        QueryWrapper<Materialstock> materialstockQueryWrapper=new QueryWrapper<>();
        materialstockQueryWrapper.eq("parentid",materialID);
        List<Materialstock> materialstocks = materialStockMapper.selectList(materialstockQueryWrapper);
        if(materialstocks!=null && materialstocks.size()>0){
            BeanUtils.copyProperties(materialstocks.get(0),materialVo);
        }

        return materialVo;

    }
}
