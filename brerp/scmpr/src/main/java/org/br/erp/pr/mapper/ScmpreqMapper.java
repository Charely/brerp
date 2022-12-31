package org.br.erp.pr.mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.br.erp.pr.entity.Scmpreq;
import org.br.erp.pr.entity.ScmpreqVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 采购申请
 * @Author: jeecg-boot
 * @Date:   2022-07-27
 * @Version: V1.0
 */
public interface ScmpreqMapper extends BaseMapper<Scmpreq> {

    @Select("select head.id as mainid,head.preqcode,head.create_by as createby,dept.depart_name as preqDeptName,sysuser.realname as preqEmptName," +
            " head.sumqty as totalqty,head.sumvalue as totalvalue,item.id as itemid, item.materialcode as materialcode," +
            " item.materialname as materialname,item.qty,item.price,item.taxinvalue as itemvalue from scmpreq " +
            " head left join scmpreqitem item on head.id=item.parentid" +
            " left join sys_depart dept on head.preqdept=dept.id" +
            " left join sys_user sysuser on head.preqemp=sysuser.id" +
            " where item.is_pr ='0'")
    public List<ScmpreqVo> selectAll();

    public List<ScmpreqVo>  getpreqVoList(@Param("page")IPage<ScmpreqVo> page,
                                          @Param(Constants.WRAPPER) Wrapper<ScmpreqVo> queryWrapper);
}
