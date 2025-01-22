package com.spring_boot.ecommerce_new.repository;

import com.spring_boot.ecommerce_new.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface OrderRepo extends JpaRepository<Order,UUID> {

    List<Order> findByEmail(String emailId);

    Order findByEmailAndOrderId(String email,UUID orderId);
}
