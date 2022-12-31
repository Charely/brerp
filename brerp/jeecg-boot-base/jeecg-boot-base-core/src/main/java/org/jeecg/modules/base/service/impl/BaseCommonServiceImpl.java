package org.jeecg.modules.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.modules.base.mapper.BaseCommonMapper;
import org.jeecg.modules.base.objectEntity.CustomEntity;
import org.jeecg.modules.base.objectEntity.Scmbillcoderule;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description: common实现类
 * @author: jeecg-boot
 */
@Service
@Slf4j
public class BaseCommonServiceImpl implements BaseCommonService {

    @Resource
    private BaseCommonMapper baseCommonMapper;

    @Override
    public void addLog(LogDTO logDTO) {
        if(oConvertUtils.isEmpty(logDTO.getId())){
            logDTO.setId(String.valueOf(IdWorker.getId()));
        }
        //保存日志（异常捕获处理，防止数据太大存储失败，导致业务失败）JT-238
        try {
            logDTO.setCreateTime(new Date());
            baseCommonMapper.saveLog(logDTO);
        } catch (Exception e) {
            log.warn(" LogContent length : "+logDTO.getLogContent().length());
            log.warn(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operatetype, LoginUser user) {
        LogDTO sysLog = new LogDTO();
        sysLog.setId(String.valueOf(IdWorker.getId()));
        //注解上的描述,操作日志内容
        sysLog.setLogContent(logContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operatetype);
        try {
            //获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(IpUtils.getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        //获取登录用户信息
        if(user==null){
            try {
                user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        if(user!=null){
            sysLog.setUserid(user.getUsername());
            sysLog.setUsername(user.getRealname());
        }
        sysLog.setCreateTime(new Date());
        //保存日志（异常捕获处理，防止数据太大存储失败，导致业务失败）JT-238
        try {
            baseCommonMapper.saveLog(sysLog);
        } catch (Exception e) {
            log.warn(" LogContent length : "+sysLog.getLogContent().length());
            log.warn(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operateType) {
        addLog(logContent, logType, operateType, null);
    }

    @Override
    public void inserttotmptable(String tablename,List<String> p) {
        baseCommonMapper.createTmpTable(tablename);
        baseCommonMapper.inserttotmptable(tablename,p);
        baseCommonMapper.droptemptable(tablename);
    }

    @Override
    public void createTmpTable() {
        baseCommonMapper.createTmpTable("");
    }

    @Override
    public List<CustomEntity> selectCustomCode(String objectcode) {
        return baseCommonMapper.selectCustomCode(objectcode);
    }

    @Override
    public String selecttablevalue(String tablename, String tableid, String tableidvalue, String tablecode) {
        return baseCommonMapper.selecttablevalue(tablename,tablecode,tableid,tableidvalue);
    }

    @Override
    public String getBillCode(String objectCode) {
        String result="";

        Scmbillcoderule scmbillcoderule = baseCommonMapper.getBillCoderule(objectCode);
        if(scmbillcoderule!=null){
            if(!scmbillcoderule.getFixedcode().equalsIgnoreCase("")){
                result+=scmbillcoderule.getFixedcode();
            }
            if(scmbillcoderule.getBillruleyear().equalsIgnoreCase("Y")){
                Calendar calendar = Calendar.getInstance();
                result+=calendar.get(Calendar.YEAR);
            }
            if(scmbillcoderule.getBillrulemonth().equalsIgnoreCase("Y")){
                Calendar calendar = Calendar.getInstance();
                result+=(calendar.get(Calendar.MONTH)+1);
            }
            if(scmbillcoderule.getBillruleday().equalsIgnoreCase("Y")){
                Calendar calendar = Calendar.getInstance();
                result+=calendar.get(Calendar.DATE);
            }
            if(scmbillcoderule.getBillrulenum()!=0){
                result+="0001";
            }
        }
        return "";
    }


}
