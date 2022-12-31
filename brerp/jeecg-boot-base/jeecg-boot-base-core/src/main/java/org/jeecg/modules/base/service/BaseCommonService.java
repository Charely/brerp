package org.jeecg.modules.base.service;

import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.base.objectEntity.CustomEntity;

import java.util.List;

/**
 * common接口
 * @author: jeecg-boot
 */
public interface BaseCommonService {

    /**
     * 保存日志
     * @param logDTO
     */
    void addLog(LogDTO logDTO);

    /**
     * 保存日志
     * @param logContent
     * @param logType
     * @param operateType
     * @param user
     */
    void addLog(String logContent, Integer logType, Integer operateType, LoginUser user);

    /**
     * 保存日志
     * @param logContent
     * @param logType
     * @param operateType
     */
    void addLog(String logContent, Integer logType, Integer operateType);

    void inserttotmptable(String tableName,List<String> p);

    void createTmpTable();

    List<CustomEntity> selectCustomCode(String objectcode);

    String selecttablevalue(String tablename,String tableid,String tableidvalue,String tablecode);


    String getBillCode(String objectCode);

}
