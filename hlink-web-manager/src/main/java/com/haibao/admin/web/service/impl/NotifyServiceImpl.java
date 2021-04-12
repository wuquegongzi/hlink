package com.haibao.admin.web.service.impl;

import com.haibao.admin.web.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/*
 * @Author ml.c
 * @Description
 * @Date 21:33 2020-04-22
 **/
@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Override
    public void sendMail(SimpleMailMessage simpleMailMessage) {
//        SimpleMailMessage message = new SimpleMailMessage();
//           message.setSubject("email测试");
//           message.setText("邮件测试内容");
//           message.setTo("fanqixxxx@vip.qq.com");
//           message.setFrom("fanqixxxx@163.com");
           javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendComplicated(MimeMessage mimeMessage) {
        //创建一个复杂的消息邮件
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        //用MimeMessageHelper来包装MimeMessage
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
//        mimeMessageHelper.setSubject("email测试");
//        mimeMessageHelper.setText("邮件测试内容");
//        mimeMessageHelper.setTo("fanqixxxx@vip.qq.com");
//        mimeMessageHelper.setFrom("fanqixxxx@163.com");
//        mimeMessageHelper.addAttachment("meinv.jpg",new File("D:\\meinv.jpg"));
        javaMailSender.send(mimeMessage);
    }
}
