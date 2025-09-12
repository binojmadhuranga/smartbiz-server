package com.smartbiz.smartbiz_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long itemId;
    private String name;
    private String description;
    private Integer quantity;
    private Double unitSellingPrice;
    private Double unitBuyingPrice;

    private Set<Long> supplierIds;

}