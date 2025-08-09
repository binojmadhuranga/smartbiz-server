package com.smartbiz.smartbiz_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long itemId;

    private String name;

    private String description;

}
