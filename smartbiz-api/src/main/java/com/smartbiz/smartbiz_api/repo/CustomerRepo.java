package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
//    Optional<Customer> findByEmail(String email);

}
