package com.smartbiz.smartbiz_api.controller;
import com.smartbiz.smartbiz_api.dto.PlanUpdateRequestDto;
import com.smartbiz.smartbiz_api.service.AdminPlanService;
import com.smartbiz.smartbiz_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminPlaneController {

    private final AdminPlanService adminService;
    private final PaymentService paymentService;


    // Update user plan (NORMAL <-> PRO) and return only user ID
    @PutMapping("/users/{userId}/plan")
    public ResponseEntity<Long> updateUserPlan(
            @PathVariable Long userId,
            @RequestBody PlanUpdateRequestDto request) {
        return ResponseEntity.ok(adminService.updateUserPlan(userId, request).getId());
    }

}
