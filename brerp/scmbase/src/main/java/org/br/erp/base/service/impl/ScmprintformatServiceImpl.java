package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Scmprintformat;
import org.br.erp.base.mapper.ScmprintformatMapper;
import org.br.erp.base.service.IScmprintformatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 打印数据源
 * @Author: jeecg-boot
 * @Date:   2022-12-18
 * @Version: V1.0
 */
@Service
public class ScmprintformatServiceImpl extends ServiceImpl<ScmprintformatMapper, Scmprintformat> implements IScmprintformatService {

    @Autowired
    ScmprintformatMapper scmprintformatMapper;


    @Override
    @Transactional
    public void savefomrat(Scmprintformat scmprintformat) {
        //先删除当前业务对象所属的打印格式
        String objectcode = scmprintformat.getObjectcode();
        QueryWrapper<Scmprintformat> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("objectcode",objectcode);
        scmprintformatMapper.delete(queryWrapper);

        scmprintformatMapper.insert(scmprintformat);
    }

    @Override
    public Scmprintformat queryPrintFormatByObjectCode(String objectCode) {
        QueryWrapper<Scmprintformat> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("objectcode",objectCode);
        List<Scmprintformat> scmprintformats = scmprintformatMapper.selectList(queryWrapper);
        if(scmprintformats!=null && scmprintformats.size()>0){
            return scmprintformats.get(0);
        }else {
            return null;
        }
    }
}
