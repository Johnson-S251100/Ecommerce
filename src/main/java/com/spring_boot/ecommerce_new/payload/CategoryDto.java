package com.spring_boot.ecommerce_new.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private UUID categoryId;
    private String categoryName;
}
