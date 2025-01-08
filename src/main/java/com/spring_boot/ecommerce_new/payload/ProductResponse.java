package com.spring_boot.ecommerce_new.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private List<ProductDto> content;
    private int pageNum;
    private int pageSize;
}
