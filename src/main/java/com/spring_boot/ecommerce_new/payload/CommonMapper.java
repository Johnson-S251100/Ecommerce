package com.spring_boot.ecommerce_new.payload;

import com.spring_boot.ecommerce_new.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommonMapper {

    CommonMapper INSTANCE= Mappers.getMapper(CommonMapper.class);
    CategoryDto toCategoryDto(Category category);

    ProductDto toProductDto(Product product);


    UserDto toUserDto(User newUser);

    CartDTO toCartDTO(Cart cart);

    OrderDTO toOrderDTO(Order order);

    OrderItemDTO toOrderItemDTO(OrderItem item);

    AddressDTO toAdressDTO(Address newAddress);

    RoleDTO toRoleDTO(Role role);


}
