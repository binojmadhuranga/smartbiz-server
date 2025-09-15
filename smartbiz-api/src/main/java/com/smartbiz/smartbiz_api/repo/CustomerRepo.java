package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    // updated to match User entity primary key field name 'id'
    List<Customer> findByUser_Id(Long userId);

    List<Customer> findByUser_IdAndNameContainingIgnoreCase(Long userId, String name);

}
