package com.stylecart.catalogservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductVariant  extends  BaseEntity{

    @NotBlank
    @Size(max = 20)
    private String size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Size(max = 50)
    private String color;

    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal price;

}
