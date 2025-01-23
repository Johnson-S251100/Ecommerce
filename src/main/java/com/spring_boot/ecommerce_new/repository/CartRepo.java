package com.spring_boot.ecommerce_new.repository;

import com.spring_boot.ecommerce_new.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;

public interface CartRepo extends JpaRepository<Cart, UUID> {
    Cart findByUserUserId(UUID userId);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 and c.id = ?2 ")
    Cart findCartByEmailAndCartId(String emailId, UUID cartId);
}
