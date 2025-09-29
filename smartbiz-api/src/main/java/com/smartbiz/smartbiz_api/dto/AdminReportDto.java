package com.smartbiz.smartbiz_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminReportDto {
    private long totalUsers;
    private long normalUsers;
    private long proUsers;
}
