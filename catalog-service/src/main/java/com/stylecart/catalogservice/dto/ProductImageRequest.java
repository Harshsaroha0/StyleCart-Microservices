package com.stylecart.catalogservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductImageRequest {

    @NotBlank @Size(max = 500)
    private String imageUrl;

    private boolean primary;

    @Min(0)
    private Integer displayOrder;

    @NotNull
    private Long productId;

}
