package com.stylecart.orderservice.client;

import com.stylecart.orderservice.dto.ProductVariantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog-service")
public interface CatalogClient {

    @GetMapping("/api/product-variants/{variantId}")
    ProductVariantResponse getProductVariant(
            @PathVariable("variantId") Long variantId
    );
}
