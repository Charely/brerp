package org.br.erp.base.service;

import org.br.erp.base.entity.Scmcustomformatitem;
import org.br.erp.base.entity.Scmcustomformat;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.base.vo.ColumnVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 自定义格式表
 * @Author: jeecg-boot
 * @Date:   2022-10-28
 * @Version: V1.0
 */
public interface IScmcustomformatService extends IService<Scmcustomformat> {

	/**
	 * 添加一对多
	 *
	 * @param scmcustomformat
	 * @param scmcustomformatitemList
	 */
	public void saveMain(Scmcustomformat scmcustomformat,List<Scmcustomformatitem> scmcustomformatitemList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param scmcustomformat
	 * @param scmcustomformatitemList
	 */
	public void updateMain(Scmcustomformat scmcustomformat,List<Scmcustomformatitem> scmcustomformatitemList);
	
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


	public List<ColumnVo> getColumnVoList(String objectCode);

	public void importSysFormat(String objectid);
	
}
