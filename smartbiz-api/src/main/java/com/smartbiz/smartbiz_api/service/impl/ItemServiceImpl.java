package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.ItemDto;
import com.smartbiz.smartbiz_api.entity.Item;
import com.smartbiz.smartbiz_api.entity.Supplier;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.repo.ItemRepo;
import com.smartbiz.smartbiz_api.repo.SupplierRepo;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepo itemRepository;
    private final UserRepo userRepo;
    private final SupplierRepo supplierRepo;


    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = mapToEntity(itemDto);
        item.setUser(user);

        if (itemDto.getSupplierIds() != null && !itemDto.getSupplierIds().isEmpty()) {
            Set<Supplier> suppliers = new HashSet<>(supplierRepo.findAllById(itemDto.getSupplierIds()));
            item.setSuppliers(suppliers);
        }

        Item saved = itemRepository.save(item);
        return mapToDto(saved);
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        return itemRepository.findByUser_Id(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findByItemIdAndUser_Id(itemId, userId)
                .orElseThrow(() -> new RuntimeException("Item not found or you don't own it"));
        return mapToDto(item);
    }


    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        Item existingItem = itemRepository.findByItemIdAndUser_Id(itemId, userId)
                .orElseThrow(() -> new RuntimeException("Item not found or you don't own it"));

        existingItem.setName(itemDto.getName());
        existingItem.setDescription(itemDto.getDescription());
        existingItem.setQuantity(itemDto.getQuantity());
        existingItem.setUnitSellingPrice(itemDto.getUnitSellingPrice());
        existingItem.setUnitBuyingPrice(itemDto.getUnitBuyingPrice());

        if (itemDto.getSupplierIds() != null) {
            Set<Supplier> suppliers = new HashSet<>(supplierRepo.findAllById(itemDto.getSupplierIds()));
            existingItem.setSuppliers(suppliers);
        }

        Item updated = itemRepository.save(existingItem);
        return mapToDto(updated);
    }



    @Override
    public void deleteItem(Long itemId, Long userId) {
        Item existingItem = itemRepository.findByItemIdAndUser_Id(itemId, userId)
                .orElseThrow(() -> new RuntimeException("Item not found or you don't own it"));
        itemRepository.delete(existingItem);
    }

    @Override
    public List<ItemDto> searchItemsByName(String name, Long userId) {
        return itemRepository.findByNameContainingIgnoreCaseAndUser_Id(name, userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    // ===== Helper methods =====
    private ItemDto mapToDto(Item item) {
        // Ensure suppliers are fully loaded
        Hibernate.initialize(item.getSuppliers());
        Set<Supplier> suppliersCopy = new HashSet<>(item.getSuppliers());
        Set<Long> supplierIds = suppliersCopy.stream()
                .map(Supplier::getSupplierId)
                .collect(Collectors.toSet());

        return ItemDto.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .description(item.getDescription())
                .quantity(item.getQuantity())
                .unitSellingPrice(item.getUnitSellingPrice())
                .unitBuyingPrice(item.getUnitBuyingPrice())
                .supplierIds(supplierIds)
                .build();
    }


    private Item mapToEntity(ItemDto dto) {
        return Item.builder()
                .itemId(dto.getItemId())
                .name(dto.getName())
                .description(dto.getDescription())
                .quantity(dto.getQuantity())
                .unitSellingPrice(dto.getUnitSellingPrice())
                .unitBuyingPrice(dto.getUnitBuyingPrice())
                .build();
    }

}
