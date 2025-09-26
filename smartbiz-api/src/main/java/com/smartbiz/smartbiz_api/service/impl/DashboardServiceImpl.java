package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.DashboardDto;
import com.smartbiz.smartbiz_api.repo.*;
import com.smartbiz.smartbiz_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepo customerRepository;
    private final SupplierRepo supplierRepository;
    private final ItemRepo productRepository;
    private final EmployeeRepo employeeRepository;
    private final SaleRepo saleRepository;

    @Override
    public DashboardDto getDashboard(Long userId, String filter) {
        LocalDateTime start;
        LocalDateTime end = LocalDateTime.now();

        switch (filter.toLowerCase()) {
            case "daily" -> start = LocalDate.now().atStartOfDay();
            case "weekly" -> start = LocalDate.now().minusDays(7).atStartOfDay();
            case "monthly" -> start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
            case "yearly" -> start = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
            default -> start = LocalDate.now().atStartOfDay(); // fallback daily
        }

        // ✅ User-specific counts
        long customerCount = customerRepository.countByUser_Id(userId);
        long supplierCount = supplierRepository.countByUserId(userId);
        long productCount = productRepository.countByUser_Id(userId);
        long employeeCount = employeeRepository.countByUser_Id(userId);

        // ✅ User-specific sales sum
        Double totalSales = saleRepository.getTotalSalesBetweenByUser(userId, start, end);
        if (totalSales == null) totalSales = 0.0;

        return DashboardDto.builder()
                .customerCount(customerCount)
                .supplierCount(supplierCount)
                .productCount(productCount)
                .employeeCount(employeeCount)
                .totalSales(totalSales)
                .build();
    }
}
