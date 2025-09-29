package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.AdminReportDto;
import com.smartbiz.smartbiz_api.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService reportService;

    @GetMapping("/users")
    public ResponseEntity<AdminReportDto> getUserReport() {
        return ResponseEntity.ok(reportService.generateUserReport());
    }

}
