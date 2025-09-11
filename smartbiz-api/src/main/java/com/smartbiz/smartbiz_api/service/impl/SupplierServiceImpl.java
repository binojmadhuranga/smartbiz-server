package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.SupplierDto;
import com.smartbiz.smartbiz_api.entity.Supplier;
import com.smartbiz.smartbiz_api.repo.SupplierRepo;
import com.smartbiz.smartbiz_api.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;

    private SupplierDto mapToDto(Supplier supplier) {
        return SupplierDto.builder()
                .supplierId(supplier.getSupplierId())
                .name(supplier.getName())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .userId(supplier.getUserId())
                .build();
    }

    private Supplier mapToEntity(SupplierDto dto) {
        return Supplier.builder()
                .supplierId(dto.getSupplierId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .userId(dto.getUserId())
                .build();
    }

    @Override
    public SupplierDto createSupplier(SupplierDto supplierDto) {
        if (supplierDto.getUserId() == null) {
            throw new IllegalArgumentException("UserId must be provided");
        }
        Supplier supplier = mapToEntity(supplierDto);
        Supplier savedSupplier = supplierRepo.save(supplier);
        return mapToDto(savedSupplier);
    }

    @Override
    public SupplierDto getSupplierById(Long id) {
        Supplier supplier = supplierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return mapToDto(supplier);
    }

    @Override
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierDto> getSuppliersByUserId(Long userId) {
        return supplierRepo.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDto updateSupplier(Long id, SupplierDto supplierDto) {
        Supplier existingSupplier = supplierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        existingSupplier.setName(supplierDto.getName());
        existingSupplier.setEmail(supplierDto.getEmail());
        existingSupplier.setPhone(supplierDto.getPhone());
        existingSupplier.setAddress(supplierDto.getAddress());

        // Update userId if provided
        if (supplierDto.getUserId() != null) {
            existingSupplier.setUserId(supplierDto.getUserId());
        }

        Supplier updatedSupplier = supplierRepo.save(existingSupplier);
        return mapToDto(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        supplierRepo.deleteById(id);
    }

}
