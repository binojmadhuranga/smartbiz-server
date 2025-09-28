package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.config.OpenAIConfig;
import com.smartbiz.smartbiz_api.dto.ItemDto;
import com.smartbiz.smartbiz_api.dto.PostGenerationResponseDto;
import com.smartbiz.smartbiz_api.service.ItemService;
import com.smartbiz.smartbiz_api.service.PostGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostGenerationServiceImpl implements PostGenerationService {

    private final OpenAIConfig openAIConfig;
    private final ItemService itemService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PostGenerationResponseDto generateSellingPost(Long userId, List items) {
        return null;
    }
}
