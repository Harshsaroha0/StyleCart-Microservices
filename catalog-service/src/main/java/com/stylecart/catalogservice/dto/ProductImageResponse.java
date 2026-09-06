package com.stylecart.catalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String imageUrl;
    private boolean primary;
    private Integer displayOrder;

}
