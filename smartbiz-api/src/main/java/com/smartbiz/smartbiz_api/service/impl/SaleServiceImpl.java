package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.SaleDto;
import com.smartbiz.smartbiz_api.entity.Sale;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.repo.SaleRepo;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepo saleRepository;
    private final UserRepo userRepository;

    @Override
    public SaleDto createSale(Long userId, SaleDto saleDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Sale sale = Sale.builder()
                .productName(saleDto.getProductName())
                .quantity(saleDto.getQuantity())
                .price(saleDto.getPrice())
                .totalAmount(saleDto.getQuantity() * saleDto.getPrice())
                .saleDate(LocalDateTime.now())
                .user(user)
                .build();

        return mapToDto(saleRepository.save(sale));
    }

    @Override
    public List<SaleDto> getAllSales(Long userId) {
        return saleRepository.findByUser_Id(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SaleDto getSaleById(Long userId, Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        if (!sale.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }
        return mapToDto(sale);
    }

    @Override
    public SaleDto updateSale(Long userId, Long saleId, SaleDto saleDto) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        if (!sale.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        sale.setProductName(saleDto.getProductName());
        sale.setQuantity(saleDto.getQuantity());
        sale.setPrice(saleDto.getPrice());
        sale.setTotalAmount(saleDto.getQuantity() * saleDto.getPrice());

        return mapToDto(saleRepository.save(sale));
    }

    @Override
    public void deleteSale(Long userId, Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        if (!sale.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        saleRepository.delete(sale);
    }

    private SaleDto mapToDto(Sale sale) {
        return SaleDto.builder()
                .id(sale.getId())
                .productName(sale.getProductName())
                .quantity(sale.getQuantity())
                .price(sale.getPrice())
                .totalAmount(sale.getTotalAmount())
                .saleDate(sale.getSaleDate())
                .build();
    }

}
