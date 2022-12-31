package org.br.erp.pr.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.lettuce.core.pubsub.PubSubEndpoint;
import org.br.erp.pr.entity.Scmpreq;
import org.br.erp.pr.entity.ScmpreqVo;
import org.br.erp.pr.entity.Scmpreqitem;
import org.jeecg.common.api.vo.Result;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采购申请
 * @Author: jeecg-boot
 * @Date:   2022-07-27
 * @Version: V1.0
 */
public interface IScmpreqService extends IService<Scmpreq> {


	Result audit(String dataid);
	/**
	 * 添加一对多
	 *
	 * @param scmpreq
	 * @param scmpreqitemList
	 */
	public void saveMain(Scmpreq scmpreq,List<Scmpreqitem> scmpreqitemList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param scmpreq
	 * @param scmpreqitemList
	 */
	public void updateMain(Scmpreq scmpreq,List<Scmpreqitem> scmpreqitemList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


	List<Scmpreq> selectScmpreqList(Collection<? extends  Serializable> idList);


	List<ScmpreqVo> getScmpreqVoList();

	IPage<ScmpreqVo> selectpregVoPage(IPage<ScmpreqVo> page, Map queryWrapper);
}
