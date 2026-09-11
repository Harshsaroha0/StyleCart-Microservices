package com.stylecart.cartservice.controller;

import com.stylecart.cartservice.dto.CartItemRequest;
import com.stylecart.cartservice.dto.CartResponse;
import com.stylecart.cartservice.dto.UpdateCartItemRequest;
import com.stylecart.cartservice.serviceInterface.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.getCart(userId)
        );
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponse> addItem(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequest request) {

        CartResponse response =
                cartService.addItem(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        return ResponseEntity.ok(
                cartService.updateItem(
                        userId,
                        cartItemId,
                        request
                )
        );
    }

    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId) {

        return ResponseEntity.ok(
                cartService.removeItem(
                        userId,
                        cartItemId
                )
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CartResponse> clearCart(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.clearCart(userId)
        );
    }
}