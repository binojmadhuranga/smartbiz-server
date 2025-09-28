package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.ItemDto;
import com.smartbiz.smartbiz_api.dto.PostGenerationResponseDto;
import com.smartbiz.smartbiz_api.service.ItemService;
import com.smartbiz.smartbiz_api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostGenerationController {


    private final PostService postGenerationService;
    private final ItemService itemService;

    @GetMapping("/generate")
    public ResponseEntity<PostGenerationResponseDto> generatePost(HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized: userId not found in token");
        }

        List<ItemDto> items = itemService.getAllItems(userId);
        PostGenerationResponseDto response = postGenerationService.generateSellingPost(userId, items);
        return ResponseEntity.ok(response);
    }


}
