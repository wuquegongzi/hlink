package com.haibao.admin.web.service;

import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

/**
 * 通知服务
 */
public interface NotifyService {
   void sendMail(SimpleMailMessage simpleMailMessage);
   void sendComplicated(MimeMessage mimeMessage);
}
