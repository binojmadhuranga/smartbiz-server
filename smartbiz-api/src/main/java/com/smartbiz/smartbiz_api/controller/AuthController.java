package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.*;
import com.smartbiz.smartbiz_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public AuthResponseDto login(@RequestBody AuthDto request) {
        return authService.login(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordDto dto) {
        return ResponseEntity.ok(authService.forgotPassword(dto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordDto dto) {
        return ResponseEntity.ok(authService.resetPassword(dto));
    }


}
