package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.PlanUpdateRequestDto;
import com.smartbiz.smartbiz_api.dto.UserResponseDto;



public interface AdminPlanService {

    UserResponseDto updateUserPlan(Long userId, PlanUpdateRequestDto request);



}
