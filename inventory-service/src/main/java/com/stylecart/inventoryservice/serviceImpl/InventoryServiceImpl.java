package com.stylecart.inventoryservice.serviceImpl;


import com.stylecart.inventoryservice.dto.InventoryRequest;
import com.stylecart.inventoryservice.dto.InventoryResponse;
import com.stylecart.inventoryservice.entity.Inventory;
import com.stylecart.inventoryservice.exception.InsufficientStockException;
import com.stylecart.inventoryservice.exception.NameAlreadyExistsException;
import com.stylecart.inventoryservice.exception.ResourceNotFoundException;
import com.stylecart.inventoryservice.repository.InventoryRepository;
import com.stylecart.inventoryservice.serviceInterface.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;


    @Override
    public InventoryResponse createInventory(InventoryRequest request) {

        // Check whether inventory already exists
        if (inventoryRepository.existsByProductVariantId(
                request.getProductVariantId())) {

            throw new NameAlreadyExistsException(
                    "Inventory already exists for product variant id: "
                            + request.getProductVariantId()
            );
        }

        // Validate reserved stock
        if (request.getReservedStock() > request.getStock()) {

            throw new IllegalArgumentException(
                    "Reserved stock cannot be greater than stock"
            );
        }

        Inventory inventory = new Inventory();

        inventory.setProductVariantId(
                request.getProductVariantId()
        );

        inventory.setStock(request.getStock());

        inventory.setReservedStock(
                request.getReservedStock()
        );

        inventory.setLowStockThreshold(
                request.getLowStockThreshold()
        );

        Inventory savedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(savedInventory);
    }


    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryById(Long id) {

        Inventory inventory =
                inventoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(inventory);
    }


    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByVariantId(
            Long productVariantId) {

        Inventory inventory =
                inventoryRepository
                        .findByProductVariantId(productVariantId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found for product variant id: "
                                                + productVariantId
                                )
                        );

        return mapToResponse(inventory);
    }


    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public InventoryResponse updateInventory(
            Long id,
            InventoryRequest request) {

        Inventory inventory =
                inventoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found with id: "
                                                + id
                                )
                        );

        // Validate reserved stock
        if (request.getReservedStock() > request.getStock()) {

            throw new IllegalArgumentException(
                    "Reserved stock cannot be greater than stock"
            );
        }

        // Check if product variant is being changed
        if (!inventory.getProductVariantId()
                .equals(request.getProductVariantId())) {

            if (inventoryRepository.existsByProductVariantId(
                    request.getProductVariantId())) {

                throw new IllegalArgumentException(
                        "Inventory already exists for product variant: "
                                + request.getProductVariantId()
                );
            }

            inventory.setProductVariantId(
                    request.getProductVariantId()
            );
        }

        inventory.setStock(request.getStock());

        inventory.setReservedStock(
                request.getReservedStock()
        );

        inventory.setLowStockThreshold(
                request.getLowStockThreshold()
        );

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }


    @Override
    public void deleteInventory(Long id) {

        Inventory inventory =
                inventoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found with id: "
                                                + id
                                )
                        );

        inventoryRepository.delete(inventory);
    }


    @Override
    public void reserveStock(
            Long productVariantId,
            Integer quantity) {

        validateQuantity(quantity);

        int updatedRows =
                inventoryRepository.reserveStock(
                        productVariantId,
                        quantity
                );

        if (updatedRows == 0) {

            checkInventoryExists(productVariantId);

            throw new InsufficientStockException(
                    "Insufficient available stock for product variant: "
                            + productVariantId
            );
        }
    }


    @Override
    public void releaseStock(
            Long productVariantId,
            Integer quantity) {

        validateQuantity(quantity);

        int updatedRows =
                inventoryRepository.releaseStock(
                        productVariantId,
                        quantity
                );

        if (updatedRows == 0) {

            checkInventoryExists(productVariantId);

            throw new IllegalArgumentException(
                    "Cannot release more stock than reserved"
            );
        }
    }


    @Override
    public void removeStock(
            Long productVariantId,
            Integer quantity) {

        validateQuantity(quantity);

        int updatedRows =
                inventoryRepository.removeStock(
                        productVariantId,
                        quantity
                );

        if (updatedRows == 0) {

            checkInventoryExists(productVariantId);

            throw new IllegalArgumentException(
                    "Cannot remove more stock than reserved"
            );
        }
    }


    private void validateQuantity(Integer quantity) {

        if (quantity == null || quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }
    }


    private void checkInventoryExists(Long productVariantId) {

        if (!inventoryRepository.existsByProductVariantId(
                productVariantId)) {

            throw new ResourceNotFoundException(
                    "Inventory not found for product variant id: "
                            + productVariantId
            );
        }
    }


    private InventoryResponse mapToResponse(
            Inventory inventory) {

        InventoryResponse response =
                new InventoryResponse();

        response.setId(inventory.getId());

        response.setProductVariantId(
                inventory.getProductVariantId()
        );

        response.setStock(
                inventory.getStock()
        );

        response.setReservedStock(
                inventory.getReservedStock()
        );

        response.setAvailableStock(
                inventory.getStock()
                        - inventory.getReservedStock()
        );

        response.setLowStockThreshold(
                inventory.getLowStockThreshold()
        );

        response.setVersion(
                inventory.getVersion()
        );

        return response;
    }
}