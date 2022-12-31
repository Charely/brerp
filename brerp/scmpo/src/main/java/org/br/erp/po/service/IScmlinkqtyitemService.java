package org.br.erp.po.service;

import org.br.erp.po.entity.Scmlinkqtyitem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 配额协议分录
 * @Author: jeecg-boot
 * @Date:   2022-08-27
 * @Version: V1.0
 */
public interface IScmlinkqtyitemService extends IService<Scmlinkqtyitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmlinkqtyitem>
	 */
	public List<Scmlinkqtyitem> selectByMainId(String mainId);
}
