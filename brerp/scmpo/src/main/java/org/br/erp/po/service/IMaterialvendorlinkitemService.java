package org.br.erp.po.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.po.entity.Materialvendorlinkitem;

import java.util.List;

/**
 * @Description: 货源清单分录
 * @Author: jeecg-boot
 * @Date:   2022-08-07
 * @Version: V1.0
 */
public interface IMaterialvendorlinkitemService extends IService<Materialvendorlinkitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Materialvendorlinkitem>
	 */
	public List<Materialvendorlinkitem> selectByMainId(String mainId);
}
