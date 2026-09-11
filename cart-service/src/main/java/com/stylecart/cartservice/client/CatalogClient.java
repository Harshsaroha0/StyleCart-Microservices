package com.stylecart.cartservice.client;

import com.stylecart.cartservice.dto.client.ProductVariantResponse;
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
