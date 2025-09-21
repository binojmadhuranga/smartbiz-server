package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.service.UserPlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.smartbiz.smartbiz_api.dto.PlanUpdateRequestDto;

import org.springframework.web.bind.annotation.*;
import com.smartbiz.smartbiz_api.dto.PaymentDto;
import com.smartbiz.smartbiz_api.service.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserPlaneService userPlaneService;
    private final PaymentService paymentService;

    @PutMapping("/{userId}/plan")
    public ResponseEntity<User> updatePlan(
            @PathVariable Long userId,
            @RequestBody PlanUpdateRequestDto request) {
        User updatedUser = userPlaneService.updateUserPlan(userId, request.getPlan());
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping(path = "/{userId}/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PaymentDto> uploadPayment(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        PaymentDto dto = paymentService.uploadPayment(userId, file);
        return ResponseEntity.ok(dto);
    }

}
