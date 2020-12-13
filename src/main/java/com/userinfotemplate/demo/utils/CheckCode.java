package com.userinfotemplate.demo.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckCode {
    //随机生成邮箱验证码
    public static String getCheckCode() {
        //String ZiMu = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGJKLZXCVBNM1234567890";   //验证码由大小写字母和数字构成
        String num = "1234567890";       //验证码只由数字构成
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {       //4位
            int index = random.nextInt(num.length());
            char c = num.charAt(index);
            result += c;
        }
        return result;
    }

    //校验邮箱的合法性：
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
