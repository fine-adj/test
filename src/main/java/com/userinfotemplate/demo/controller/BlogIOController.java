package com.userinfotemplate.demo.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class BlogIOController {
    //从博客文件中读取内容:返回值是一个文件中的一个博客的数据，传进来哪个地址，就从哪个文件中读内容
    //文件IO：写，用多线程异步实现
    @Async("myTaskThreadPool")   //表示用myTaskThreadPool这个线程池，在执行此方法的时候，会单独开启线程来执行
    //@Async注解会自动帮我们把任务提交到线程池中。
//    public List<String> getBlogContentByAddr(String blog_addr){
    public CompletableFuture<List<String>> getBlogContentByAddr(String blog_addr){
        String tempString = "";
        List<String> stringList = new ArrayList<>();
        try{
            FileReader fileReader = new FileReader(blog_addr);   //输入流
            BufferedReader bufferedReader = new BufferedReader(fileReader);  //以"行"为单位从文件中读取数据，比按照字节读取更高效
            int line = 1;
            while ((tempString = bufferedReader.readLine()) != null){
                stringList.add(tempString) ;
                line ++;
            }
            bufferedReader.close();
        }catch (IOException e){
            e.getStackTrace();
        }
//        return stringList;
        return CompletableFuture.completedFuture(stringList);
    }
    @Async("myTaskThreadPool")
    public CompletableFuture<String> setBlogContent(Integer user_id, String blog_content,String blog_type, String blog_title){
        //存入文件：每个用户有一个自己的文件夹，博客文件名称为博客id_分类标签，例如：1_数据库
        //windows上与linux上的路径分隔符不同，要用：File.separator做路径转换符
        String filepath = "C:"+ File.separator+"Users"+File.separator+"Administrator"+File.separator+"Desktop"+File.separator+"userInfoTemplate_free"+
                File.separator+"userInfoTemplate"+File.separator+"blogfile"+File.separator+user_id+"-"+blog_type+"-"+blog_title+".txt";
        FileOutputStream fileOutputStream = null;
        try{
            fileOutputStream  = new FileOutputStream(filepath);       //创建输出流
            //buffer中三个概念：capacity缓冲区的容量；position：类似于读/写指针，表示当前读(写)到什么位置。
            // limit在写模式下表示最多能写入多少数据，此时和capacity相同。在读模式下表示最多能读多少数据，此时和缓存中的实际数据大小相同。
            byte[] blog_contentBytes = blog_content.getBytes();       //按照数据大小一次性分配缓冲区进行一次性读写
//            System.out.println("blog_contentBytes.length="+blog_contentBytes.length);
            int capacity = (blog_contentBytes.length/1024+1)*1024;
            //
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity);             //创建缓冲区，容量capacity为2*1024字节
            FileChannel fileChannel = fileOutputStream.getChannel();       //文件与channel唯一关联
            //flip()作用：当准备向缓冲区写数据时，先调用该方法表示将指针移到开头，从头开始向缓冲区中写数据
            //当从缓冲区中读数据时，调用该方法表示将指针移到缓冲区开头，从头开始读
            //flip()把limit设为当前position，把position设为0，一般在从Buffer读出数据前调用。
            byteBuffer.clear();          //把position设为0，把limit设为capacity，一般在把数据写入Buffer前调用。
            byteBuffer.put(blog_contentBytes);          //把数据写入buffer，String类型，通过getBytes()转成字节
            byteBuffer.flip();           //将buffer置成可读，从头开始读
            fileChannel.write(byteBuffer);  //从buffer把数据写入channel，就相当于写入file

        }catch (IOException e){
            e.getStackTrace();
        }finally {
            if(fileOutputStream != null){
                try{
                    fileOutputStream.close();
                }catch (IOException e){
                    e.getStackTrace();
                }
            }
        }
        return CompletableFuture.completedFuture(filepath);
    }
}
