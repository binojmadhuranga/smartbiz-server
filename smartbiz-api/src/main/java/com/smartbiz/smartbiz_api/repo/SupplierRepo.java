package com.smartbiz.smartbiz_api.repo;


import com.smartbiz.smartbiz_api.entity.Supplier;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    @EntityGraph(attributePaths = {"items"})
    List<Supplier> findByUserId(Long userId);

    // New method for search
    List<Supplier> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);

    // ✅ Get suppliers that provide a specific item (fetch items in one query)
    @EntityGraph(attributePaths = {"items"})
    List<Supplier> findByItems_ItemId(Long itemId);

    // ✅ Alternative: explicit JOIN FETCH to avoid N+1
    @Query("SELECT DISTINCT s FROM Supplier s JOIN FETCH s.items i WHERE i.itemId = :itemId")
    List<Supplier> findSuppliersWithItemsByItemId(@Param("itemId") Long itemId);




}
