package com.stylecart.cartservice.dto.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductVariantResponse {

    private Long id;

    private String productName;

    private String size;

    private String color;

    private BigDecimal price;
}
