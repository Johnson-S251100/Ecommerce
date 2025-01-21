package com.spring_boot.ecommerce_new.payload;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredentials {

    @Email
    private String email;
    private String password;
}
