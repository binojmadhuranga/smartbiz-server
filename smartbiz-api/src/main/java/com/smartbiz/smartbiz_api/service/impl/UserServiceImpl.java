package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.entity.PlanType;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public User updateUserPlan(Long userId, PlanType planType) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPlan(planType);
        return userRepo.save(user);
    }




}
