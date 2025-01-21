package com.spring_boot.ecommerce_new.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    @NotBlank
    @Size(min=5,message = "Building name must have atleast 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 4,message = "City name must have atleast 4 characters")
    private String city;

    @NotBlank
    @Size(min = 2,message = "Country name must have atleast 2 Characters")
    private String country;

    @NotBlank
    @Size(min = 5,max = 6,message ="Pincode  must have atleast 6 characters" )
    private String pincode;

    @NotBlank
    @Size(min = 2,message = "state name must atleast 2 characters")
    private String state;

    @NotBlank
    @Size(min = 5,message = "Street name must atleast have 5 character")
    private String street;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    User user;
}
