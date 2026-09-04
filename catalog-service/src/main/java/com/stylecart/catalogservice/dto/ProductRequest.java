package com.stylecart.catalogservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank @Size(min = 3 , max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NonNull @DecimalMin(value = "0.01")
    private BigDecimal price;

    @NonNull @Min(value = 0)
    private Integer stockQuantity;

    @NonNull
    private Long categoryId;

}
