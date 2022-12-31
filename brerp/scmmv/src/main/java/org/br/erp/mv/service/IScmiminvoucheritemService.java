package org.br.erp.im.service;

import org.br.erp.im.entity.Scmiminvoucheritem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 入库凭证分录
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
public interface IScmiminvoucheritemService extends IService<Scmiminvoucheritem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmiminvoucheritem>
	 */
	public List<Scmiminvoucheritem> selectByMainId(String mainId);
}
