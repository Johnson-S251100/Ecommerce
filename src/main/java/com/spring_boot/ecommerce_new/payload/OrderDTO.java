package com.spring_boot.ecommerce_new.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private UUID orderId;
    private String email;
    private List<OrderItemDTO> orderItemDTOS=new ArrayList<>();
    private LocalDate orderDate;
    private ProductDto payment;
    private double totalAmount;
    private String orderStatus;
}
