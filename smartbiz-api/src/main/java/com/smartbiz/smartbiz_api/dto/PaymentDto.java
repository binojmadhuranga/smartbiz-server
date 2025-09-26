package com.smartbiz.smartbiz_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Long id;
    private Long userId;
    private String originalFilename;
    private String storedFilename;
    private String storagePath;
    private String contentType;
    private long sizeBytes;
    private LocalDateTime uploadedAt;
}

