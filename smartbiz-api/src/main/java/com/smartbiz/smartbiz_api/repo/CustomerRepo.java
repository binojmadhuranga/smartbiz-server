package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

}
