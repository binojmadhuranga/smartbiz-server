package com.smartbiz.smartbiz_api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long supplierId;

    private String name;
    private String email;
    private String phone;
    private String address;

    @Column(nullable = false)
    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "supplier_items",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    @Builder.Default
    @JsonIgnore
    @ToString.Exclude
    private Set<Item> items = new HashSet<>();
}
