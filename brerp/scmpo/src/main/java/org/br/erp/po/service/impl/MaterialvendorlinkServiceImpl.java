package org.br.erp.po.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.br.erp.base.entity.Material;
import org.br.erp.base.entity.Scmpartner;
import org.br.erp.base.service.IMaterialService;
import org.br.erp.base.service.IScmpartnerService;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.po.entity.Materialvendorlink;
import org.br.erp.po.entity.Materialvendorlinkitem;
import org.br.erp.po.mapper.MaterialvendorlinkMapper;
import org.br.erp.po.mapper.MaterialvendorlinkitemMapper;
import org.br.erp.po.service.IMaterialvendorlinkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 货源清单
 * @Author: jeecg-boot
 * @Date:   2022-08-07
 * @Version: V1.0
 */
@Service
public class MaterialvendorlinkServiceImpl extends ServiceImpl<MaterialvendorlinkMapper, Materialvendorlink> implements IMaterialvendorlinkService {

	@Autowired
	private MaterialvendorlinkMapper materialvendorlinkMapper;
	@Autowired
	private MaterialvendorlinkitemMapper materialvendorlinkitemMapper;

	@Autowired
	private IMaterialService materialService;
	@Autowired
	private IScmpartnerService scmpartnerService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Materialvendorlink materialvendorlink, List<Materialvendorlinkitem> materialvendorlinkitemList) {
		if(!materialvendorlink.getMaterialid().equalsIgnoreCase("")){

			Map<String,Object> columnMap=new HashMap<>();
			columnMap.put("materialid",materialvendorlink.getMaterialid().toString());
			//先判断是否之前有相同的物料已经进行货源清单控制了
			List<Materialvendorlink> materialvendorlinks = materialvendorlinkMapper.selectByMap(columnMap);
			if(materialvendorlinks!=null && materialvendorlinks.size()>0){
				throw  new RuntimeException("已存在相同物料的货源清单，不允许再次生成");
			}

			Material material = materialService.getMaterialbyMaterialId(materialvendorlink.getMaterialid().toString());
			materialvendorlink.setMaterialcode(material.getMaterialcode());
			materialvendorlink.setMaterialname(material.getMaterialname());
		}
		materialvendorlinkMapper.insert(materialvendorlink);
		if(materialvendorlinkitemList!=null && materialvendorlinkitemList.size()>0) {
			for(Materialvendorlinkitem entity:materialvendorlinkitemList) {
				//外键设置
				entity.setParentid(materialvendorlink.getId());
				Scmpartner scmpartner = scmpartnerService.getById(entity.getVendorid());
				entity.setVendorcode(scmpartner.getPartnercode());
				entity.setVendorname(scmpartner.getPartnername());
				if(ERPUtils.isNoneOrEmpty(entity.getSumordervalue())){
					entity.setSumordervalue(new BigDecimal(0));
				}

				if(entity.getFixedvendor().equalsIgnoreCase("false")){
					entity.setFixedvendor("0");
				}else{
					entity.setFixedvendor("1");
				}
				materialvendorlinkitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Materialvendorlink materialvendorlink,List<Materialvendorlinkitem> materialvendorlinkitemList) {
		materialvendorlinkMapper.updateById(materialvendorlink);
		
		//1.先删除子表数据
		materialvendorlinkitemMapper.deleteByMainId(materialvendorlink.getId());
		
		//2.子表数据重新插入
		if(materialvendorlinkitemList!=null && materialvendorlinkitemList.size()>0) {
			for(Materialvendorlinkitem entity:materialvendorlinkitemList) {
				//外键设置
				entity.setParentid(materialvendorlink.getId());
				if(entity.getFixedvendor()!=null && entity.getFixedvendor().equalsIgnoreCase("false")){
					entity.setFixedvendor("0");
				}else{
					entity.setFixedvendor("1");
				}
				materialvendorlinkitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		materialvendorlinkitemMapper.deleteByMainId(id);
		materialvendorlinkMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			materialvendorlinkitemMapper.deleteByMainId(id.toString());
			materialvendorlinkMapper.deleteById(id);
		}
	}

	@Override
	public List<Materialvendorlinkitem> getVendorItemsByMaterialid(String materialid) {
		HashMap queryMap=new HashMap();
		queryMap.put("materialid",materialid);
		queryMap.put("canenable","true");
		List<Materialvendorlink> list = materialvendorlinkMapper.selectByMap(queryMap);
		if(list==null || list.size()>1 || list.size()==0){
			throw  new RuntimeException("当前物料定义的货源清单有问题，请重新定义");
		}
		Materialvendorlink materialvendorlink = list.get(0);
		QueryWrapper<Materialvendorlinkitem> queryWrapper=new QueryWrapper();
		queryWrapper.eq("parentid",materialvendorlink.getId());
		List<Materialvendorlinkitem> materialvendorlinkitems = materialvendorlinkitemMapper.selectList(queryWrapper);
		if(materialvendorlinkitems==null || materialvendorlinkitems.size()==0){
			throw  new RuntimeException("货源清单上没有定义供应商信息");
		}
		return materialvendorlinkitems;
	}

	@Override
	public List<Materialvendorlink> getMaterialsOfVendor(String vendorid) {
		List<Materialvendorlink> res=new ArrayList<>();
		QueryWrapper<Materialvendorlinkitem> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("vendorid",vendorid);
		List<Materialvendorlinkitem> materialvendorlinkitems = materialvendorlinkitemMapper.selectList(queryWrapper);
		if(materialvendorlinkitems!=null && materialvendorlinkitems.size()>0){
			for (Materialvendorlinkitem materialvendorlinkitem : materialvendorlinkitems) {
				String parentid = materialvendorlinkitem.getParentid();
				Materialvendorlink materialvendorlink = materialvendorlinkMapper.selectById(parentid);
				if(materialvendorlink!=null){
					res.add(materialvendorlink);
				}
			}
		}
		return res;

	}

}
