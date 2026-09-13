package com.stylecart.orderservice.serviceInterface;

import com.stylecart.orderservice.dto.OrderRequest;
import com.stylecart.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(Long orderId);

    List<OrderResponse> getOrdersByUserId(Long userId);

    OrderResponse cancelOrder(Long orderId);

    OrderResponse confirmOrder(Long orderId);

    OrderResponse shipOrder(Long orderId);

    OrderResponse deliverOrder(Long orderId);
}
