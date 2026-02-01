package com.smartbiz.smartbiz_api.event;

import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserRegisteredListener {


    private final EmailService emailService;

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        User user = event.getUser();
        emailService.sendWelcomeEmail(
                user.getEmail(),
                user.getName(),
                user.getPlan().name()
        );
    }
}
