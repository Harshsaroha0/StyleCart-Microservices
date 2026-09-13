package com.stylecart.orderservice.repository;

import com.stylecart.orderservice.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {
}