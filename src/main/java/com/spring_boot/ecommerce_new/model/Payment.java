package com.spring_boot.ecommerce_new.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    @OneToOne(mappedBy = "payment",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Order order;

    @NotBlank
    @Size(min=4,message = "payment method name should be atleast 3 characters")
    private String paymentMethod;

}
