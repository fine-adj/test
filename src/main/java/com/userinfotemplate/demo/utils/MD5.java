package com.userinfotemplate.demo.utils;

import org.springframework.util.DigestUtils;

public class MD5 {
    //该方法相同的原字符串，加密后结果相同，例如：“123456”->"e10adc3949ba59abbe56e057f20f883e"
    public static String getMD5(String src_str){
        return DigestUtils.md5DigestAsHex(src_str.getBytes());
    }
}
