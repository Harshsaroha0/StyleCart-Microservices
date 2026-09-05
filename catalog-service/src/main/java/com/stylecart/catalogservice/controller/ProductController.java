package com.stylecart.catalogservice.controller;

import com.stylecart.catalogservice.dto.ProductRequest;
import com.stylecart.catalogservice.dto.ProductResponse;
import com.stylecart.catalogservice.serviceInterface.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Set;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getById(
            @PathVariable long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PutMapping("{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> update(
            @PathVariable long id, @Valid @RequestBody ProductRequest request) {

        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable long id) {

        productService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(0) @Max(50) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Set<String> allowedSortFields = Set.of(
                "id",
                "name",
                "price",
                "stockQuantity"
        );

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'"
            );
        }

        if (minPrice != null && maxPrice != null
                && minPrice.compareTo(maxPrice) > 0) {

            throw new IllegalArgumentException(
                    "Minimum price cannot be greater than maximum price"
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        if (minPrice != null && maxPrice != null
                && minPrice.compareTo(maxPrice) > 0) {

            throw new IllegalArgumentException(
                    "minPrice cannot be greater than maxPrice"
            );
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                productService.filterProducts(search,categoryId, minPrice,maxPrice,pageable));
    }
}

