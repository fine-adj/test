package com.userinfotemplate.demo.dto;

/*
* @Description: 前后端交互的消息实体:用于将后台操作结果/数据返回给前端
*
* */

public class AjaxResultDTO<T> {
    //操作成功/失败状态码
   public static enum STATE{          // 静态枚举内部类
        SUCCESS,
        FAILURE
    }
    public STATE state = STATE.SUCCESS;
    //返回的数据
    private T data;
    //返回的说明信息
    private String message;

    //重载的构造函数
    public AjaxResultDTO(){}

    public AjaxResultDTO(String message,T data){
        this.message = message;
        this.data = data;
    }

    //get,set方法
    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //toString方法
    @Override
    public String toString() {
        return "AjaxResultDTO{" +
                "state=" + state +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
