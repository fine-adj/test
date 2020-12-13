package com.userinfotemplate.demo.utils;

import com.userinfotemplate.demo.entity.UserInfo;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailUtil {
    private static String host = "smtp.163.com";//假设你使用163邮箱
    private static String email = "xxx@163.com";//你的邮箱地址（给别人发邮件的邮箱地址）
    private static String username = "xxx@163.com";//用户名称
    private static String password = "xxx";//邮箱账户密码（给别人发邮件的邮箱的授权码）
    //在我们进行Java发送邮箱的时候，网易设置了授权码登录，而不是密码登录，防止我们的密码被盗用。
    // 邮箱客户端授权密码适用于任何通过IMAP/POP3协议登录邮箱的客户端。
    // 所以我们不应该设置的是我们邮箱登录的密码，而是授权码。

    public static boolean send(UserInfo user, String checkcode) throws Exception{
        Properties prop = new Properties();
        prop.setProperty("mail.host", host);
        prop.setProperty("mail.transport.protocol", "smtp");
        Session session = Session.getInstance(prop);
        Message message = createmessage(session, user,checkcode);
        Transport ts = session.getTransport();
        ts.connect(username, password);
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
        return true;
    }

    private static Message createmessage(Session session, UserInfo userInfo, String checkcode) throws AddressException, MessagingException{
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        System.out.println("email:"+ userInfo.getEmail());
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(userInfo.getEmail()));//得到用户邮箱
        message.setSubject("\"😝故事和酒~\"网站新用户注册");		//更改你想要发送的内容
//        String content = "亲爱的\"😃故事和酒~~~\"用户\""+userBasicInfo.getUsername()+"\"，欢迎注册，请在1分钟内正确填写邮箱验证码："+checkcode;
        String content = "亲爱的用户\""+ userInfo.getUsername()+"\",欢迎注册\"最懂你的❤之😝故事和酒~\"网站，请在1分钟内正确填写邮箱验证码："+checkcode;
        message.setContent(content, "text/html;charset=UTF-8");
        message.saveChanges();
        return message;
    }


}
