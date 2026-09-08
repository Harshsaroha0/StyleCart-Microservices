package com.stylecart.inventoryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponse {

    private Long id;

    private Long productVariantId;

    private Integer stock;

    private Integer reservedStock;

    private Integer availableStock;

    private Integer lowStockThreshold;

    private Long version;


}
