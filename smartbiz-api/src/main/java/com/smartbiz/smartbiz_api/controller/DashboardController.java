package com.smartbiz.smartbiz_api.controller;


import com.smartbiz.smartbiz_api.dto.DashboardDto;
import com.smartbiz.smartbiz_api.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    @GetMapping
    public ResponseEntity<DashboardDto> getDashboard(
            HttpServletRequest request,
            @RequestParam(defaultValue = "daily") String filter
    ) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(dashboardService.getDashboard(userId, filter));
    }

}
