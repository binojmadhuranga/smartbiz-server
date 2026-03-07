package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.entity.PlanType;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.exception.NotFoundException;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.UserPlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPlaneServiceImpl implements UserPlaneService {

    private final UserRepo userRepo;

    public User updateUserPlan(Long userId, PlanType planType) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setPlan(planType);
        return userRepo.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

}
