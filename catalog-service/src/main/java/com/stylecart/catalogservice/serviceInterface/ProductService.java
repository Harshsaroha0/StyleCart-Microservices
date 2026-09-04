package com.stylecart.catalogservice.serviceInterface;

import com.stylecart.catalogservice.dto.ProductRequest;
import com.stylecart.catalogservice.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

    ProductResponse update(Long id , ProductRequest request);

    void delete(Long id);

    Page<ProductResponse> filterProducts(String search, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice , Pageable pageable);
}