package com.spring_boot.ecommerce_new.repository;

import com.spring_boot.ecommerce_new.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface AddressRepo extends JpaRepository<Address, UUID> {
    List<Address> findByUserUserId(UUID userId);

    Address findByAddressIdAndUserUserId(UUID addressId, UUID userId);
}
