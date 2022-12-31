package org.jeecg.modules.system.flow;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.flowable.apithird.entity.SysCategory;
import org.jeecg.modules.flowable.apithird.entity.SysRole;
import org.jeecg.modules.flowable.apithird.entity.SysUser;
import org.jeecg.modules.flowable.apithird.service.IFlowThirdService;
import org.jeecg.modules.system.service.impl.SysRoleServiceImpl;
import org.jeecg.modules.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowThirdServiceImpl implements IFlowThirdService {
    @Autowired
    ISysBaseAPI sysBaseAPI;
    @Autowired
    SysUserServiceImpl sysUserService;
    @Autowired
    SysRoleServiceImpl sysRoleService;
    @Override
    public SysUser getLoginUser() {
        LoginUser sysUser = null;
        SysUser copyProperties = null;
        try {
            sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            copyProperties = BeanUtil.copyProperties(sysUser, SysUser.class);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return copyProperties;
    }

    @Override
    public List<SysUser> getAllUser() {
        List<org.jeecg.modules.system.entity.SysUser> list = sysUserService.list();
        List<SysUser> userList = list.stream().map(o -> BeanUtil.copyProperties(o, SysUser.class)).collect(Collectors.toList());
        return userList;
    }

    @Override
    public List<SysUser> getUsersByRoleId(String roleId) {
        Page<org.jeecg.modules.system.entity.SysUser> page = new Page<>(1,Integer.MAX_VALUE);
        IPage<org.jeecg.modules.system.entity.SysUser> userByRoleId = sysUserService.getUserByRoleId(page, roleId, null);
        List<org.jeecg.modules.system.entity.SysUser> records = userByRoleId.getRecords();
        List<SysUser> userList = records.stream().map(o -> BeanUtil.copyProperties(o, SysUser.class)).collect(Collectors.toList());
        return userList;
    }


    @Override
    public SysUser getUserByUsername(String username) {
        LoginUser userByName = sysBaseAPI.getUserByName(username);
        return userByName==null?null:BeanUtil.copyProperties(userByName, SysUser.class);
    }

    @Override
    public List<SysRole> getAllRole() {
        List<org.jeecg.modules.system.entity.SysRole> list = sysRoleService.list();
        List<SysRole> roleList = list.stream().map(o -> BeanUtil.copyProperties(o, SysRole.class)).collect(Collectors.toList());
        return roleList;
    }
    @Override
    public List<SysCategory> getAllCategory() {
        List<SysCategory> res=new ArrayList<>();

        List<DictModel> dictModels = sysBaseAPI.queryTableDictItemsByCode("scmflowcategory", "categoryname", "categorycode");
        if(dictModels!=null){
            for (DictModel dictModel : dictModels) {
                SysCategory category=new SysCategory();
                category.setId(dictModel.getValue());
                category.setName(dictModel.getText());
                res.add(category);
            }

        }
        return res;

    }

    @Override
    public List<String> getDepartNamesByUsername(String username) {
        List<String> departNamesByUsername = sysBaseAPI.getDepartNamesByUsername(username);
        return departNamesByUsername;
    }
}
