package com.smartbiz.smartbiz_api.event;

import com.smartbiz.smartbiz_api.entity.User;
import lombok.Getter;

@Getter
public class UserRegisteredEvent {
    private final User user;

    public UserRegisteredEvent(User user) {
        this.user = user;
    }
}
