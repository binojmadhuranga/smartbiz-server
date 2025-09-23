package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.PlanUpdateRequestDto;
import com.smartbiz.smartbiz_api.dto.UserResponseDto;

import java.util.List;

public interface AdminPlanService {

    UserResponseDto updateUserPlan(Long userId, PlanUpdateRequestDto request);

    List<UserResponseDto> getAllUsers();

}
