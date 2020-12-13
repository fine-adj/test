package com.userinfotemplate.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component("UserRoleMapper")
public interface UserRoleMapper {
    boolean setUserRole(Integer user_id,Integer role_id);

    String getUserRoleName(Integer user_id);
}
