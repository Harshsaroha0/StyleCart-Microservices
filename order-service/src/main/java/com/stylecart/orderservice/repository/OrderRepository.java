package com.stylecart.orderservice.repository;


import com.stylecart.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @Query("""
       SELECT DISTINCT o
       FROM Order o
       LEFT JOIN FETCH o.items
       WHERE o.id = :orderId
       """)
    Optional<Order> findOrderWithItemsById(@Param("orderId") Long orderId);


}
