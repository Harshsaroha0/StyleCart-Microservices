package com.stylecart.inventoryservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_inventory_product_variant",
                        columnNames = "product_variant_id"
                )
        }
)
public class Inventory extends BaseEntity {

    @Column(name = "product_variant_id", nullable = false, unique = true)
    private Long productVariantId;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer reservedStock;

    @Column(nullable = false)
    private Integer lowStockThreshold;

    @Version
    private Long version;
}