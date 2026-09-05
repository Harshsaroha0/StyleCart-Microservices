package com.stylecart.catalogservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantResponse {

    private Long id;
    private String productName;
    private Long productId;
    private String size;
    private String color;
    private BigDecimal price;
}