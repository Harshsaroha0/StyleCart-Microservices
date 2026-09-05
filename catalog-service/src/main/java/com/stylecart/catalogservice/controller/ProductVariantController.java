package com.stylecart.catalogservice.controller;

import com.stylecart.catalogservice.dto.ProductVariantRequest;
import com.stylecart.catalogservice.dto.ProductVariantResponse;
import com.stylecart.catalogservice.serviceInterface.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductVariantResponse> create(
            @Valid @RequestBody ProductVariantRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productVariantService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantResponse>> getAll() {

        return ResponseEntity.ok(
                productVariantService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productVariantService.getById(id)
        );
    }

    @PutMapping("/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductVariantResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductVariantRequest request) {

        return ResponseEntity.ok(
                productVariantService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        productVariantService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
