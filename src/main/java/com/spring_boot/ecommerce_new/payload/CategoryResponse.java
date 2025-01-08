package com.spring_boot.ecommerce_new.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.AnyKeyJavaClass;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private List<CategoryDto> content;
    private int pageNum;
    private int pageSize;

}
