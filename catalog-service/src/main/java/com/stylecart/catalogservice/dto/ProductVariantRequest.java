package com.stylecart.catalogservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantRequest {

    @NonNull
    private Long productId;

    @NotBlank
    private String size;

    @NotBlank
    private String color;

    @NonNull
    @DecimalMin(value = "0.01")
    private BigDecimal price;
}
