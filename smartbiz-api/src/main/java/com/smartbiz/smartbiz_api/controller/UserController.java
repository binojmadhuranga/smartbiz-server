package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.UserDto;
import com.smartbiz.smartbiz_api.service.UserService;
import com.smartbiz.smartbiz_api.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile(HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }


    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMyProfile(
            HttpServletRequest request,
            @RequestBody UserDto userDto) {

        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }


    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("JWT Token is missing");
    }






}
