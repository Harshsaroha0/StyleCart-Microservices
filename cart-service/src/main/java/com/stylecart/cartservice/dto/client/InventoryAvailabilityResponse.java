package com.stylecart.cartservice.dto.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryAvailabilityResponse {

    private Long productVariantId;

    private Integer stock;

    private Integer reservedStock;

    private Integer availableStock;
}