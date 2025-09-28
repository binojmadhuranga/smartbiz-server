package com.smartbiz.smartbiz_api.service;

import java.util.List;
import com.smartbiz.smartbiz_api.dto.ItemDto;
import com.smartbiz.smartbiz_api.dto.PostGenerationResponseDto;

public interface PostGenerationService {

    PostGenerationResponseDto generateSellingPost(Long userId, List items);

}
