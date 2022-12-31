package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.Constant.PeriodState;
import org.br.erp.base.entity.IMperiod;
import org.br.erp.base.entity.Mvperiod;
import org.br.erp.base.mapper.IMperiodMapper;
import org.br.erp.base.mapper.MvperiodMapper;
import org.br.erp.base.service.IIMperiodService;
import org.br.erp.base.service.IMvperiodService;
import org.br.erp.base.utils.ERPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: 库存期间
 * @Author: jeecg-boot
 * @Date:   2022-11-27
 * @Version: V1.0
 */
@Service
public class IMperiodServiceImpl extends ServiceImpl<IMperiodMapper, IMperiod> implements IIMperiodService {

    @Autowired
    private IMperiodMapper mvperiodMapper;

    @Override
    @Transactional
    public void addIMPerioid(String companyid) {
        String nowDate = ERPUtils.getDateString(new Date(),"yyyyMM");
        QueryWrapper<IMperiod> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("perioddate",nowDate.substring(0,4));
        queryWrapper.eq("companyid",companyid);
        List<IMperiod> mvperiods = mvperiodMapper.selectList(queryWrapper);
        if(!(mvperiods!=null && mvperiods.size()>=12)){
            //todo 创建12行期间
            for(int i=1;i<13;i++){
                IMperiod mvperiod=new IMperiod();
                mvperiod.setCompanyid(companyid);
                mvperiod.setPeriodstate(PeriodState.init.toString());
                //获取月份
                String month=String.valueOf(i);
                if(i<10){
                    month="0"+i;
                }
                mvperiod.setPerioddate(nowDate.substring(0,4)+month);
                mvperiod.setFromdate(ERPUtils.getFirstDay(Integer.parseInt(nowDate.substring(0,4)),Integer.parseInt(month),""));
                mvperiod.setEnddate(ERPUtils.getLastDayOfMonth(Integer.parseInt(nowDate.substring(0,4)),Integer.parseInt(month)));
                mvperiodMapper.insert(mvperiod);
            }
        }
    }

    /**
     * 更新期间状态
     * @param companyid 公司
     * @param perioddate 期间
     * @param periodState 期间状态
     */
    @Override
    public void changeIMPeriodState(String companyid, String perioddate, String periodState) {
        //todo 查看当前期间状态
        QueryWrapper<IMperiod> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("companyid",companyid);
        queryWrapper.eq("perioddate",perioddate);
        List<IMperiod> mvperiods = mvperiodMapper.selectList(queryWrapper);
        String curPeriodState="";
        IMperiod currentPerioid=null;
        if(mvperiods!=null && mvperiods.size()>0){
             currentPerioid = mvperiods.get(0);
            curPeriodState=currentPerioid.getPeriodstate();
        }else{
            return;
        }
        if(curPeriodState.equalsIgnoreCase(periodState)){
            return;
        }
        //todo 将初始状态更新为打开状态
        if(periodState.equalsIgnoreCase(PeriodState.open.toString())){
            //判断当前是否是初始状态
//            if(!(curPeriodState.equalsIgnoreCase("init")&&curPeriodState.equalsIgnoreCase("stopped"))){
//                throw new RuntimeException("当前期间不是初始或关账，不允许打开");
//             }
            if(!(curPeriodState.equalsIgnoreCase("init") || curPeriodState.equalsIgnoreCase("future"))){
                throw new RuntimeException("当前期间不是初始状态，不允许打开");
            }
            //将当前的期间状态更新为打开
            currentPerioid.setPeriodstate(PeriodState.open.toString());
            mvperiodMapper.updateById(currentPerioid);

            //判断下一个期间是否是月结或者关账，如果是则不允许打开
            QueryWrapper<IMperiod> nextQueryWrapper=new QueryWrapper<>();
            nextQueryWrapper.eq("companyid",companyid);
            String year = perioddate.substring(0, 4);
            String month = perioddate.substring(4, 6);
            int i = Integer.parseInt(month);
            if(i+1<10){
                month="0"+(i+1);
            }else{
                month=String.valueOf(i+1);
            }
            nextQueryWrapper.eq("perioddate",year+month);
            nextQueryWrapper.and(item->item.eq("periodstate",PeriodState.open).or(a->a.eq("periodstate",PeriodState.stopped))
                    .or(b->b.eq("periodstate",PeriodState.closed)));

            List<IMperiod> nextPeriods = mvperiodMapper.selectList(nextQueryWrapper);
            if(nextPeriods!=null && nextPeriods.size()>0){
                throw new RuntimeException("存在未来期间已打开或关闭，不允许打开现有期间");
            }

            //将当前期间之后的状态更新为未来
            if(Integer.parseInt(month) < 13){
                //将未来期间为未来
                 for(int j=Integer.parseInt(month);j<13;j++){
                     QueryWrapper<IMperiod> futureQueryWrapper=new QueryWrapper<>();
                     futureQueryWrapper.eq("companyid",companyid);
                     String jmonth="";
                     if(j<10){
                         jmonth="0"+j;
                     }else{
                         jmonth=String.valueOf(j);
                     }
                     futureQueryWrapper.eq("perioddate",year+jmonth);
                     List<IMperiod> futurePeriods= mvperiodMapper.selectList(futureQueryWrapper);
                     if(futurePeriods!=null && futurePeriods.size()>0){
                         IMperiod futureperiod = futurePeriods.get(0);
                         futureperiod.setPeriodstate(PeriodState.future.toString());
                         mvperiodMapper.updateById(futureperiod);
                     }
                 }
            }

            //将之前的期间更新为关闭
            if(Integer.parseInt(month) >0){
                for(int k=1;k<Integer.parseInt(month)-1;k++){
                    QueryWrapper<IMperiod> postQueryWrapper=new QueryWrapper<>();
                    postQueryWrapper.eq("companyid",companyid);
                    String kmonth="";
                    if(k<10){
                        kmonth="0"+k;
                    }else{
                        kmonth=String.valueOf(k);
                    }
                    postQueryWrapper.eq("perioddate",year+kmonth);
                    List<IMperiod> postPeriods = mvperiodMapper.selectList(postQueryWrapper);
                    if(postPeriods!=null && postPeriods.size()>0){
                        IMperiod postperiod = postPeriods.get(0);
                        if(postperiod.getPeriodstate().equalsIgnoreCase("open")){
                            continue;
                        }
                        postperiod.setPeriodstate(PeriodState.closed.toString());
                        mvperiodMapper.updateById(postperiod);
                    }
                }
            }
        }
        else if(periodState.equalsIgnoreCase(PeriodState.stopped.toString())){

        }
    }

    @Override
    public boolean checkPeriodState(String companyid, String periodDate) {

        QueryWrapper<IMperiod> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("companyid",companyid);
        queryWrapper.eq("perioddate",periodDate);
        List<IMperiod> mvperiods = mvperiodMapper.selectList(queryWrapper);
        if(mvperiods!=null && mvperiods.size()>0){
            IMperiod mvperiod = mvperiods.get(0);
            String periodstate = mvperiod.getPeriodstate();
            if(periodstate.equalsIgnoreCase(PeriodState.open.toString())){
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }


}
