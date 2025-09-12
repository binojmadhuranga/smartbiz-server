package com.smartbiz.smartbiz_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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

    private Integer quantity;

    private Double unitSellingPrice;

    private Double unitBuyingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToMany(mappedBy = "items")
    @JsonIgnore
    private Set<Supplier> suppliers = new HashSet<>();

}
