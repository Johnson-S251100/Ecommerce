package com.spring_boot.ecommerce_new.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private List<OrderDTO> content;
    private int pageNumber;
    private int pageSize;
}
