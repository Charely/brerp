package org.br.erp.bussinessobject.service;

import org.jeecg.common.system.vo.SelectTreeModel;
import org.br.erp.bussinessobject.entity.Scmobject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 业务对象描述
 * @Author: jeecg-boot
 * @Date:   2022-09-28
 * @Version: V1.0
 */
public interface IScmobjectService extends IService<Scmobject> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**
	 * 新增节点
	 *
	 * @param scmobject
	 */
	void addScmobject(Scmobject scmobject);
	
	/**
   * 修改节点
   *
   * @param scmobject
   * @throws JeecgBootException
   */
	void updateScmobject(Scmobject scmobject) throws JeecgBootException;
	
	/**
	 * 删除节点
	 *
	 * @param id
   * @throws JeecgBootException
	 */
	void deleteScmobject(String id) throws JeecgBootException;

	  /**
	   * 查询所有数据，无分页
	   *
	   * @param queryWrapper
	   * @return List<Scmobject>
	   */
    List<Scmobject> queryTreeListNoPage(QueryWrapper<Scmobject> queryWrapper);

	/**
	 * 【vue3专用】根据父级编码加载分类字典的数据
	 *
	 * @param parentCode
	 * @return
	 */
	List<SelectTreeModel> queryListByCode(String parentCode);

	/**
	 * 【vue3专用】根据pid查询子节点集合
	 *
	 * @param pid
	 * @return
	 */
	List<SelectTreeModel> queryListByPid(String pid);

}
