package com.spring_boot.ecommerce_new.payload;

import com.spring_boot.ecommerce_new.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private UUID addressId;
    private String buildingName;
    private String city;
    private String country;
    private String pincode;
    private String state;
    private String street;
    private User user;
}
