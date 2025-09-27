package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.DashboardDto;

public interface OpenAIService {
    String getBusinessSuggestions(DashboardDto dashboardDto);
}
