package com.stylecart.orderservice.client;

import com.stylecart.orderservice.dto.InventoryAvailabilityResponse;
import com.stylecart.orderservice.dto.StockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/variant/{productVariantId}")
    InventoryAvailabilityResponse getInventory(
            @PathVariable("productVariantId") Long productVariantId
    );


    @PostMapping("/api/inventory/variant/{productVariantId}/reserve")
    void reserveStock(
            @PathVariable("productVariantId") Long productVariantId,
            @RequestBody StockRequest request
    );

    @PostMapping("/api/inventory/variant/{productVariantId}/release")
    void releaseStock(
            @PathVariable("productVariantId") Long productVariantId,
            @RequestBody StockRequest request
    );

    @PostMapping("/api/inventory/variant/{productVariantId}/remove")
    void removeStock(
            @PathVariable("productVariantId") Long productVariantId,
            @RequestBody StockRequest request
    );
}
