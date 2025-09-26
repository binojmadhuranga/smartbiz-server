package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepo extends JpaRepository<Sale, Long> {

    List<Sale> findByUser_Id(Long userId);

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.user.id = :userId AND s.saleDate BETWEEN :start AND :end")
    Double getTotalSalesBetweenByUser(@Param("userId") Long userId,
                                      @Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);

    List<Sale> findByUserIdAndProductNameContainingIgnoreCase(Long userId, String keyword);

}
