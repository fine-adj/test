package com.userinfotemplate.demo.controller;

import com.userinfotemplate.demo.dto.AjaxResultDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/*
* 用户角色权限的处理：
* 有3个角色：1管理员【提前为这个系统设置好，功能是：当某个用户申请升级为会员后，为用户进行角色的转变，并授予相应权限】
* 新注册的用户角色都是2普通用户，在将注册信息存入数据库时直接赋予一个默认的角色的值
* 普通用户在前端点击申请3会员按钮时，发送请求，后台统计讨论区所有发帖和回帖数量，决定是否能升级为会员
* 权限分为类别1，2，3：【仅对角色2与角色3在可使用功能上的差别做处理，其他共同功能不做处理】
*                   权限1表示最多可新建自己的5个博客，第6个博客新建失败；无法收藏别人博客；【暂时先这两个】
 */
//为系统设置一个管理员：用户名为admin,密码为123456,邮箱为xxx.com,user_id=5,role_id=0
//管理员操作【为管理员身份登录单独跳转到权限管理页面】
@Controller
public class UserRoleController {

    //普通用户请求升级为会员
    @PostMapping("/requestVIP")
    @ResponseBody
    public AjaxResultDTO userRequestVIP(@RequestParam("username")String username){
        System.out.println("我要申请VIP，我叫"+username);
        //查询
        return null;
    }

}
