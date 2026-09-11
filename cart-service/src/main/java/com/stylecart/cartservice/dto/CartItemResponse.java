package com.stylecart.cartservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemResponse {

    private Long id;
    private Long productVariantId;
    private String productName;
    private String size;
    private String color;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;
}
