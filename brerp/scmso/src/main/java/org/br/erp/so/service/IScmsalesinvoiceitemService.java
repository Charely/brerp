package org.br.erp.so.service;

import org.br.erp.so.entity.Scmsalesinvoiceitem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售发票分录
 * @Author: jeecg-boot
 * @Date:   2022-11-08
 * @Version: V1.0
 */
public interface IScmsalesinvoiceitemService extends IService<Scmsalesinvoiceitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmsalesinvoiceitem>
	 */
	public List<Scmsalesinvoiceitem> selectByMainId(String mainId);
}
