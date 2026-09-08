package com.stylecart.inventoryservice.repository;

import com.stylecart.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductVariantId(Long productVariantId);

    boolean existsByProductVariantId(Long productVariantId);

    @Modifying
    @Query("""
        UPDATE Inventory i
        SET i.reservedStock = i.reservedStock + :quantity
        WHERE i.productVariantId = :productVariantId
        AND i.stock - i.reservedStock >= :quantity
        """)
    int reserveStock(
            @Param("productVariantId") Long productVariantId,
            @Param("quantity") Integer quantity
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
        SET i.reservedStock = i.reservedStock - :quantity
        WHERE i.productVariantId = :productVariantId
        AND i.reservedStock >= :quantity
        """)
    int releaseStock(
            @Param("productVariantId") Long productVariantId,
            @Param("quantity") Integer quantity
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
        SET i.stock = i.stock - :quantity,
            i.reservedStock = i.reservedStock - :quantity
        WHERE i.productVariantId = :productVariantId
        AND i.stock >= :quantity
        AND i.reservedStock >= :quantity
        """)
    int removeStock(
            @Param("productVariantId") Long productVariantId,
            @Param("quantity") Integer quantity
    );
}