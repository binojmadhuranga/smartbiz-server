package com.smartbiz.smartbiz_api.service;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final Usertest userService = new Usertest() ;

    @Test
    void testGreetUser() {
        String result = userService.greetUser("Binoj");
        assertEquals("Hello, Binoj!", result);
    }
}
