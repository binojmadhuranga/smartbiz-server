package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.DashboardDto;
import com.smartbiz.smartbiz_api.service.DashboardService;
import com.smartbiz.smartbiz_api.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class OpenAIController {

    private final DashboardService dashboardService;
    private final OpenAIService openAIService;

    @GetMapping("/suggestions")
    public String getSuggestions(HttpServletRequest request,
                                 @RequestParam(defaultValue = "weekly") String filter) {

        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized: userId not found in token");
        }

        DashboardDto dashboardDto = dashboardService.getDashboard(userId, filter);
        return openAIService.getBusinessSuggestions(dashboardDto);

    }
}
