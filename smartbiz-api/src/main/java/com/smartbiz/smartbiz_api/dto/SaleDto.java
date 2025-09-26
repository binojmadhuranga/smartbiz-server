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
    public class SaleDto {
        private Long id;
        private String productName;
        private int quantity;
        private double price;
        private double totalAmount;
        private LocalDateTime saleDate;

    }
