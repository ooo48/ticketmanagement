package org.example.ticketmanagement.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender mailSender;

    private String fromEmail = "2966801095@qq.com";

    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("演出购票系统 - 验证码");
        message.setText("您的验证码是: " + code + "，有效期为5分钟。请勿泄露给他人。");
        mailSender.send(message);
    }

    public void sendTicketSuccessEmail(String toEmail, String showName, String sessionTime,
                                       String seats, String orderNo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("购票成功通知");
        message.setText("恭喜您购票成功！\n" +
                "演出名称: " + showName + "\n" +
                "场次时间: " + sessionTime + "\n" +
                "座位信息: " + seats + "\n" +
                "订单号: " + orderNo + "\n" +
                "请准时到场观看演出。");
        mailSender.send(message);
    }
}
