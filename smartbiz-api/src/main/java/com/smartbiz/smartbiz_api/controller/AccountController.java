package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.smartbiz.smartbiz_api.dto.PlanUpdateRequestDto;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    @PutMapping("/{userId}/plan")
    public ResponseEntity<User> updatePlan(
            @PathVariable Long userId,
            @RequestBody PlanUpdateRequestDto request) {
        User updatedUser = userService.updateUserPlan(userId, request.getPlan());
        return ResponseEntity.ok(updatedUser);
    }

}
