package com.spring_boot.ecommerce_new.payload;

import com.spring_boot.ecommerce_new.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String password;
    private Set<RoleDTO> roles;
    private List<AddressDTO> address;
    private CartDTO cartDTO;
}
