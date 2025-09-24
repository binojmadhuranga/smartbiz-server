package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.PaymentDto;
import com.smartbiz.smartbiz_api.entity.Payment;
import org.springframework.web.multipart.MultipartFile;

public interface PaymentService {
    PaymentDto uploadPayment(Long userId, MultipartFile file);

    Payment getPaymentById(Long paymentId);

    byte[] downloadPaymentFile(Long paymentId) throws Exception;

    Payment getLatestPaymentByUserId(Long userId);


}

