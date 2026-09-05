package com.stylecart.catalogservice.serviceInterface;


import com.stylecart.catalogservice.dto.ProductVariantRequest;
import com.stylecart.catalogservice.dto.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {

    ProductVariantResponse create(ProductVariantRequest request);
    ProductVariantResponse getById(Long id);
    ProductVariantResponse update(Long id , ProductVariantRequest request);
    List<ProductVariantResponse> getAll();
    void delete(Long id);
}
