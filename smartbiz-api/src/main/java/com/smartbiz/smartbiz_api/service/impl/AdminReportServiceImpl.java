package com.smartbiz.smartbiz_api.service.impl;


import com.smartbiz.smartbiz_api.dto.AdminReportDto;
import com.smartbiz.smartbiz_api.entity.PlanType;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {

    private final UserRepo userRepository;

    @Override
    public AdminReportDto generateUserReport() {
        long total = userRepository.count();
        long normal = userRepository.countByPlan(PlanType.NORMAL);
        long pro = userRepository.countByPlan(PlanType.PRO);

        return new AdminReportDto(total, normal, pro);
    }
}
