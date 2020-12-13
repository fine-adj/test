package com.userinfotemplate.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String login(){

        return "login.html";
    }

    @RequestMapping("/register")
    public String register(){
        return "register.html";
    }

    @RequestMapping("/userpage")
    public String userpage(){
        return "userpage.html";
    }

    @RequestMapping("/blog")
    public String blog(){
        return "blog.html";
    }

    @RequestMapping("/blogdetail")
    public String blogdetail(){
        return "blogdetail.html";
    }

    @RequestMapping("/errorpage")
    public String error(){
        return "error.html";
    }

    @RequestMapping("/bloglist")
    public String blogList(){
        return "bloglist.html";
    }

}
