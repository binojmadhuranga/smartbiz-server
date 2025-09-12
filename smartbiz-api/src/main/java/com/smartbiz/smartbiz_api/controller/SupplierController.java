package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.SupplierDto;
import com.smartbiz.smartbiz_api.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierDto> createSupplier(@RequestBody SupplierDto supplierDto) {
        if (supplierDto.getUserId() == null) {
            return ResponseEntity.badRequest().build();
        }
        SupplierDto created = supplierService.createSupplier(supplierDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplier(@PathVariable Long id) {
        SupplierDto supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        List<SupplierDto> list = supplierService.getAllSuppliers();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupplierDto>> getSuppliersByUser(@PathVariable Long userId) {
        List<SupplierDto> list = supplierService.getSuppliersByUserId(userId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable Long id, @RequestBody SupplierDto supplierDto) {
        if (supplierDto.getUserId() == null) {
            return ResponseEntity.badRequest().build();
        }
        SupplierDto updated = supplierService.updateSupplier(id, supplierDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<Long>> getItemsForSupplier(@PathVariable Long id) {
        SupplierDto supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier.getItemIds().stream().toList());
    }

}
