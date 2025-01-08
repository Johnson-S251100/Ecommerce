package com.spring_boot.ecommerce_new.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @NotBlank
    @Size(min=3,message = "Product name minimum have more than 3 letters")
    private String productName;

    @NotBlank
    @Size(min = 5,message = "Product desc must have more than 5 letters")
    private String productDescription;

    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch =FetchType.EAGER)
    private List<CartItem> products=new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<OrderItem> orderItems=new ArrayList<>();
}
