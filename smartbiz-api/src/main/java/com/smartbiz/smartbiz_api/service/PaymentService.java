package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.PaymentDto;
import org.springframework.web.multipart.MultipartFile;

public interface PaymentService {
    PaymentDto uploadPayment(Long userId, MultipartFile file);
}

