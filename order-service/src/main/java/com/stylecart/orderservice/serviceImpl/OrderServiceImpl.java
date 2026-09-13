package com.stylecart.orderservice.serviceImpl;

import com.stylecart.orderservice.client.CatalogClient;
import com.stylecart.orderservice.client.InventoryClient;
import com.stylecart.orderservice.dto.*;
import com.stylecart.orderservice.entity.Order;
import com.stylecart.orderservice.entity.OrderItems;
import com.stylecart.orderservice.entity.OrderStatus;
import com.stylecart.orderservice.exception.ResourceNotFoundException;
import com.stylecart.orderservice.repository.OrderItemRepository;
import com.stylecart.orderservice.repository.OrderRepository;
import com.stylecart.orderservice.serviceInterface.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CatalogClient catalogClient;
    private final InventoryClient inventoryClient;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        Order order = new Order();

        order.setUserId(request.getUserId());
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItems> reservedItems = new ArrayList<>();

        try {

            for (OrderItemRequest itemRequest : request.getItems()) {

                ProductVariantResponse product =
                        catalogClient.getProductVariant(
                                itemRequest.getProductVariantId()
                        );

                InventoryAvailabilityResponse inventory =
                        inventoryClient.getInventory(
                                itemRequest.getProductVariantId()
                        );

                if (inventory.getAvailableStock()
                        < itemRequest.getQuantity()) {

                    throw new IllegalStateException(
                            "Insufficient stock for product variant: "
                                    + itemRequest.getProductVariantId()
                    );
                }

                StockRequest stockRequest = new StockRequest();
                stockRequest.setQuantity(itemRequest.getQuantity());

                inventoryClient.reserveStock(
                        itemRequest.getProductVariantId(),
                        stockRequest
                );

                BigDecimal unitPrice = product.getPrice();

                BigDecimal subTotal =
                        unitPrice.multiply(
                                BigDecimal.valueOf(
                                        itemRequest.getQuantity()
                                )
                        );

                OrderItems orderItem = new OrderItems();

                orderItem.setOrder(order);
                orderItem.setProductVariantId(product.getId());
                orderItem.setProductName(product.getProductName());
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setUnitPrice(unitPrice);
                orderItem.setSubTotal(subTotal);

                order.getItems().add(orderItem);

                reservedItems.add(orderItem);

                totalAmount = totalAmount.add(subTotal);
            }

            order.setTotalAmount(totalAmount);

            Order savedOrder = orderRepository.save(order);

            return mapToResponse(savedOrder);

        } catch (Exception ex) {

            for (OrderItems item : reservedItems) {

                try {

                    StockRequest stockRequest = new StockRequest();
                    stockRequest.setQuantity(item.getQuantity());

                    inventoryClient.releaseStock(
                            item.getProductVariantId(),
                            stockRequest
                    );

                } catch (Exception releaseException) {

                }
            }

            throw ex;
        }
    }


    @Override
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findOrderWithItemsById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "order not exist" + orderId
                        )
                );

        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {

        Order order = orderRepository.findOrderWithItemsById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order does not exist: " + orderId
                        )
                );

        if (order.getStatus() != OrderStatus.PENDING &&
                order.getStatus() != OrderStatus.CONFIRMED) {

            throw new IllegalStateException(
                    "Order cannot be cancelled in status: "
                            + order.getStatus()
            );
        }

        for (OrderItems item : order.getItems()) {

            StockRequest stockRequest = new StockRequest();
            stockRequest.setQuantity(item.getQuantity());

            inventoryClient.releaseStock(
                    item.getProductVariantId(),
                    stockRequest
            );
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse confirmOrder(Long orderId) {

        Order order = orderRepository.findOrderWithItemsById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order does not exist: " + orderId
                        )
                );

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Order cannot be confirmed in status: "
                            + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.CONFIRMED);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse shipOrder(Long orderId) {

        Order order = orderRepository.findOrderWithItemsById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order does not exist: " + orderId
                        )
                );

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Order cannot be shipped in status: "
                            + order.getStatus()
            );
        }

        for (OrderItems item : order.getItems()) {

            StockRequest stockRequest = new StockRequest();
            stockRequest.setQuantity(item.getQuantity());

            inventoryClient.removeStock(
                    item.getProductVariantId(),
                    stockRequest
            );
        }

        order.setStatus(OrderStatus.SHIPPED);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse deliverOrder(Long orderId) {

        Order order = orderRepository.findOrderWithItemsById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order does not exist: " + orderId
                        )
                );

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException(
                    "Order cannot be delivered in status: "
                            + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.DELIVERED);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    private OrderResponse mapToResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setShippingAddress(order.getShippingAddress());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(item -> {

                    OrderItemResponse itemResponse = new OrderItemResponse();

                    itemResponse.setProductVariantId(item.getProductVariantId());
                    itemResponse.setProductName(item.getProductName());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setUnitPrice(item.getUnitPrice());
                    itemResponse.setSubTotal(item.getSubTotal());

                    return itemResponse;
                })
                .toList();

        response.setItems(itemResponses);

        return response;
    }
}
