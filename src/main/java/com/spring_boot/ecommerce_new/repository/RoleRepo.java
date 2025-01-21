package com.spring_boot.ecommerce_new.repository;

import com.spring_boot.ecommerce_new.model.Role;
import org.hibernate.annotations.CompositeTypeRegistrations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {
    Role findByRoleName(String string);
}
