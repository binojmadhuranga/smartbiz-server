package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Supplier;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    @EntityGraph(attributePaths = {"items"})
    List<Supplier> findByUserId(Long userId);


    List<Supplier> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);

    long countByUserId(Long userId);

}
