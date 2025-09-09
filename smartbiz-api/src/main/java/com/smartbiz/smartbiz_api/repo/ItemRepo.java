package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(String name);
}



