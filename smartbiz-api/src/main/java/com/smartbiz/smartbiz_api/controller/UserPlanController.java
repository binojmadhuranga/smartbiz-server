package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.exception.ForbiddenException;
import com.smartbiz.smartbiz_api.exception.UnauthorizedException;
import com.smartbiz.smartbiz_api.service.UserPlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.smartbiz.smartbiz_api.dto.PlanUpdateRequestDto;

import org.springframework.web.bind.annotation.*;
import com.smartbiz.smartbiz_api.dto.PaymentDto;
import com.smartbiz.smartbiz_api.service.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import com.smartbiz.smartbiz_api.dto.AccountInfoDto;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class UserPlanController {

    private final UserPlaneService userPlaneService;
    private final PaymentService paymentService;

    private Long getUserId(HttpServletRequest request) {
        Object val = request.getAttribute("userId");
        if (val == null) throw new UnauthorizedException("Unauthorized");
        if (val instanceof Long) return (Long) val;
        try { return Long.valueOf(val.toString()); } catch (NumberFormatException e) { throw new UnauthorizedException("Unauthorized"); }
    }

    private String getRole(HttpServletRequest request) {
        Object r = request.getAttribute("role");
        return r != null ? r.toString() : null;
    }

    @GetMapping("/me")
    public ResponseEntity<AccountInfoDto> getUserPlan(
            HttpServletRequest request,
            @RequestParam(value = "userId", required = false) Long userIdParam
    ) {
        Long callerId = getUserId(request);
        Long targetUserId = userIdParam != null ? userIdParam : callerId;

        if (userIdParam != null && !userIdParam.equals(callerId)) {
            String role = getRole(request);
            if (role == null || !role.equalsIgnoreCase("ADMIN")) {
                throw new ForbiddenException("You are not allowed to view this user's account");
            }
        }

        User user = userPlaneService.getUserById(targetUserId);
        AccountInfoDto dto = AccountInfoDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .plan(user.getPlan())
                .build();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/plan")
    public ResponseEntity<Long> updatePlan(
            HttpServletRequest request,
            @RequestBody PlanUpdateRequestDto body) {
        Long userId = getUserId(request);
        User updatedUser = userPlaneService.updateUserPlan(userId, body.getPlan());
        return ResponseEntity.ok(updatedUser.getId());
    }

    // Admin: update any user's plan by path userId
    @PutMapping("/users/{userId}/plan")
    public ResponseEntity<Long> adminUpdateUserPlan(
            HttpServletRequest request,
            @PathVariable("userId") Long targetUserId,
            @RequestBody PlanUpdateRequestDto body
    ) {
        String role = getRole(request);
        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            throw new ForbiddenException("Only admins can update other users' plans");
        }
        User updated = userPlaneService.updateUserPlan(targetUserId, body.getPlan());
        return ResponseEntity.ok(updated.getId());
    }

    @PostMapping(path = "/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PaymentDto> uploadPayment(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file) {
        Long userId = getUserId(request);
        PaymentDto dto = paymentService.uploadPayment(userId, file);
        return ResponseEntity.ok(dto);
    }

}
