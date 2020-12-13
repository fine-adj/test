package com.userinfotemplate.demo.mapper;

import com.userinfotemplate.demo.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.List;

@Mapper
@Component("BlogMapper")
public interface BlogMapper {

    //根据user_id从数据库中查询该用户的所有博客
    List<Blog> getBlogsByUserId(@Param("user_id") Integer user_id, @Param("startIndex") Integer startIndex,@Param("step") Integer step);


    //向数据库中存入一条新博客
//    Integer saveNewBlog(@Param("blog_type") String blog_type, @Param("create_time") Date create_time,
//                        @Param("blog_addr") String blog_addr, @Param("user_id") Integer user_id, @Param("blog_title") String blog_title);

    Integer saveNewBlog(Blog blog);

    //查询这个用户所有博客的数量
    Integer blogCount(Integer user_id);

    //分页查询
    List<Blog> getBlogsByPage(@Param("user_id") Integer user_id, @Param("startIndex") Integer startIndex,@Param("step") Integer step);

    //查博客分类
    List<String> getBlogType(Integer user_id);

    //按照博客分类标签查询博客
    List<Blog> getBlogByType(@Param("user_id") Integer user_id,@Param("blog_type") String blog_type);

    //按照标签查询博客总数
    Integer getblogCountByType(@Param("user_id")Integer user_id,@Param("blog_type") String blog_type);

    //博客模糊搜索
    List<Blog> getBlogSearchByKeywords(@Param("user_id")Integer user_id,@Param("blogSearchKeywords")String blogSearchKeywords);

    List<Blog> getAllBlogsByUserId(@Param("user_id") Integer user_id);

}
