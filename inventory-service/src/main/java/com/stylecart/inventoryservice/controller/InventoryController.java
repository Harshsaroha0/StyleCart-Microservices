package com.stylecart.inventoryservice.controller;

import com.stylecart.inventoryservice.dto.InventoryRequest;
import com.stylecart.inventoryservice.dto.InventoryResponse;
import com.stylecart.inventoryservice.dto.StockRequest;
import com.stylecart.inventoryservice.serviceInterface.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @Valid @RequestBody InventoryRequest request) {

        InventoryResponse response =
                inventoryService.createInventory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }



    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(id)
        );
    }


    @GetMapping("/variant/{productVariantId}")
    public ResponseEntity<InventoryResponse> getInventoryByVariantId(
            @PathVariable Long productVariantId) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByVariantId(productVariantId)
        );
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {

        return ResponseEntity.ok(
                inventoryService.getAllInventory()
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequest request) {

        return ResponseEntity.ok(
                inventoryService.updateInventory(id, request)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(
            @PathVariable Long id) {

        inventoryService.deleteInventory(id);

        return ResponseEntity
                .noContent()
                .build();
    }


    @PostMapping("/variant/{productVariantId}/reserve")
    public ResponseEntity<Void> reserveStock(
            @PathVariable Long productVariantId,
            @Valid @RequestBody StockRequest request) {

        inventoryService.reserveStock(
                productVariantId,
                request.getQuantity()
        );

        return ResponseEntity.ok().build();
    }


    @PostMapping("/variant/{productVariantId}/release")
    public ResponseEntity<Void> releaseStock(
            @PathVariable Long productVariantId,
            @Valid @RequestBody StockRequest request) {

        inventoryService.releaseStock(
                productVariantId,
                request.getQuantity()
        );

        return ResponseEntity.ok().build();
    }


    @PostMapping("/variant/{productVariantId}/remove")
    public ResponseEntity<Void> removeStock(
            @PathVariable Long productVariantId,
            @Valid @RequestBody StockRequest request) {

        inventoryService.removeStock(
                productVariantId,
                request.getQuantity()
        );

        return ResponseEntity.ok().build();
    }
}