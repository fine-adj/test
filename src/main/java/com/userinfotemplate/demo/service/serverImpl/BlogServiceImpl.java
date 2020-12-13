package com.userinfotemplate.demo.service.serverImpl;

import com.userinfotemplate.demo.entity.Blog;
import com.userinfotemplate.demo.mapper.BlogMapper;
import com.userinfotemplate.demo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<Blog> getBlogsByUserId(Integer user_id,Integer startIndex,Integer step){
        return blogMapper.getBlogsByUserId(user_id,startIndex,step);
    }

    //向数据库中存入一条新博客
    @Override
    public Integer saveNewBlog(Blog blog){
        return blogMapper.saveNewBlog(blog);
    }

    @Override
    public Integer blogCount(Integer user_id){
        return blogMapper.blogCount(user_id);
    }

    //分页查询
    @Override
    public List<Blog> getBlogsByPage(Integer user_id, Integer startIndex, Integer step){
        return blogMapper.getBlogsByPage(user_id,startIndex,step);
    }

    //查博客分类
    @Override
    public List<String> getBlogType(Integer user_id){
        return blogMapper.getBlogType(user_id);
    }

    //按照博客分类标签查询博客
    @Override
    public List<Blog> getBlogByType(Integer user_id,String blog_type){
        return blogMapper.getBlogByType(user_id,blog_type);
    }

    //按照标签查询博客总数
    @Override
    public Integer getblogCountByType(Integer user_id,String blog_type){
        return blogMapper.getblogCountByType(user_id,blog_type);
    }

    //博客模糊搜索
    @Override
    public List<Blog> getBlogSearchByKeywords(Integer user_id,String blogSearchKeywords){
        return blogMapper.getBlogSearchByKeywords(user_id,blogSearchKeywords);
    }

    @Override
    public List<Blog> getAllBlogsByUserId(Integer user_id){
        return blogMapper.getAllBlogsByUserId(user_id);
    }

}
