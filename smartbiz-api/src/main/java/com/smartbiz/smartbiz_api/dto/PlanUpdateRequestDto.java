package com.smartbiz.smartbiz_api.dto;

import com.smartbiz.smartbiz_api.entity.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanUpdateRequestDto {
    private PlanType plan;
}
