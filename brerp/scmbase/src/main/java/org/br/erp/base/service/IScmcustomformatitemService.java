package org.br.erp.base.service;

import org.br.erp.base.entity.Scmcustomformatitem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 自定义格式表分录
 * @Author: jeecg-boot
 * @Date:   2022-10-28
 * @Version: V1.0
 */
public interface IScmcustomformatitemService extends IService<Scmcustomformatitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmcustomformatitem>
	 */
	public List<Scmcustomformatitem> selectByMainId(String mainId);
}
