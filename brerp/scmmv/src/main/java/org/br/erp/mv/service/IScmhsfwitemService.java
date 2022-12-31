package org.br.erp.mv.service;

import org.br.erp.mv.entity.Scmhsfwitem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 核算范围分录
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
public interface IScmhsfwitemService extends IService<Scmhsfwitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmhsfwitem>
	 */
	public List<Scmhsfwitem> selectByMainId(String mainId);
}
