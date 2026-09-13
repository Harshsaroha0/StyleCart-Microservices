package com.stylecart.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductVariantResponse {

    private Long id;
    private String productName;
    private BigDecimal price;
}
