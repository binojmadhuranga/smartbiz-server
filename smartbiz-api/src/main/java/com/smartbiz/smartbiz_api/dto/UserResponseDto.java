package com.smartbiz.smartbiz_api.dto;

import com.smartbiz.smartbiz_api.entity.PlanType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private PlanType plan;


}
