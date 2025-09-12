package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepo extends JpaRepository<Item, Long> {

    // search only user's items
    List<Item> findByNameContainingIgnoreCaseAndUser_Id(String name, Long userId);

    // get all items for a user
    @EntityGraph(attributePaths = {"suppliers"})
    List<Item> findByUser_Id(Long userId);

    // get item by itemId and user
    Optional<Item> findByItemIdAndUser_Id(Long itemId, Long userId);



}
