package com.spring_boot.ecommerce_new.repository;

import com.spring_boot.ecommerce_new.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {
    Page<Product> findByProductNameContainingOrProductDescriptionContaining(String productName, String productDescription, Pageable pageRequest);

    Page<Product> findByCategoryCategoryId(UUID categoryId, Pageable pageable);

}
