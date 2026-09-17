package com.stylecart.paymentservice.client;


import com.stylecart.paymentservice.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/orders/{orderId}")
    OrderResponse getOrderById(@PathVariable Long orderId);
}