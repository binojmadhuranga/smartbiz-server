package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.SaleDto;
import com.smartbiz.smartbiz_api.service.SaleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {


    private final SaleService saleService;

    private Long getUserId(HttpServletRequest request) {
        // Extract userId from JWT (your existing method)
        return (Long) request.getAttribute("userId");
    }

    @PostMapping
    public ResponseEntity<SaleDto> createSale(HttpServletRequest request, @RequestBody SaleDto saleDto) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(saleService.createSale(userId, saleDto));
    }

    @GetMapping
    public ResponseEntity<List<SaleDto>> getAllSales(HttpServletRequest request,
                                                     @RequestParam(value = "search", required = false) String search) {
        Long userId = getUserId(request);

        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(saleService.searchSales(userId, search));
        }
        return ResponseEntity.ok(saleService.getAllSales(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDto> getSaleById(HttpServletRequest request, @PathVariable Long id) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(saleService.getSaleById(userId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDto> updateSale(HttpServletRequest request,
                                              @PathVariable Long id,
                                              @RequestBody SaleDto saleDto) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(saleService.updateSale(userId, id, saleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSale(HttpServletRequest request, @PathVariable Long id) {
        Long userId = getUserId(request);
        saleService.deleteSale(userId, id);
        return ResponseEntity.ok("Sale deleted successfully");
    }



}
