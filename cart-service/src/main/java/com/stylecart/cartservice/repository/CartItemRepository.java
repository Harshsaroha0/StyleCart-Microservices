package com.stylecart.cartservice.repository;

import com.stylecart.cartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByIdAndCartId(Long cartItemId, Long cartId);

    void deleteByCartId(Long id);
}
