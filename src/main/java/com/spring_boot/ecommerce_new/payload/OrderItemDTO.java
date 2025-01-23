package com.spring_boot.ecommerce_new.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private UUID orderItemId;
    private ProductDto productDto;
    private int quantity;
    private double discount;
    private double orderedProductPrice;
}
