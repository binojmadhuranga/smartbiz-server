package com.smartbiz.smartbiz_api.service;

public interface EmailService {

    void sendWelcomeEmail(String to, String name, String plan);

    void sendOtpEmail(String to, String otp);

}