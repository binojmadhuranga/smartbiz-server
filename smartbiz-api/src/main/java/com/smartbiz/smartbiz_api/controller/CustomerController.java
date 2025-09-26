package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.CustomerDto;
import com.smartbiz.smartbiz_api.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    // Get userId from token/session - simplified here
    private Long getUserId(HttpServletRequest request) {
        // Replace with your JWT util
        return (Long) request.getAttribute("userId");
    }

    private Long parseId(String raw) {
        if (raw == null || !raw.matches("\\d+")) {
            log.warn("Invalid id path value received: {}", raw);
            throw new IllegalArgumentException("Invalid id value: " + raw);
        }
        return Long.parseLong(raw);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> saveCustomer(HttpServletRequest request, @RequestBody CustomerDto customerDto) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(customerService.saveCustomer(userId, customerDto));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(customerService.getCustomersByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            HttpServletRequest request,
            @PathVariable("id") String id,
            @RequestBody CustomerDto customerDto
    ) {
        Long userId = getUserId(request);
        Long customerId = parseId(id);
        return ResponseEntity.ok(customerService.updateCustomer(userId, customerId, customerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(HttpServletRequest request, @PathVariable("id") String id) {
        Long userId = getUserId(request);
        Long customerId = parseId(id);
        customerService.deleteCustomer(userId, customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(
            HttpServletRequest request,
            @PathVariable("id") String id
    ) {
        Long userId = getUserId(request);
        Long customerId = parseId(id);
        return ResponseEntity.ok(customerService.getCustomerById(userId, customerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> searchCustomersByName(
            HttpServletRequest request,
            @RequestParam String name
    ) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(customerService.searchCustomersByName(userId, name));
    }

}
