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
public class SupplierDto {

    private Long supplierId;
    private String name;
    private String email;
    private String phone;
    private String address;

    private Long userId;
    private Set<Long> itemIds;
    private Set<String> itemNames;
}
