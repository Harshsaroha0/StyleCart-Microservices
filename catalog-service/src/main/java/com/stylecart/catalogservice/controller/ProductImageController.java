package com.stylecart.catalogservice.controller;


import com.stylecart.catalogservice.dto.ProductImageRequest;
import com.stylecart.catalogservice.dto.ProductImageResponse;
import com.stylecart.catalogservice.serviceInterface.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<ProductImageResponse> create(
            @Valid @RequestBody ProductImageRequest request) {

        ProductImageResponse response = productImageService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageResponse> getById (
            @PathVariable Long id ) {

        return ResponseEntity.ok(productImageService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductImageResponse>> getAll() {

        return ResponseEntity.ok(productImageService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImageResponse> update (
            @PathVariable Long id , @Valid @RequestBody ProductImageRequest request) {

        return ResponseEntity.ok(productImageService.update(id , request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        productImageService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
