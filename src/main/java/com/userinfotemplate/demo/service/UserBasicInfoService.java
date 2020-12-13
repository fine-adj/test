package com.userinfotemplate.demo.service;

import com.userinfotemplate.demo.entity.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserBasicInfoService {
    boolean userRegister(String username,String password,String email);

    Integer isUserExist(String username,String email);

    UserInfo getUserInfoByNameOrEmail(String userinfo);

    Integer getUserIdByName(String username);

    //设置用户角色
    boolean setUserRole(Integer user_id,Integer role_id);

    //根据用户id去用户角色表查询角色名称
    String getUserRoleName(Integer user_id);
}
