package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.AuthDto;
import com.smartbiz.smartbiz_api.dto.AuthResponse;
import com.smartbiz.smartbiz_api.dto.UserDto;
import com.smartbiz.smartbiz_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto) {
        return authService.register(userDto);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthDto request) {
        String token = authService.login(request);
        return new AuthResponse(token);
    }
}
