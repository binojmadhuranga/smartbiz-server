package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.config.OpenAIConfig;
import com.smartbiz.smartbiz_api.dto.ItemDto;
import com.smartbiz.smartbiz_api.dto.PostGenerationResponseDto;
import com.smartbiz.smartbiz_api.service.ItemService;
import com.smartbiz.smartbiz_api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


    private final OpenAIConfig openAIConfig;
    private final ItemService itemService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PostGenerationResponseDto generateSellingPost(Long userId, List<ItemDto> items) {
        String url = "https://api.openai.com/v1/chat/completions";

        // Convert items into a descriptive text
        StringBuilder itemDetails = new StringBuilder("Here are the items for sale:\n");
        for (ItemDto item : items) {
            itemDetails.append("- ").append(item.getName())
                    .append(" (").append(item.getDescription() != null ? item.getDescription() : "No description")
                    .append("), Quantity: ").append(item.getQuantity())
                    .append(", Price: ").append(item.getUnitSellingPrice()).append("\n");
        }

        String prompt = "Create a professional and engaging Facebook/Instagram style post to sell these items. "
                + "Make it attractive for potential customers. Include promotional tone:\n\n" + itemDetails;

        // Prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAIConfig.getApiKey());

        Map<String, Object> request = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a marketing expert who writes engaging product selling posts."),
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 500
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        // Extract response text
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = (String) message.get("content");

        return PostGenerationResponseDto.builder()
                .content(content)
                .build();
    }


}
