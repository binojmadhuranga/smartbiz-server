package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.CustomerDto;
import com.smartbiz.smartbiz_api.entity.Customer;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.repo.CustomerRepo;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepository;
    private final UserRepo userRepository;

    public CustomerDto saveCustomer(Long userId, CustomerDto customerDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Customer customer = Customer.builder()
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .phone(customerDto.getPhone())
                .address(customerDto.getAddress())
                .user(user)
                .build();

        Customer saved = customerRepository.save(customer);
        return mapToDto(saved);
    }

    public List<CustomerDto> getCustomersByUser(Long userId) {
        return customerRepository.findByUser_Id(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CustomerDto updateCustomer(Long userId, Long customerId, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this customer");
        }

        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());

        return mapToDto(customerRepository.save(customer));
    }

    public void deleteCustomer(Long userId, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this customer");
        }

        customerRepository.delete(customer);
    }

    private CustomerDto mapToDto(Customer customer) {
        return CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .build();
    }



}
