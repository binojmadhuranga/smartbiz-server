package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.SaleDto;

import java.util.List;

public interface SaleService {

    SaleDto createSale(Long userId, SaleDto saleDto);

    List<SaleDto> getAllSales(Long userId);

    SaleDto getSaleById(Long userId, Long saleId);

    SaleDto updateSale(Long userId, Long saleId, SaleDto saleDto);

    void deleteSale(Long userId, Long saleId);

    List<SaleDto> searchSales(Long userId, String keyword);

}
