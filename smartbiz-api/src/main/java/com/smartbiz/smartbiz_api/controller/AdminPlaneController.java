package com.smartbiz.smartbiz_api.controller;
import com.smartbiz.smartbiz_api.dto.PlanUpdateRequestDto;
import com.smartbiz.smartbiz_api.entity.Payment;
import com.smartbiz.smartbiz_api.service.AdminPlanService;
import com.smartbiz.smartbiz_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @GetMapping("/users/{userId}/payments/download")
    public ResponseEntity<byte[]> downloadLatestUserPaymentSlip(
            @PathVariable Long userId
    ) throws Exception {
        Payment payment = paymentService.getLatestPaymentByUserId(userId);
        byte[] fileData = paymentService.downloadPaymentFile(payment.getId());

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + payment.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(payment.getContentType()))
                .body(fileData);
    }
}
