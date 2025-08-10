package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.CustomerDto;
import com.smartbiz.smartbiz_api.entity.Customer;
import com.smartbiz.smartbiz_api.repo.CustomerRepo;
import com.smartbiz.smartbiz_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    private CustomerDto mapToDto(Customer customer) {
        return CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .build();
    }

    private Customer mapToEntity(CustomerDto dto) {
        return Customer.builder()
                .customerId(dto.getCustomerId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = mapToEntity(customerDto);
        Customer saved = customerRepo.save(customer);
        return mapToDto(saved);
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return mapToDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer existing = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existing.setName(customerDto.getName());
        existing.setEmail(customerDto.getEmail());
        existing.setPhone(customerDto.getPhone());
        existing.setAddress(customerDto.getAddress());

        Customer updated = customerRepo.save(existing);
        return mapToDto(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepo.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepo.deleteById(id);
    }

}
