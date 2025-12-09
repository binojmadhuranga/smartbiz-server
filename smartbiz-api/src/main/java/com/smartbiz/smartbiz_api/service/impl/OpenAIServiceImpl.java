package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.config.OpenAIConfig;
import com.smartbiz.smartbiz_api.dto.DashboardDto;
import com.smartbiz.smartbiz_api.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {

    private final OpenAIConfig openAIConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getBusinessSuggestions(DashboardDto dashboardDto) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAIConfig.getApiKey());


        String businessData = String.format(
                "Business Weekly Summary:\n" +
                        "- Customers: %d\n" +
                        "- Suppliers: %d\n" +
                        "- Products: %d\n" +
                        "- Employees: %d\n" +
                        "- Total Sales: %.2f\n\n" +
                        "Please analyze this data and provide actionable business suggestions.",
                dashboardDto.getCustomerCount(),
                dashboardDto.getSupplierCount(),
                dashboardDto.getProductCount(),
                dashboardDto.getEmployeeCount(),
                dashboardDto.getTotalSales()
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini:ft-binoj-smartbiz");
        body.put("messages", List.of(
                Map.of("role", "system", "content", "You are an experienced business consultant AI."),
                Map.of("role", "user", "content", businessData)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }

}
