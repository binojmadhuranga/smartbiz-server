package com.smartbiz.smartbiz_api.service;
import com.smartbiz.smartbiz_api.dto.ItemDto;
import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto);
    List<ItemDto> getAllItems();
    ItemDto getItemById(Long id);
    ItemDto updateItem(Long id, ItemDto itemDto);
    void deleteItem(Long id);




}
