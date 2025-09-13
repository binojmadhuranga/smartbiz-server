package com.smartbiz.smartbiz_api.service;
import com.smartbiz.smartbiz_api.dto.ItemDto;
import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto,Long userId);
    List<ItemDto> getAllItems(Long userId);
    ItemDto getItemById(Long itemId, Long userId);
    ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId);
    void deleteItem(Long itemId, Long userId);
    List<ItemDto> searchItemsByName(String name, Long userId);
}
