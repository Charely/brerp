package org.br.erp.pr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.pr.entity.Scmpreqitem;
import org.br.erp.pr.mapper.ScmpreqitemMapper;
import org.br.erp.pr.service.IScmpreqitemService;
import org.br.erp.pr.vo.ScmpreqitemVo;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 采购申请表体
 * @Author: jeecg-boot
 * @Date:   2022-07-27
 * @Version: V1.0
 */
@Service
public class ScmpreqitemServiceImpl extends ServiceImpl<ScmpreqitemMapper, Scmpreqitem> implements IScmpreqitemService {
	
	@Autowired
	private ScmpreqitemMapper scmpreqitemMapper;

	@Autowired
	public RedisUtil redisUtil;
	
	@Override
	public List<Scmpreqitem> selectByMainId(String mainId) {
		return scmpreqitemMapper.selectByMainId(mainId);
	}

	@Override
	public List<Scmpreqitem> selectByMainIds(Collection<? extends Serializable> ids) {
		QueryWrapper<Scmpreqitem> sectionQueryWrapper = new QueryWrapper<>();
		sectionQueryWrapper.in("PARENTID",ids);
		return scmpreqitemMapper.selectList(sectionQueryWrapper);
	}


	public List<ScmpreqitemVo> selectByIds(Collection<? extends  Serializable> ids){

		LoginUser user = ERPUtils.getLoginUser();
		//redisUtil.del("user.getId()");

		for (Serializable id : ids) {
			Object curridrefer = redisUtil.get(id + "refer");
			if(curridrefer!=null){
				if(curridrefer.equals(user.getId().toString()))
				{
					continue;
				}else{
					return null;
				}
			}else{
				redisUtil.set(id+"refer",user.getId());
			}

		}

		List<Scmpreqitem> scmpreqitems = scmpreqitemMapper.selectBatchIds(ids);
		//如果存在相同物料，就进行物料的合并处理
//		List<Scmpreqitem> res = scmpreqitems.stream().collect(Collectors.toMap(Scmpreqitem::getMaterialid, a -> a, (o1, o2) -> {
//			o1.setQty(o1.getQty().add(o2.getQty()));
//			o1.setTaxinvalue(o1.getTaxinvalue().add(o2.getTaxinvalue()));
//			o1.setPrice(o1.getTaxinvalue().divide(o1.getQty(), 2, 2));
//			Map map = (HashMap) redisUtil.get("user.getId()");
//			if(map==null){
//				map=new HashMap<>();
//				List list=new ArrayList<>();
//				list.add(o2.getId());
//				list.add(o1.getId());
//				map.put(o1.getId(),list);
//				redisUtil.set(user.getId(),map);
//			}else{
//				if(!map.containsKey(o1.getId())){
//					List newList=new ArrayList();
//					newList.add(o2.getId());
//					map.put(o1.getId(),newList);
//					redisUtil.set(user.getId(),map);
//				}else{
//					List list = (ArrayList)map.get(o1.getId());
//					if(!list.contains(o2.getId())){
//						list.add(o2.getId());
//						map.put(o1.getId(),list);
//						redisUtil.set("user.getId()",map);
//					}else{
//
//					}
//				}
//			}
//			return o1;
//		})).values().stream().collect(Collectors.toList());


		//返回结果后，需要加上当前的订货日期字段
		List<ScmpreqitemVo> newRet=new ArrayList<>();;
		for(int i=0;i<scmpreqitems.size();i++){
			Scmpreqitem scmpreqitem = scmpreqitems.get(i);
			ScmpreqitemVo curr=new ScmpreqitemVo();
			BeanUtils.copyProperties(scmpreqitem,curr);
			Date prOrderDate = ERPUtils.getPrOrderDate(scmpreqitems.get(i).getMaterialid(), scmpreqitems.get(i).getQty().toString(), scmpreqitems.get(i).getPreqdate());
			curr.setOrderdate(prOrderDate);
			newRet.add(curr);
		}

		return newRet;
	}

	@Override
	public void updatePrStatue(String itemid,String pritemid) {
		scmpreqitemMapper.updatePrStatue(itemid,pritemid);
	}

	@Override
	public void updatePreqItemStatus(String pritemid) {
		scmpreqitemMapper.updatePreqStatue(pritemid);
	}
}
