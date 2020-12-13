package com.userinfotemplate.demo.service.serverImpl;

import com.userinfotemplate.demo.entity.Note;
import com.userinfotemplate.demo.entity.UserInfo;
import com.userinfotemplate.demo.mapper.UserBasicInfoMapper;
import com.userinfotemplate.demo.mapper.UserRoleMapper;
import com.userinfotemplate.demo.service.UserBasicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserBasicInfoServiceImpl implements UserBasicInfoService {

//    private final UserBasicInfoMapper userBasicInfoMapper;
//
//    @Autowired
//    public UserBasicInfoServiceImpl(UserBasicInfoMapper userBasicInfoMapper) {
//        this.userBasicInfoMapper = userBasicInfoMapper;
//    }

    @Autowired
    private UserBasicInfoMapper userBasicInfoMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public boolean userRegister(String username,String password,String email){
        return userBasicInfoMapper.userRegister(username,password,email);
    }

    @Override
    public Integer isUserExist(String username,String email){
        return userBasicInfoMapper.isUserExist(username,email);
    }

    @Override
    public UserInfo getUserInfoByNameOrEmail(String userinfo){
        return userBasicInfoMapper.getUserInfoByNameOrEmail(userinfo);
    }

    @Override
    public  Integer getUserIdByName(String username){
        return userBasicInfoMapper.getUserIdByName(username);
    }

    //用户角色
    @Override
    public boolean setUserRole(Integer user_id,Integer role_id){
        return userRoleMapper.setUserRole(user_id,role_id);
    }

    @Override
    public String getUserRoleName(Integer user_id){
        return userRoleMapper.getUserRoleName(user_id);
    }

}
