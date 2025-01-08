package com.spring_boot.ecommerce_new.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID productId;
    private String productName;
    private String ProductDescription;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

}
