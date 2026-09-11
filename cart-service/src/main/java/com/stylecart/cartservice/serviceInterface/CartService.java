package com.stylecart.cartservice.serviceInterface;

import com.stylecart.cartservice.dto.CartItemRequest;
import com.stylecart.cartservice.dto.CartResponse;
import com.stylecart.cartservice.dto.UpdateCartItemRequest;

public interface CartService {

    CartResponse getCart(Long userId);

    CartResponse addItem(
            Long userId,
            CartItemRequest cartItemRequest
    );

    CartResponse updateItem(
            Long userId,
            Long cartItemId,
            UpdateCartItemRequest request
    );

    CartResponse removeItem(
            Long userId,
            Long cartItemId
    );

    CartResponse clearCart(Long userId);
}
