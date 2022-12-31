package org.br.erp.pr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.pr.entity.Scmpritem;

import java.util.List;

/**
 * @Description: 采购计划分录
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
public interface IScmpritemService extends IService<Scmpritem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmpritem>
	 */
	public List<Scmpritem> selectByMainId(String mainId);
}
