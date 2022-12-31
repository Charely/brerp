package org.br.erp.po.service.impl;

import org.br.erp.po.entity.Scmmatreceiptreqitem;
import org.br.erp.po.entity.Scmpoitem;
import org.br.erp.po.mapper.ScmmatreceiptreqitemMapper;
import org.br.erp.po.service.IScmmatreceiptreqitemService;
import org.br.erp.po.service.IScmpoitemService;
import org.br.erp.po.vo.ScmInventoryReferMatreceiptItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 收料申请单分录
 * @Author: jeecg-boot
 * @Date:   2022-10-18
 * @Version: V1.0
 */
@Service
public class ScmmatreceiptreqitemServiceImpl extends ServiceImpl<ScmmatreceiptreqitemMapper, Scmmatreceiptreqitem> implements IScmmatreceiptreqitemService {
	
	@Autowired
	private ScmmatreceiptreqitemMapper scmmatreceiptreqitemMapper;

	@Autowired
	private IScmpoitemService scmpoitemService;
	
	@Override
	public List<Scmmatreceiptreqitem> selectByMainId(String mainId) {
		return scmmatreceiptreqitemMapper.selectByMainId(mainId);
	}

	@Override
	public List<ScmInventoryReferMatreceiptItemVo> getscmInventoryReferMaterItemVo(String ids) {

		List<ScmInventoryReferMatreceiptItemVo> res=new ArrayList<>();

		String[] split = ids.split(",");

		Arrays.stream(split).forEach(item->{
			ScmInventoryReferMatreceiptItemVo scmInventoryReferMatreceiptItemVo=new ScmInventoryReferMatreceiptItemVo();
			Scmmatreceiptreqitem scmmatreceiptreqitem = scmmatreceiptreqitemMapper.selectById(item);
			BeanUtils.copyProperties(scmmatreceiptreqitem,scmInventoryReferMatreceiptItemVo);
			res.add(scmInventoryReferMatreceiptItemVo);
		});
		if(res.size()>0){
			res.stream().forEach(item->{
				if(item.getFromitemid()!=null && !item.getFromitemid().equalsIgnoreCase("")){
					Scmpoitem scmpoitem = scmpoitemService.getById(item.getFromitemid());
					if(scmpoitem!=null){
						item.setTaxinprice(scmpoitem.getTaxinprice());
						item.setTaxexprice(scmpoitem.getTaxexprice());
						item.setTaxrate(scmpoitem.getTaxrate());
					}
				}
			});
		}

		return res;

	}
}
