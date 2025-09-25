package com.smartbiz.smartbiz_api.controller;
import com.smartbiz.smartbiz_api.dto.PlanUpdateRequestDto;
import com.smartbiz.smartbiz_api.entity.Payment;
import com.smartbiz.smartbiz_api.exception.NotFoundException;
import com.smartbiz.smartbiz_api.service.AdminPlanService;
import com.smartbiz.smartbiz_api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<?> downloadLatestUserPaymentSlip(@PathVariable Long userId) {
        try {
            Payment payment = paymentService.getLatestPaymentByUserId(userId);
            byte[] fileData = paymentService.downloadPaymentFile(payment.getId());

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + payment.getOriginalFilename() + "\"")
                    .contentType(MediaType.parseMediaType(payment.getContentType()))
                    .body(fileData);

        } catch (NotFoundException e) {
            // Return a friendly response instead of HTTP 404
            return ResponseEntity.ok().body(
                    Map.of("success", false, "message", "User has not uploaded a payment slip yet")
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("success", false, "message", "Server error")
            );
        }
    }


}
