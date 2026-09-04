package com.stylecart.catalogservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
@Entity
public class Product extends BaseEntity {


    @Column(nullable = false , unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
