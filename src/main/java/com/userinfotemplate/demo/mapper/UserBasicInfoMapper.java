package com.userinfotemplate.demo.mapper;

import com.userinfotemplate.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Mapper;
/*
* @Description:与数据库交互
* */

@Mapper
@Component("UserBasicInfoMapper")
public interface UserBasicInfoMapper {

    boolean userRegister(@Param("username") String username, @Param("password") String password, @Param("email") String email);

    Integer isUserExist(String username,String email);

    UserInfo getUserInfoByNameOrEmail(String userinfo);

    Integer getUserIdByName(String username);
}
