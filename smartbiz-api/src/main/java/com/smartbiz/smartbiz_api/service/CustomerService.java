package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.CustomerDto;
import java.util.List;

public interface CustomerService {
    CustomerDto saveCustomer(Long userId, CustomerDto customerDto);

    List<CustomerDto> getCustomersByUser(Long userId);

    CustomerDto updateCustomer(Long userId, Long customerId, CustomerDto customerDto);

    void deleteCustomer(Long userId, Long customerId);

    CustomerDto getCustomerById(Long userId, Long customerId);

    List<CustomerDto> searchCustomersByName(Long userId, String name);

}
