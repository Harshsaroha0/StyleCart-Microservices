package com.stylecart.inventoryservice.serviceInterface;


import com.stylecart.inventoryservice.dto.InventoryRequest;
import com.stylecart.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest request);

    InventoryResponse getInventoryById(Long id);

    InventoryResponse getInventoryByVariantId(Long productVariantId);

    List<InventoryResponse> getAllInventory();

    InventoryResponse updateInventory(Long id, InventoryRequest request);

    void deleteInventory(Long id);

    void reserveStock(Long productVariantId, Integer quantity);

    void releaseStock(Long productVariantId, Integer quantity);

    void removeStock(Long productVariantId, Integer quantity);
}