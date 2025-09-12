package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.SupplierDto;
import com.smartbiz.smartbiz_api.entity.Item;
import com.smartbiz.smartbiz_api.entity.Supplier;
import com.smartbiz.smartbiz_api.repo.ItemRepo;
import com.smartbiz.smartbiz_api.repo.SupplierRepo;
import com.smartbiz.smartbiz_api.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;
    private final ItemRepo itemRepo;


    private SupplierDto mapToDto(Supplier supplier) {
        // defensive copy to avoid concurrent modification while Hibernate finalizes loading
        Set<Item> itemsCopy = new HashSet<>(supplier.getItems());
        Set<Long> itemIds = itemsCopy.stream()
                .map(Item::getItemId)
                .collect(Collectors.toSet());

        return SupplierDto.builder()
                .supplierId(supplier.getSupplierId())
                .name(supplier.getName())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .userId(supplier.getUserId())
                .itemIds(itemIds)
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
    @org.springframework.transaction.annotation.Transactional
    public SupplierDto createSupplier(SupplierDto supplierDto) {
        if (supplierDto.getUserId() == null) {
            throw new IllegalArgumentException("UserId must be provided");
        }

        Supplier supplier = mapToEntity(supplierDto);

        if (supplierDto.getItemIds() != null && !supplierDto.getItemIds().isEmpty()) {
            Set<Item> items = new HashSet<>(itemRepo.findAllById(supplierDto.getItemIds()));
            supplier.setItems(items); // owning side
        }

        Supplier savedSupplier = supplierRepo.save(supplier);
        return mapToDto(savedSupplier);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public SupplierDto getSupplierById(Long id) {
        Supplier supplier = supplierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return mapToDto(supplier);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<SupplierDto> getSuppliersByUserId(Long userId) {
        return supplierRepo.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public SupplierDto updateSupplier(Long id, SupplierDto supplierDto) {
        Supplier existingSupplier = supplierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        existingSupplier.setName(supplierDto.getName());
        existingSupplier.setEmail(supplierDto.getEmail());
        existingSupplier.setPhone(supplierDto.getPhone());
        existingSupplier.setAddress(supplierDto.getAddress());

        if (supplierDto.getUserId() != null) {
            existingSupplier.setUserId(supplierDto.getUserId());
        }

        if (supplierDto.getItemIds() != null) {
            Set<Item> items = new HashSet<>(itemRepo.findAllById(supplierDto.getItemIds()));
            existingSupplier.setItems(items); // owning side only
        }

        Supplier updatedSupplier = supplierRepo.save(existingSupplier);
        return mapToDto(updatedSupplier);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteSupplier(Long id) {
        supplierRepo.deleteById(id);
    }
}
