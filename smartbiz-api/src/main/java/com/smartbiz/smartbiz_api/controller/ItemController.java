package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.ItemDto;
import com.smartbiz.smartbiz_api.service.ItemService;
import com.smartbiz.smartbiz_api.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final JwtUtil jwtUtil;

    // ===== Helper to extract userId from JWT =====
    private Long getUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            throw new RuntimeException("Invalid token or userId not found");
        }
        return userId;
    }


    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto, HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(itemService.createItem(itemDto, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(itemService.getAllItems(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(itemService.getItemById(id, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id,
                                              @RequestBody ItemDto itemDto,
                                              HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(itemService.updateItem(id, itemDto, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        itemService.deleteItem(id, userId);
        return ResponseEntity.ok("Item deleted successfully");
    }


    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam("name") String name,
                                                     HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(itemService.searchItemsByName(name, userId));
    }

    @GetMapping("/{id}/suppliers")
    public ResponseEntity<List<Long>> getSuppliersForItem(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        ItemDto item = itemService.getItemById(id, userId);
        return ResponseEntity.ok(item.getSupplierIds().stream().toList());
    }

}
