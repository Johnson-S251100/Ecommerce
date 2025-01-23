package com.spring_boot.ecommerce_new.repository;

import com.spring_boot.ecommerce_new.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PaymentRepo extends JpaRepository<Payment, UUID> {
}
