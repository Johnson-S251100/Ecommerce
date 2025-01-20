package com.spring_boot.ecommerce_new.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private List<UserDto> content;
    private int pageNum;
    private int pageSize;
}
