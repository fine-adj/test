package com.userinfotemplate.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class Blog {

    private Integer blog_id;
    private String blog_type;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date create_time;
    private String blog_addr;
    private Integer user_id;
    private String blog_title;
    private String blog_content;            //表中没有的字段
    private Integer blogCount;              //表中没有的字段

    public Integer getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(Integer blogCount) {
        this.blogCount = blogCount;
    }

    public String getBlog_content() {
        return blog_content;
    }

    public void setBlog_content(String blog_content) {
        this.blog_content = blog_content;
    }

    public Integer getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(Integer blog_id) {
        this.blog_id = blog_id;
    }

    public String getBlog_type() {
        return blog_type;
    }

    public void setBlog_type(String blog_type) {
        this.blog_type = blog_type;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getBlog_addr() {
        return blog_addr;
    }

    public void setBlog_addr(String blog_addr) {
        this.blog_addr = blog_addr;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getBlog_title() {
        return blog_title;
    }

    public void setBlog_title(String blog_title) {
        this.blog_title = blog_title;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blog_id=" + blog_id +
                ", blog_type='" + blog_type + '\'' +
                ", create_time=" + create_time +
                ", blog_addr='" + blog_addr + '\'' +
                ", user_id=" + user_id +
                ", blog_title='" + blog_title + '\'' +
                '}';
    }
}
