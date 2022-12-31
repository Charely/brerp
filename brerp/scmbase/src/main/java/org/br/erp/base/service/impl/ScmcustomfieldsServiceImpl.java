package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Scmcustomfields;
import org.br.erp.base.entity.Scmobject;
import org.br.erp.base.mapper.ScmcustomfieldsMapper;
import org.br.erp.base.mapper.ScmobjectMapper;
import org.br.erp.base.service.IScmcustomfieldsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 自定义表字段
 * @Author: jeecg-boot
 * @Date:   2022-09-30
 * @Version: V1.0
 */
@Service
public class ScmcustomfieldsServiceImpl extends ServiceImpl<ScmcustomfieldsMapper, Scmcustomfields> implements IScmcustomfieldsService {

    @Autowired
    ScmcustomfieldsMapper scmcustomfieldsMapper;

    @Autowired
    ScmobjectMapper scmobjectMapper;
    @Override
    @Transactional
    public boolean cusinsert(Scmcustomfields entity) {
        //todo 执行正常的插入操作
        scmcustomfieldsMapper.insert(entity);

        //todo 开始查找对应的业务对象表信息，将字段插入到对应的字段表上
        Scmobject scmobject = scmobjectMapper.selectById(entity.getScmobjectid());
        if(scmobject!=null){
            String objecttable = scmobject.getObjecttable();
            scmcustomfieldsMapper.appendtablecolumn(objecttable,entity.getCustomcode());
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<Scmcustomfields> getCustomfieldsByobjectcode(String objectcode) {
        QueryWrapper<Scmobject> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("objectcode",objectcode);
        List<Scmobject> scmobjects = scmobjectMapper.selectList(queryWrapper);
        if(scmobjects==null || scmobjects.size()==0){
            return null;
        }
        QueryWrapper<Scmcustomfields> scmcustomfieldsQueryWrapper=new QueryWrapper<>();
        scmcustomfieldsQueryWrapper.eq("scmobjectid",scmobjects.get(0).getId());
        List<Scmcustomfields> scmcustomfields = scmcustomfieldsMapper.selectList(scmcustomfieldsQueryWrapper);
        return scmcustomfields;

    }
}
