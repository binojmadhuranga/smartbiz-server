package com.smartbiz.smartbiz_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SupplierItemDto {
    private Long itemId;
    private String itemName;
    private Long supplierId;
    private String supplierName;





}
