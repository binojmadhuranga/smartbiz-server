package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.entity.PlanType;
import com.smartbiz.smartbiz_api.entity.User;

public interface UserPlaneService {

    User updateUserPlan(Long userId, PlanType planType);

    // Fetch a user by id
    User getUserById(Long userId);

}
