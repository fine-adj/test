package com.userinfotemplate.demo.controller;


import com.userinfotemplate.demo.dto.AjaxResultDTO;
import com.userinfotemplate.demo.entity.UserInfo;
import com.userinfotemplate.demo.service.UserBasicInfoService;
import com.userinfotemplate.demo.utils.CheckCode;
import com.userinfotemplate.demo.utils.EmailUtil;
import com.userinfotemplate.demo.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserLoginController {

    //@Autowired注入bean，相当于spring在xml配置文件中配置<bean>，并且使用setter注入。
    //DI依赖注入的三种方式：属性注入，构造函数注入，setter方法注入。
    //Java变量的初始化顺序：静态变量或静态语句块–>实例变量或初始化语句块–>构造方法–>@Autowired
    private final UserInfo userInfo;
    private final UserBasicInfoService userBasicInfoService;

    //spring 4.0开始就不推荐使用属性注入，改为推荐构造器注入和setter注入。
    @Autowired
    public UserLoginController(UserInfo userInfo, UserBasicInfoService userBasicInfoService) {
        this.userInfo = userInfo;
        this.userBasicInfoService = userBasicInfoService;
    }

    //新用户注册：用户名，密码，邮箱验证码，用户名和邮箱不可重复
    //点击注册按钮：先验证用户名是否已经更换，再验证邮箱验证码是否正确
    @PostMapping("register")
    @ResponseBody  //返回前端纯数据，要在方法上加注解@ResponseBody
    public AjaxResultDTO userRegister(@RequestParam("username")String username,
                                      @RequestParam("password")String password, @RequestParam("email")String email,
                                      @RequestParam("checkcode")String checkcode){
        //Controller层与前端交互：封装了一个DTO层，用来返回状态码，数据，说明信息等。
        AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
        //校验传入参数: 有一个参数为空，就返回失败
        if(username.equals("") || password.equals("") || email.equals("")||checkcode.equals("")){
            ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
            ajaxResultDTO.setMessage("register information is not allowed null!");
            return ajaxResultDTO;
        }else {
            //从缓存中读取验证码(官方推荐java使用redis通过Jedis，但springboot2.0整合了redis通过lettuce)
            Jedis jedis = new Jedis("x.x.x.x", 6379);
            System.out.println(jedis.ping());
            String checkUsername = jedis.hget(username+"_register",username+"_username");
            String realCheckCode = jedis.hget(username+"_register",email+"_email");
            jedis.close();
            //或者将验证码返回给前端，前端验证更合适。
            if(realCheckCode == null || checkUsername == null){  //如果更改了注册用户名就不会从redis中查询到
                ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
                ajaxResultDTO.setMessage("redis check is null! register fail!");
            } else if(realCheckCode.equals(checkcode)){
                //注册成功：操作数据库：将用户注册信息插入用户表后，查询该用户的user_id，操作user_role表进行角色赋予
                String code_password = MD5.getMD5(password);      //密码加密
                userBasicInfoService.userRegister(username,code_password,email);  //存入表userbasicinfo
                //查询user_id字段，并操作user_role表，赋予新用户普通用户权限
                Integer user_id = userBasicInfoService.getUserIdByName(username);
                //操作user_role表: 管理员-0；普通用户-1；会员-2
                boolean user_role_flag = userBasicInfoService.setUserRole(user_id,1);
                if(user_role_flag){
                    ajaxResultDTO.state = AjaxResultDTO.STATE.SUCCESS;
                    ajaxResultDTO.setMessage("register success!");
                }
            }
            else {
                ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
                ajaxResultDTO.setMessage("register fail!");
            }
            return ajaxResultDTO;
        }
    }

    //点击发送验证码按钮
    @PostMapping("email_check")
    @ResponseBody
    public AjaxResultDTO emailCheck(@RequestParam("username")String username,
                                    @RequestParam("password")String password,@RequestParam("email")String email){
//        System.out.println("前端传来的："+username+","+password+","+email);
        AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
        if(username.equals("") || password.equals("") || email.equals("")){
            ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
            ajaxResultDTO.setMessage("user information is not allowed null!");
            return ajaxResultDTO;
        }else {
            //校验邮箱的合法性
            boolean isEmailVaild = CheckCode.checkEmail(email);
            if(!isEmailVaild){
                ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
                ajaxResultDTO.setMessage("email is not vaild!");
                return ajaxResultDTO;
            }else {
                //先验证用户名和邮箱是否已经被注册
                int userCount = userBasicInfoService.isUserExist(username,email);
                if(userCount > 0){
                    ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
                    ajaxResultDTO.setMessage("username or email is existed!");
                    return ajaxResultDTO;
                }else {
                    //随机生成验证码，存入redis，并向邮箱发送验证码
                    String check_code = CheckCode.getCheckCode();           //得到随机的验证码
                    //发邮件
                    userInfo.setUsername(username);
                    userInfo.setEmail(email);
                    try{
                        EmailUtil.send(userInfo,check_code);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //邮件发送成功后，将验证码存入redis开始计时，防止发邮件时延导致的计时不够60s
                    Jedis jedis = new Jedis("x.x.x.x", 6379);
                    Map<String,String> map = new HashMap<>();
                    //使用动态的变量拼接的key，查询时，如果有变更，直接查询失败，可省去后面的判断
                    map.put(username+"_username",username);
                    map.put(email+"_email",check_code);
                    jedis.hmset(username+"_register",map); //redis中hash格式存储： 用户名_register: {"username":"用户名"，"邮箱_email":"4位验证码"}
                    jedis.expire(username+"_register",120);            //将验证码存入redis，并开始定时
                    jedis.close();
                    ajaxResultDTO.state = AjaxResultDTO.STATE.SUCCESS;
                    return ajaxResultDTO;
                }

            }
        }
    }

    //用户登录：可以通过用户名或者邮箱登录
    @PostMapping("login")
    @ResponseBody
    public AjaxResultDTO userLogin(@RequestParam("userlogininfo")String userlogininfo,@RequestParam("password")String password){
        //前端判断一下两个参数都不为空
        AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();

        if(userlogininfo.equals("")||password.equals("")){
            ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
            ajaxResultDTO.setMessage("information is null!");
            return ajaxResultDTO;
        }else {
            String code_password = MD5.getMD5(password);
            //先按照userlogininfo查一下是否存在,存在之后再校验密码是否正确
            UserInfo userInfo = userBasicInfoService.getUserInfoByNameOrEmail(userlogininfo);  //登录成功后将用户基本信息返回给前端，用于cookie的记录
            if(userInfo == null){
                ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
                ajaxResultDTO.setMessage("user is not exist!");
            }else {
                if(userInfo.getPassword().equals(code_password)){
                    //此处表示可以登录成功
                    //要将用户的角色返回给前端显示出来，根据user_id，去user_role表查询角色
                    String role_name = userBasicInfoService.getUserRoleName(userInfo.getUser_id());
                    System.out.println("用户角色为："+role_name);
                    //构造一个list，返回给前端用户角色，用户名，邮箱
                    List<String> list = new ArrayList<>();
                    list.add("user_id="+userInfo.getUser_id());
                    list.add("username="+userInfo.getUsername());
                    list.add("user_role="+role_name);
                    list.add("email="+userInfo.getEmail());
                    ajaxResultDTO.setData(list);
                    ajaxResultDTO.state = AjaxResultDTO.STATE.SUCCESS;
                    ajaxResultDTO.setMessage("login success!");
                }else {
                    ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
                    ajaxResultDTO.setMessage("password is error!");
                }
            }
            return ajaxResultDTO;
        }
    }


    //进入系统主页，来到登录页面，直接刷新出验证码
//    @GetMapping("index")
//    @ResponseBody
//    public Object[] getImageCode(){
//        Object[] imagecode= ImageUtil.createImg();
//        for(int i=0;i<imagecode.length;i++){
//            System.out.println(imagecode[i]);
//        }
//        return imagecode;
//    }
}
