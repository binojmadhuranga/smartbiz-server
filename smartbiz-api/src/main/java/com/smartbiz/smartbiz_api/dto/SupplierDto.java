package com.smartbiz.smartbiz_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
