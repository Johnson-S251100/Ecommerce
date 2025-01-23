package com.spring_boot.ecommerce_new.repository;

import com.spring_boot.ecommerce_new.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, UUID> {

    @Query("SELECT ci FROM CartItem ci where ci.cart.id=?1 and ci.product.id=?2")
    CartItem findCartItemByProductIdAndCartId(UUID cartId, UUID productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id=?1 and ci.product.id=?2")
    void deleteCartItemByProductIdAndCartId(UUID cartId, UUID productId);
}
