package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.DashboardDto;

public interface DashboardService {

    DashboardDto getDashboard(Long userId, String filter);

}