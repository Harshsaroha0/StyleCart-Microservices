package com.stylecart.cartservice.serviceImpl;

import com.stylecart.cartservice.client.CatalogClient;
import com.stylecart.cartservice.client.InventoryClient;
import com.stylecart.cartservice.dto.CartItemRequest;
import com.stylecart.cartservice.dto.CartItemResponse;
import com.stylecart.cartservice.dto.CartResponse;
import com.stylecart.cartservice.dto.UpdateCartItemRequest;
import com.stylecart.cartservice.dto.client.InventoryAvailabilityResponse;
import com.stylecart.cartservice.dto.client.ProductVariantResponse;
import com.stylecart.cartservice.entity.Cart;
import com.stylecart.cartservice.entity.CartItem;
import com.stylecart.cartservice.exception.InsufficientStockException;
import com.stylecart.cartservice.exception.ResourceNotFoundException;
import com.stylecart.cartservice.repository.CartItemRepository;
import com.stylecart.cartservice.repository.CartRepository;
import com.stylecart.cartservice.serviceInterface.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CatalogClient catalogClient;
    private final InventoryClient inventoryClient;

    @Override
    public CartResponse getCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            Long variantId = cartItem.getProductVariantId();

            ProductVariantResponse variant =
                    catalogClient.getProductVariant(variantId);

            BigDecimal price = variant.getPrice();

            BigDecimal subTotal = price.multiply(
                    BigDecimal.valueOf(cartItem.getQuantity())
            );

            CartItemResponse response = new CartItemResponse();

            response.setId(cartItem.getId());
            response.setProductVariantId(variant.getId());
            response.setProductName(variant.getProductName());
            response.setSize(variant.getSize());
            response.setColor(variant.getColor());
            response.setPrice(price);
            response.setQuantity(cartItem.getQuantity());
            response.setSubTotal(subTotal);

            itemResponses.add(response);

            total = total.add(subTotal);
        }

        CartResponse cartResponse = new CartResponse();

        cartResponse.setId(cart.getId());
        cartResponse.setUserId(cart.getUserId());
        cartResponse.setItems(itemResponses);
        cartResponse.setTotal(total);

        return cartResponse;
    }

    @Override
    public CartResponse addItem(
            Long userId,
            CartItemRequest cartItemRequest) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        Long variantId = cartItemRequest.getProductVariantId();

        ProductVariantResponse variant =
                catalogClient.getProductVariant(variantId);

        if (variant == null) {
            throw new ResourceNotFoundException(
                    "Product variant not found");
        }

        InventoryAvailabilityResponse inventory =
                inventoryClient.getInventory(variantId);

        if (inventory == null) {
            throw new ResourceNotFoundException(
                    "Inventory not found");
        }

        Integer requestedQuantity =
                cartItemRequest.getQuantity();

        Optional<CartItem> existingItem =
                cartItemRepository.findByCartIdAndProductVariantId(
                        cart.getId(),
                        variantId
                );

        int finalQuantity;

        if (existingItem.isPresent()) {

            CartItem cartItem = existingItem.get();

            finalQuantity =
                    cartItem.getQuantity() + requestedQuantity;

            // 4. Check available stock
            if (finalQuantity > inventory.getAvailableStock()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product variant"
                );
            }

            cartItem.setQuantity(finalQuantity);

            cartItemRepository.save(cartItem);

        } else {

            finalQuantity = requestedQuantity;

            // 5. Check available stock
            if (finalQuantity > inventory.getAvailableStock()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product variant"
                );
            }

            // 6. Create new cart item
            CartItem cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProductVariantId(variantId);
            cartItem.setQuantity(finalQuantity);

            cartItemRepository.save(cartItem);
        }

        return getCart(userId);
    }

    @Override
    public CartResponse updateItem(Long userId, Long cartItemId, UpdateCartItemRequest request) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository
                .findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("CartItem not found"));

        Long variantId = cartItem.getProductVariantId();

        InventoryAvailabilityResponse inventory =
                inventoryClient.getInventory(variantId);

        if (inventory == null) {
            throw new ResourceNotFoundException(
                    "Inventory not found");
        }

        Integer quantity = request.getQuantity();

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        int availableStock =
                inventory.getStock() - inventory.getReservedStock();

        if (quantity > availableStock) {
            throw new InsufficientStockException("Insufficient stock");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return getCart(userId);

    }

    @Override
    public CartResponse removeItem(Long userId, Long cartItemId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository
                .findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("CartItem not found"));

        cartItemRepository.delete(cartItem);

        return getCart(userId);
    }

    @Override
    public CartResponse clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        cartItemRepository.deleteByCartId(cart.getId());

        return getCart(userId);
    }
}
