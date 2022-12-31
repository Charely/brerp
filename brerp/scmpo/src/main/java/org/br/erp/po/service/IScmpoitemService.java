package org.br.erp.po.service;

import org.br.erp.po.entity.Scmpoitem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 采购订单分录
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
public interface IScmpoitemService extends IService<Scmpoitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmpoitem>
	 */
	public List<Scmpoitem> selectByMainId(String mainId);

	public void writebackfrompo(String itemid, BigDecimal qty,boolean isdelete);
}
