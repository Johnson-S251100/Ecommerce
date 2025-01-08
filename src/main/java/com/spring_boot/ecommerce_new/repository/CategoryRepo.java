package com.spring_boot.ecommerce_new.repository;

import com.spring_boot.ecommerce_new.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {
    Category findByCategoryName(String categoryName);
}
