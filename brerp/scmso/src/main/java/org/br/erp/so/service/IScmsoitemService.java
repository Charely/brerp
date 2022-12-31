package org.br.erp.so.service;

import org.br.erp.so.entity.Scmsoitem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售订单分录
 * @Author: jeecg-boot
 * @Date:   2022-10-30
 * @Version: V1.0
 */
public interface IScmsoitemService extends IService<Scmsoitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmsoitem>
	 */
	public List<Scmsoitem> selectByMainId(String mainId);


	void updateSoitemOutFlag(String itemid,String outFlag);
}
