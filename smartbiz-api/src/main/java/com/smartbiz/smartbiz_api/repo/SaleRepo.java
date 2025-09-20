package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepo extends JpaRepository<Sale, Long> {

    List<Sale> findByUser_Id(Long userId);

}
