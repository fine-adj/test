package com.userinfotemplate.demo.service;

import com.userinfotemplate.demo.entity.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface BlogService {

    //根据user_id从数据库中查询该用户的所有博客
    List<Blog> getBlogsByUserId(Integer user_id,Integer startIndex,Integer step);

    List<Blog> getAllBlogsByUserId(Integer user_id);

    //向数据库中存入一条新博客
    Integer saveNewBlog(Blog blog);

    //查询这个用户所有博客的数量
    Integer blogCount(Integer user_id);


    //分页查询
    List<Blog> getBlogsByPage(@Param("user_id") Integer user_id, @Param("startIndex") Integer startIndex,@Param("step") Integer step);

    //查博客分类
    List<String> getBlogType(Integer user_id);

    //按照博客分类标签查询博客
    List<Blog> getBlogByType(Integer user_id,String blog_type);

    //按照标签查询博客总数
    Integer getblogCountByType(@Param("user_id")Integer user_id,@Param("blog_type") String blog_type);

    //博客模糊搜索
    List<Blog> getBlogSearchByKeywords(Integer user_id,String blogSearchKeywords);
}
