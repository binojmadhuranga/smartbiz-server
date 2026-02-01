package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendWelcomeEmail(String to, String name, String plan) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to SmartBiz 🎉");

        message.setText(
                "Hi " + name + ",\n\n" +
                        "Welcome to SmartBiz!\n\n" +
                        "Your account has been successfully created.\n" +
                        "Current Plan: " + plan + "\n\n" +
                        "Start managing your business smarter 🚀\n\n" +
                        "– SmartBiz Team"
        );

        mailSender.send(message);
    }
}