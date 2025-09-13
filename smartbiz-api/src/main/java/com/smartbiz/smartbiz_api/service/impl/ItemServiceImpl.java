package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.ItemDto;
import com.smartbiz.smartbiz_api.dto.SupplierDto; // added
import com.smartbiz.smartbiz_api.dto.ItemWithSuppliersDto; // new import
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
import org.springframework.transaction.annotation.Transactional; // added
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Collections; // for emptySet
import com.smartbiz.smartbiz_api.exception.NotFoundException;
import com.smartbiz.smartbiz_api.exception.ForbiddenException;
import java.util.Objects; // added


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepo itemRepository;
    private final UserRepo userRepo;
    private final SupplierRepo supplierRepo;

    @Override
    @Transactional
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

        // update owning side (Supplier.items) so join table rows are created
        if (!suppliers.isEmpty()) {
            for (Supplier s : suppliers) {
                s.getItems().add(saved);
            }
            supplierRepo.saveAll(suppliers);
        }
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
                .orElseThrow(() -> new NotFoundException("Item not found"));
        Hibernate.initialize(item.getSuppliers());
        return mapToDto(item);
    }

    @Override
    @Transactional
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
    @Transactional
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

    @Override
    public List<ItemWithSuppliersDto> getItemsWithSuppliers(Long userId) {
        return itemRepository.findByUser_Id(userId)
                .stream()
                .map(item -> {
                    Set<Supplier> supps = Optional.ofNullable(item.getSuppliers())
                            .map(s -> { Hibernate.initialize(s); return s; })
                            .orElse(Collections.emptySet());
                    List<String> supplierNames = supps.stream()
                            .map(Supplier::getName)
                            .filter(Objects::nonNull)
                            .sorted()
                            .collect(Collectors.toList());
                    return ItemWithSuppliersDto.builder()
                            .itemId(item.getItemId())
                            .name(item.getName())
                            .description(item.getDescription())
                            .quantity(item.getQuantity())
                            .unitSellingPrice(item.getUnitSellingPrice())
                            .unitBuyingPrice(item.getUnitBuyingPrice())
                            .supplierNames(supplierNames)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // ===== Helper methods =====
    private ItemDto mapToDto(Item item) {
        Set<Supplier> suppliersCopy = Optional.ofNullable(item.getSuppliers())
                .map(suppliers -> {
                    Hibernate.initialize(suppliers);
                    return new HashSet<>(suppliers);
                })
                .orElseGet(HashSet::new);

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
                .suppliers(suppliersCopy.stream()
                        .map(s -> SupplierDto.builder()
                                .supplierId(s.getSupplierId())
                                .name(s.getName())
                                .email(s.getEmail())
                                .phone(s.getPhone())
                                .address(s.getAddress())
                                .userId(s.getUserId())
                                .itemIds(Optional.ofNullable(s.getItems())
                                        .orElse(Collections.emptySet())
                                        .stream()
                                        .map(Item::getItemId)
                                        .collect(Collectors.toSet()))
                                .build())
                        .collect(Collectors.toList()))
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
