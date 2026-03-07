package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.ItemDto;
import com.smartbiz.smartbiz_api.dto.SupplierDto;
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
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Collections;
import com.smartbiz.smartbiz_api.exception.NotFoundException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepo itemRepository;
    private final UserRepo userRepo;
    private final SupplierRepo supplierRepo;

    @Override

    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = mapToEntity(itemDto);
        item.setUser(user);

        Set<Supplier> suppliers = new HashSet<>();
        if (itemDto.getSupplierIds() != null && !itemDto.getSupplierIds().isEmpty()) {
            suppliers.addAll(supplierRepo.findAllById(itemDto.getSupplierIds()));
            item.setSuppliers(suppliers); // inverse side
        }

        Item saved = itemRepository.save(item); // save item first to get id


        if (!suppliers.isEmpty()) {
            for (Supplier s : suppliers) {
                s.getItems().add(saved); // owning side
            }
            supplierRepo.saveAll(suppliers);
        }
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItems(Long userId) {
        return itemRepository.findByUser_Id(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findByItemIdAndUser_Id(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        Hibernate.initialize(item.getSuppliers());
        return mapToDto(item);
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        Item existingItem = itemRepository.findByItemIdAndUser_Id(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        existingItem.setName(itemDto.getName());
        existingItem.setDescription(itemDto.getDescription());
        existingItem.setQuantity(itemDto.getQuantity());
        existingItem.setUnitSellingPrice(itemDto.getUnitSellingPrice());
        existingItem.setUnitBuyingPrice(itemDto.getUnitBuyingPrice());

        if (itemDto.getSupplierIds() != null) {
            // detach from old suppliers
            Set<Supplier> oldSuppliers = Optional.ofNullable(existingItem.getSuppliers()).orElseGet(HashSet::new);
            for (Supplier old : oldSuppliers) {
                old.getItems().remove(existingItem);
            }
            supplierRepo.saveAll(oldSuppliers);

            // attach new suppliers
            Set<Supplier> newSuppliers = new HashSet<>(supplierRepo.findAllById(itemDto.getSupplierIds()));
            existingItem.setSuppliers(newSuppliers);
            Item savedItem = itemRepository.save(existingItem);
            for (Supplier s : newSuppliers) {
                s.getItems().add(savedItem);
            }
            supplierRepo.saveAll(newSuppliers);
        }
        return mapToDto(existingItem);
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        Item existingItem = itemRepository.findByItemIdAndUser_Id(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        // remove from owning side to clean join rows
        Set<Supplier> suppliers = Optional.ofNullable(existingItem.getSuppliers()).orElseGet(HashSet::new);
        for (Supplier s : suppliers) {
            s.getItems().remove(existingItem);
        }
        supplierRepo.saveAll(suppliers);
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
        Set<Supplier> suppliers = Optional.ofNullable(item.getSuppliers())
                .map(s -> { Hibernate.initialize(s); return new HashSet<>(s); })
                .orElseGet(HashSet::new);

        // Collect supplier IDs for convenience
        Set<Long> supplierIds = suppliers.stream()
                .map(Supplier::getSupplierId)
                .collect(Collectors.toSet());

        // Map suppliers to SupplierDto including item info with supplier info
        List<SupplierDto> supplierDtos = suppliers.stream()
                .map(supplier -> {
                    Set<Long> itemIds = Optional.ofNullable(supplier.getItems())
                            .orElse(Collections.emptySet())
                            .stream()
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
                })
                .collect(Collectors.toList());

        // Build and return ItemDto
        return ItemDto.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .description(item.getDescription())
                .quantity(item.getQuantity())
                .unitSellingPrice(item.getUnitSellingPrice())
                .unitBuyingPrice(item.getUnitBuyingPrice())
                .supplierIds(supplierIds)
                .suppliers(supplierDtos)
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
