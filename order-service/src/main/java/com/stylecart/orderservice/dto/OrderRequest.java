package com.stylecart.orderservice.dto;

import com.stylecart.orderservice.entity.OrderItems;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String shippingAddress;

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}
