package org.br.erp.po.service;

import org.br.erp.po.entity.Scmpurinvoiceitem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购发票分录
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
public interface IScmpurinvoiceitemService extends IService<Scmpurinvoiceitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmpurinvoiceitem>
	 */
	public List<Scmpurinvoiceitem> selectByMainId(String mainId);

	public List<Scmpurinvoiceitem> selectByFromItemId(String itemid);
}
