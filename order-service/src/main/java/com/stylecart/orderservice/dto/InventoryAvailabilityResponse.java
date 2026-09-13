package com.stylecart.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryAvailabilityResponse {

    private Long id;

    private Long productVariantId;

    private Integer stock;

    private Integer reservedStock;

    private Integer availableStock;

    private Integer lowStockThreshold;
}