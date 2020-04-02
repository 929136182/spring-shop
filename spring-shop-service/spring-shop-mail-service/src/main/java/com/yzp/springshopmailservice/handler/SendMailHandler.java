package com.yzp.springshopmailservice.handler;

import com.yzp.common.constant.RabbitConstant;
import com.yzp.entity.TProduct;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendMailHandler {

    @Autowired
    private JavaMailSender mailSender;
    @RabbitListener(queues = RabbitConstant.PRODUCT_MAIL_QUEUE)
    public void sndMail(TProduct product){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("929136182@qq.com");
        mailMessage.setTo("y929136182@163.com");
        mailMessage.setSubject("添加信息");
        mailMessage.setText(product.toString());
        mailSender.send(mailMessage);
        System.out.println("成功");
    }

}
