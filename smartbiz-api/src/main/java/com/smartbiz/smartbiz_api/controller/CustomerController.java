package com.smartbiz.smartbiz_api.controller;

import com.smartbiz.smartbiz_api.dto.CustomerDto;
import com.smartbiz.smartbiz_api.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // Get userId from token/session - simplified here
    private Long getUserId(HttpServletRequest request) {
        // Replace with your JWT util
        return (Long) request.getAttribute("userId");
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
            @PathVariable Long id,
            @RequestBody CustomerDto customerDto
    ) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(customerService.updateCustomer(userId, id, customerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(HttpServletRequest request, @PathVariable Long id) {
        Long userId = getUserId(request);
        customerService.deleteCustomer(userId, id);
        return ResponseEntity.noContent().build();
    }

}
