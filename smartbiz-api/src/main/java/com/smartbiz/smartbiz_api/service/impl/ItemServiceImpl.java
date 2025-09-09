package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.ItemDto;
import com.smartbiz.smartbiz_api.entity.Item;
import com.smartbiz.smartbiz_api.repo.ItemRepo;
import com.smartbiz.smartbiz_api.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepo itemRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        Item item = mapToEntity(itemDto);
        Item saved = itemRepository.save(item);
        return mapToDto(saved);
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        return mapToDto(item);
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        existingItem.setName(itemDto.getName());
        existingItem.setDescription(itemDto.getDescription());
        existingItem.setQuantity(itemDto.getQuantity());
        existingItem.setUnitSellingPrice(itemDto.getUnitSellingPrice());
        existingItem.setUnitBuyingPrice(itemDto.getUnitBuyingPrice());

        Item updated = itemRepository.save(existingItem);
        return mapToDto(updated);
    }


    @Override
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        itemRepository.deleteById(id);
    }


    // ===== Helper methods =====
    private ItemDto mapToDto(Item item) {
        return ItemDto.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .description(item.getDescription())
                .quantity(item.getQuantity())
                .unitSellingPrice(item.getUnitSellingPrice())
                .unitBuyingPrice(item.getUnitBuyingPrice())
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
