package com.smartbiz.smartbiz_api.dto;

import com.smartbiz.smartbiz_api.entity.PlanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String password;
    private String name;
    private String email;
    private String role;
    private PlanType plan;


    // Added explicit constructor matching usages that don't include password
    public UserDto(Long id, String name, String email, String role,PlanType plan) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.plan = plan;

    }

}
