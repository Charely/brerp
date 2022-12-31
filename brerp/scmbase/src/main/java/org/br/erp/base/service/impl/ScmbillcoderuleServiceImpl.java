package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Scmbillcodehis;
import org.br.erp.base.entity.Scmbillcoderule;
import org.br.erp.base.entity.Scmobject;
import org.br.erp.base.mapper.ScmbillcoderuleMapper;
import org.br.erp.base.service.IScmbillcodehisService;
import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.service.IScmobjectService;
import org.br.erp.base.utils.ERPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Description: 单据编号规则表
 * @Author: jeecg-boot
 * @Date:   2022-10-12
 * @Version: V1.0
 */
@Service
public class ScmbillcoderuleServiceImpl extends ServiceImpl<ScmbillcoderuleMapper, Scmbillcoderule> implements IScmbillcoderuleService {

    @Autowired
    private ScmbillcoderuleMapper scmbillcoderuleMapper;
    @Autowired
    private IScmobjectService scmobjectService;

    @Autowired
    private IScmbillcodehisService scmbillcodehisService;
    /**
     *获取单据编号信息
     */
    @Override
    @Transactional
    public String getBillCode(String objectcode) {
        String result="";
        //先获取业务对象
        QueryWrapper<Scmobject> scmobjectQueryWrapper=new QueryWrapper<>();
        scmobjectQueryWrapper.eq("objectcode",objectcode);
        Scmobject scmobject = scmobjectService.getOne(scmobjectQueryWrapper);
        //去查询是否有历史单据编号
        QueryWrapper<Scmbillcodehis> scmbillcodehisQueryWrapper=new QueryWrapper<>();
        if(scmobject==null){
            return ERPUtils.getDateString(new Date(),"yyyy-MM-dd hh:mm:ss")
                    .replaceAll("-","")
                    .replaceAll(":","")
                    .replaceAll(" ","")+String.valueOf(new Random().nextInt());

        }
        scmbillcodehisQueryWrapper.eq("objectid",scmobject.getId());

        List<Scmbillcodehis> scmbillcodehisList = scmbillcodehisService.selectScmBillCodeHisByObjectId(scmobject.getId());
        //再获取单据编号规则
        QueryWrapper<Scmbillcoderule> scmbillcoderuleQueryWrapper=new QueryWrapper<>();
        scmbillcoderuleQueryWrapper.eq("objectid",scmobject.getId());
        Scmbillcoderule scmbillcoderule = scmbillcoderuleMapper.selectOne(scmbillcoderuleQueryWrapper);
        if(scmbillcodehisList!=null && scmbillcodehisList.size()>0){
            if(scmbillcoderule!=null){
                Integer billrulenum = scmbillcoderule.getBillrulenum();
                //判断是否断号重用
                if(scmbillcoderule.getBillruledhsy().equalsIgnoreCase("Y")){
                    boolean havevalidbillcode=false;
                    for (Scmbillcodehis scmbillcodehis : scmbillcodehisList) {
                        if(scmbillcodehis.getIsvaild().equalsIgnoreCase("1"))
                        {
                            havevalidbillcode=true;
                            scmbillcodehisService.removeById(scmbillcodehis);
                            String billCodeRuleString = getBillCodeRuleString(result, scmbillcoderule);
                            result=billCodeRuleString+String.format("%0"+billrulenum+"d",scmbillcodehis.getMaxbillcode());
                            break;
                        }
                    }

                    if(!havevalidbillcode)
                    {
                        Scmbillcodehis scmbillcodehis = scmbillcodehisList.get(scmbillcodehisList.size() - 1);
                        String billcode = getBillCodeRuleString(result,scmbillcoderule);
                        Integer maxbillcode = scmbillcodehis.getMaxbillcode();
                        String format = String.format("%0" + billrulenum + "d", maxbillcode);
                        scmbillcodehis.setMaxbillcode(maxbillcode+1);
                        scmbillcodehisService.updateById(scmbillcodehis);
                        result=billcode+format;
                    }

                }else{
                    //不断号重用，直接在历史单据编号上加1
                    Scmbillcodehis scmbillcodehis = scmbillcodehisList.get(scmbillcodehisList.size() - 1);
                    String billcode = getBillCodeRuleString(result,scmbillcoderule);
                    Integer maxbillcode = scmbillcodehis.getMaxbillcode();
                    String format = String.format("%0" + billrulenum + "d", maxbillcode);
                    scmbillcodehis.setMaxbillcode(maxbillcode+1);
                    scmbillcodehisService.updateById(scmbillcodehis);
                    result=billcode+format;
                }
            }else {
                result="0000";
            }
        }else{
            //默认没有，直接取当前日期组成的信息
            if(scmbillcoderule!=null){
                result = getBillCodeRuleString(result, scmbillcoderule);
                if(scmbillcoderule.getBillrulenum()!=0){
                    String format = String.format("%0" + scmbillcoderule.getBillrulenum() + "d", 1);
                    Scmbillcodehis scmbillcodehis =new Scmbillcodehis();
                    scmbillcodehis.setObjectid(scmobject.getId());
                    scmbillcodehis.setBillcode(result);
                    scmbillcodehis.setMaxbillcode(1);
                    scmbillcodehis.setIsvaild("0");
                    scmbillcodehisService.save(scmbillcodehis);
                    result+=format;
                }
            }
        }
        return result;
    }

    private String getBillCodeRuleString(String result, Scmbillcoderule scmbillcoderule) {
        if(!scmbillcoderule.getFixedcode().equalsIgnoreCase("")){
            result += scmbillcoderule.getFixedcode();
        }
        if(scmbillcoderule.getBillruleyear().equalsIgnoreCase("Y")){
            Calendar calendar = Calendar.getInstance();
            result +=calendar.get(Calendar.YEAR);
        }
        if(scmbillcoderule.getBillrulemonth().equalsIgnoreCase("Y")){
            Calendar calendar = Calendar.getInstance();
            result +=(calendar.get(Calendar.MONTH)+1);
        }
        if(scmbillcoderule.getBillruleday().equalsIgnoreCase("Y")){
            Calendar calendar = Calendar.getInstance();
            result +=calendar.get(Calendar.DATE);
        }
        return result;
    }

    @Override
    @Transactional
    public void djDeleteButSaveBillCode(String objectCode, String billCode) {
        //查找单据规则定义
        QueryWrapper<Scmobject> scmobjectQueryWrapper=new QueryWrapper<>();
        scmobjectQueryWrapper.eq("objectcode",objectCode);
        Scmobject scmobject = scmobjectService.getOne(scmobjectQueryWrapper);
        if(scmobject!=null){
            //再获取单据编号规则
            QueryWrapper<Scmbillcoderule> scmbillcoderuleQueryWrapper=new QueryWrapper<>();
            scmbillcoderuleQueryWrapper.eq("objectid",scmobject.getId());
            Scmbillcoderule scmbillcoderule = scmbillcoderuleMapper.selectOne(scmbillcoderuleQueryWrapper);
            if(scmbillcoderule!=null){
                Integer billrulenum = scmbillcoderule.getBillrulenum();
                String substring = billCode.substring(0, billCode.length() - billrulenum);
                Scmbillcodehis scmbillcodehis=new Scmbillcodehis();
                scmbillcodehis.setObjectid(scmobject.getId());
                scmbillcodehis.setBillcode(substring);
                String canInteger = billCode.substring(billCode.length() - billrulenum);
                int i = Integer.parseInt(canInteger);
                scmbillcodehis.setMaxbillcode(i);
                scmbillcodehis.setIsvaild("1");
                scmbillcodehisService.save(scmbillcodehis);
            }
        }
    }
}
