package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.PlanUpdateRequestDto;
import com.smartbiz.smartbiz_api.dto.UserResponseDto;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.AdminPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminPlanServiceImpl implements AdminPlanService {

    private final UserRepo userRepository;

    @Override
    public UserResponseDto updateUserPlan(Long userId, PlanUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPlan(request.getPlan());
        User updatedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole())
                .plan(updatedUser.getPlan())
                .build();
    }




}
