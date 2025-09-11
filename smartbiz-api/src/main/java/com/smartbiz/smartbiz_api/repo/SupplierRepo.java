package com.smartbiz.smartbiz_api.repo;


import com.smartbiz.smartbiz_api.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    List<Supplier> findByUserId(Long userId);
}
