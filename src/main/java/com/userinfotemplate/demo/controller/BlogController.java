package com.userinfotemplate.demo.controller;

import com.userinfotemplate.demo.dto.AjaxResultDTO;
import com.userinfotemplate.demo.entity.Blog;
import com.userinfotemplate.demo.entity.Note;
import com.userinfotemplate.demo.service.BlogService;
import com.userinfotemplate.demo.service.NoteService;
import java.lang.Object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
* 笔记+博客
* */

@Controller
public class BlogController {

    private final NoteService noteService;
    private final BlogService blogService;
    private Logger logger = LoggerFactory.getLogger(BlogController.class);
    private List<Blog> allBlogList;
    private final BlogIOController blogIOController;

    @Autowired
    public BlogController(NoteService noteService, BlogService blogService,BlogIOController blogIOController){
        this.noteService = noteService;
        this.blogService = blogService;
        this.blogIOController = blogIOController;
    }

    //来到用户主页后根据user_id查到这个用户的所有博客，通过limit多次访问数据库的读取方式
//    @RequestMapping("/userpage/getblog/")
//    @ResponseBody
//    public List<Object> getUserBlog(@RequestParam("user_id")Integer user_id,HttpSession session)throws Exception{
//        List<Object> objectList = new ArrayList<>();
//        AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
//        List<Blog> blogList = blogService.getBlogsByUserId(user_id,0,3); //默认来到这个页面先展示3条博客
////        System.out.println(blogList);
//        List<List<String>>  blogContentlist = getBlogContent(user_id);
//        Integer blogCount = blogService.blogCount(user_id);
//        for(int i=0;i<blogList.size();i++){
//            blogList.get(i).setBlog_content(blogContentlist.get(i).toString());
////            System.out.println(blogList.get(i).getBlog_content());
//        }
////        System.out.println(blogList);
//        if(blogList != null){
//            ajaxResultDTO.state = AjaxResultDTO.STATE.SUCCESS;
//            ajaxResultDTO.setData(blogList);
//        }else {
//            ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
//        }
//        objectList.add(ajaxResultDTO);
//        objectList.add(blogCount);
//        session.setAttribute("blogCount","共"+blogCount+"条博客");
//        return objectList;
//    }

    //返回值是分页查询的，多个博客的内容list，每个元素是一个博客文件中的数据
    public List<List<String>>  getBlogContent(Integer user_id) throws Exception{
        //这里不应该是0,3，否则每次遍历的时候拿到的都是前3个地址，读到的都是前3个地址的内容
//        List<Blog> blogList = blogService.getBlogsByUserId(user_id,0,3);
        List<Blog> blogList = blogService.getAllBlogsByUserId(user_id);
        List<String> blog_content;
        List<List<String>> blog_content_list = new ArrayList<>();
        //增强for更适合于不知道循环次数，和复杂数据类型的循环
        for (Blog blog:blogList) {
            System.out.println(blog.getBlog_addr());
            CompletableFuture<List<String>> completableFuture = blogIOController.getBlogContentByAddr(blog.getBlog_addr());    //通过从数据库查到的每条数据拿到blog_addr
            blog_content = completableFuture.get();
            blog_content_list.add(blog_content);
        }
        return blog_content_list;
    }


    //在写博客页面，点击保存博客就一定是新创建的
    @RequestMapping("/saveblog")
    @ResponseBody
    public AjaxResultDTO saveBlog(@RequestParam("user_id")Integer user_id,@RequestParam("blog_content")String blog_content,
                     @RequestParam("blog_type")String blog_type,@RequestParam("blog_title")String blog_title){
        Date date = new Date();
        AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
        CompletableFuture<String> completableFuture = blogIOController.setBlogContent(user_id,blog_content,blog_type,blog_title);
        try {
           String filepath = completableFuture.get();
            //将博客内容写入文件后，存入数据库
            //存入数据库
            Blog blog = new Blog();
            blog.setBlog_type(blog_type);
            blog.setBlog_addr(filepath);
            blog.setBlog_title(blog_title);
            blog.setUser_id(user_id);
            blog.setCreate_time(date);
            Integer saveFlag = blogService.saveNewBlog(blog);
            if(saveFlag>0){           //1表示insert语句执行成功
                ajaxResultDTO.state = AjaxResultDTO.STATE.SUCCESS;
                ajaxResultDTO.setData(blog.getBlog_id());
            }else {
                ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
            }

        }catch (InterruptedException i){
            i.printStackTrace();
        }catch (ExecutionException e){
            e.fillInStackTrace();
        }
        return ajaxResultDTO;
    }

    //点击保存按钮
    //新建便签
    @PostMapping("/savenote")
    @ResponseBody
    public AjaxResultDTO createNote(@RequestParam("user_id")Integer user_id, @RequestParam("note_type")String note_type,
                                    @RequestParam("note_content")String note_content){
        AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
        //获取系统提交保存便签的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //存入数据库
        Note note = noteService.createNote(user_id,sdf.format(new Date()),note_type,note_content);
        if(note != null){
            //拿到这个便签的id给前端
            ajaxResultDTO.setData(note.getNote_id());
            ajaxResultDTO.state = AjaxResultDTO.STATE.SUCCESS;
        }else{
            ajaxResultDTO.setData(null);
            ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
        }
        return ajaxResultDTO;
    }

    //便签保存成功后，提示保存成功，有“返回我的主页”和“立即查看”
    @RequestMapping("/looknote")
    @ResponseBody
    public AjaxResultDTO lookNote(@RequestParam("note_id")Integer node_id){
        AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
        Note note = noteService.getNoteById(node_id);
        if(note == null){
            ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
        }else {
            ajaxResultDTO.setData(note);
            ajaxResultDTO.state = AjaxResultDTO.STATE.SUCCESS;
        }
        return ajaxResultDTO;
    }

    //分页：前端点击第几页，来返回几条数据，默认一页显示3个博客
    @RequestMapping("/toPage")
    @ResponseBody
    public List<Blog> mypage(@RequestParam("user_id")Integer user_id,@RequestParam("page")Integer page) throws Exception{
//    public List<Blog> mypage(@RequestParam("user_id")Integer user_id,@RequestParam(value = "page", required = false, defaultValue = "1")Integer page,
//                       @RequestParam(value = "pageSize", required = false, defaultValue = "2") int pageSize){
        //每次查询条数，默认是3
        System.out.println("page:"+page);
        //就返回从page到page+3
        Integer step = 3;
        //查询数据库的起始行数
//        Integer startIndex = page + ((page-1)*4 - 1);     //步长是5的算法
        Integer startIndex = (page-1)*3;                    //步长是3的算法
//        System.out.println("startIndex="+startIndex);
//        List<Blog> blogList = blogService.getBlogsByPage(user_id,startIndex,step);   //按照分页查询的博客列表
//        List<List<String>> blogContentlist = getBlogContent(user_id);
//        for(int i=0;i<blogList.size();i++){
//            blogList.get(i).setBlog_content(blogContentlist.get(i).toString());
//        }
//        System.out.println(blogList);
        List<Blog> tempBlogList = new ArrayList<>();
        if (startIndex+1 <= this.allBlogList.size()&&startIndex+2 <= this.allBlogList.size()&&startIndex+3 <= this.allBlogList.size()){
            tempBlogList.add(this.allBlogList.get(startIndex));
            tempBlogList.add(this.allBlogList.get(startIndex+1));
            tempBlogList.add(this.allBlogList.get(startIndex+2));

        }
        return tempBlogList;
    }

    //查询博客分类标签
    @RequestMapping("/getBlogType")
    @ResponseBody
    public List<String> getBlogType(@RequestParam("user_id")Integer user_id){
        return blogService.getBlogType(user_id);
    }

    //按照标签获得博客总数
    @RequestMapping("/getBlogByType")
    @ResponseBody
    public List<Object> getBlogCountByType(@RequestParam("user_id")Integer user_id,@RequestParam("blog_type")String blog_type) throws  Exception{
        Integer blogCount = blogService.getblogCountByType(user_id,blog_type);   //查博客总数，用于计算页码
        List<Blog> blogList = blogService.getBlogByType(user_id,blog_type);    //查博客在数据库存的数据
        //根据博客地址，查询博客内容
        List<String>  blogContentList ;         //
        List<List<String>> blogsContentList = new ArrayList<>();
        //根据每个博客地址从一个文件中读取获得一个博客的内容，并add到list中
        for (int i=0;i<blogList.size();i++){
            CompletableFuture<List<String>> completableFuture = blogIOController.getBlogContentByAddr(blogList.get(i).getBlog_addr());
            blogContentList = completableFuture.get();
            blogsContentList.add(blogContentList);
        }
        //同时遍历博客列表和从数据库查到的数据列表，将从文件读取的博客内容set到Blog对象中
        for(int i=0;i<blogList.size();i++){
            blogList.get(i).setBlog_content(blogsContentList.get(i).toString());
        }
        List<Object> objectList = new ArrayList<>();
        objectList.add(blogCount);
        objectList.add(blogList);
        return objectList;              //返回给前端[查到的博客总数，包含博客内容的博客列表]
    }

    //搜索博客
    @RequestMapping("/searchBlogByKeywords")
    @ResponseBody
    public AjaxResultDTO searchBlogByKeywords(@RequestParam("user_id")Integer user_id,@RequestParam("blogSearchKeywords")String blogSearchKeywords)throws  Exception{
        List<Blog> blogList = blogService.getBlogSearchByKeywords(user_id,blogSearchKeywords);
//        System.out.println("模糊搜索："+blogList);
        AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
        List<String>  blogContentList ;         //
        List<List<String>> blogsContentList = new ArrayList<>();
        if(blogList == null || blogList.isEmpty()){
            ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
        }else {
            //根据blog_addr读内容
            //根据每个博客地址从一个文件中读取获得一个博客的内容，并add到list中
            for (int i=0;i<blogList.size();i++){
                CompletableFuture<List<String>> completableFuture = blogIOController.getBlogContentByAddr(blogList.get(i).getBlog_addr());
                blogContentList = completableFuture.get();
                blogsContentList.add(blogContentList);
            }
            //同时遍历博客列表和从数据库查到的数据列表，将从文件读取的博客内容set到Blog对象中
            for(int i=0;i<blogList.size();i++){
                blogList.get(i).setBlog_content(blogsContentList.get(i).toString());
            }
            ajaxResultDTO.state = AjaxResultDTO.STATE.SUCCESS;
            ajaxResultDTO.setData(blogList);
        }
        return ajaxResultDTO;
    }

    //一次性都查到，分页返回，减少对数据库访问次数
    @RequestMapping("/userpage/getblog/{page}")
    @ResponseBody
    public List<Object> getBlogByPage(@RequestParam("user_id")Integer user_id,HttpSession session,@PathVariable Integer page)throws Exception{
        AjaxResultDTO ajaxResultDTO = new AjaxResultDTO();
        List<Object> objectList = new ArrayList<>();
        List<Blog> blogList = blogService.getAllBlogsByUserId(user_id);  //查到了一个人所有的博客（没有内容）
        System.out.println("all:"+blogList);
        System.out.println(blogList.size());
        Integer blogCount = blogService.blogCount(user_id);
        List<List<String>>  blogContentlist = getBlogContent(user_id);
        for(int i=0;i<blogList.size();i++){
            blogList.get(i).setBlog_content(blogContentlist.get(i).toString());
        }
        objectList.add(blogCount);
        session.setAttribute("blogCount","共"+blogCount+"条博客");
        //此处博客列表有内容了
        this.allBlogList = blogList;     //全局的所有博客列表
        //刚来到此页默认显示3个
        if(page == 1){
            ajaxResultDTO.state = AjaxResultDTO.STATE.SUCCESS;
            List<Blog> tempBlogList = new ArrayList<>();
            for(int i=0;i<3;i++){
                tempBlogList.add(blogList.get(i));
            }
            ajaxResultDTO.setData(tempBlogList);
            objectList.add(ajaxResultDTO);
            return objectList;
        }else {
            ajaxResultDTO.state = AjaxResultDTO.STATE.FAILURE;
            return objectList;
        }
    }
}
