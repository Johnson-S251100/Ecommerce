package com.spring_boot.ecommerce_new.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Size(min = 3,max = 20,message = "FirstName have min 3 letter Max 20 letters.")
    @Pattern(regexp = "^[a-zA-Z]*$",message = "FirstName should not contains any special characters")
    private String firstName;

    @Size(min = 1,max = 20,message = "LastName have min 3 letter Max 20 letters.")
    @Pattern(regexp = "^[a-zA-Z]*$",message = "LastName should not contains any special characters")
    private String lastName;

    @Size(min = 10,message = "Mobile Number must have 10 numbers")
    @Pattern(regexp = "^\\d{10}$",message = "Mobile number must contain only numbers")
    private String mobileNumber;

    @Email
    @Column(unique = true,nullable = false)
    private String email;

    @NotBlank
    @Size(min = 3,message = "Password must have more than 3 letters")
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles=new HashSet<>();

    @ManyToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    List<Address> addresses=new ArrayList<>();

    @OneToOne(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
    private Cart cart;

}


