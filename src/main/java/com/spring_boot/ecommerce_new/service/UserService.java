package com.spring_boot.ecommerce_new.service;

import com.spring_boot.ecommerce_new.configuration.AppConstants;
import com.spring_boot.ecommerce_new.exceptions.ApiException;
import com.spring_boot.ecommerce_new.exceptions.ResourceNotFoundException;
import com.spring_boot.ecommerce_new.model.*;
import com.spring_boot.ecommerce_new.payload.*;
import com.spring_boot.ecommerce_new.repository.CartRepo;
import com.spring_boot.ecommerce_new.repository.RoleRepo;
import com.spring_boot.ecommerce_new.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CartService cartService;
    @Autowired
    private RoleRepo roleRepo;

    public UserDto registerUser(User user) {
        User savedUser= userRepo.findByEmail(user.getEmail());
        Cart cart=new Cart();
        user.setCart(cart);

        if(savedUser != null){
            throw new ApiException("User Already Present with email "+user.getEmail());
        }

        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
        user.setPassword(encoder.encode(user.getPassword()));

        Role role=roleRepo.findByRoleName("USER");
        user.getRoles().add(role);
        User newUser=userRepo.save(user);

        cart.setUser(newUser);
        cartRepo.save(cart);

        return CommonMapper.INSTANCE.toUserDto(newUser);
    }

    public UserResponse getAllUsers(int pageNum, int pageSize) {

        Pageable pageDetail= PageRequest.of(pageNum,pageSize);
        Page<User> pagedUser=userRepo.findAll(pageDetail);
        List<User> users=pagedUser.getContent();
        if(users.size()==0){
            throw new ApiException("No User exists");
        }
        List<UserDto> userDtos=users.stream().map(user->{
            UserDto dto=CommonMapper.INSTANCE.toUserDto(user);
            CartDTO cartDTO=CommonMapper.INSTANCE.toCartDTO(user.getCart());

            List<ProductDto> productDtos=user.getCart().getCartItems().stream()
                    .map(item-> CommonMapper.INSTANCE.toProductDto(item.getProduct())).toList();

            Set<RoleDTO> roleDTOS = user.getRoles().stream()
                    .map(CommonMapper.INSTANCE::toRoleDTO).collect(Collectors.toSet());


            List<AddressDTO> addressDTOS = user.getAddresses().stream().map(
                    address -> CommonMapper.INSTANCE.toAdressDTO(address)
            ).toList();

            dto.setCartDTO(cartDTO);
            dto.getCartDTO().setProducts(productDtos);
            dto.setRoles(roleDTOS);
            dto.setAddress(addressDTOS);

            return dto;

        }).toList();
        UserResponse userResponse=new UserResponse();
        userResponse.setContent(userDtos);
        userResponse.setPageNum(pagedUser.getNumber());
        userResponse.setPageSize(pagedUser.getSize());

        return userResponse;
    }

    public UserDto getUser(HttpServletRequest request) {

        String token =jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);
        User user =userRepo.findByEmail(emailId);

        if(user == null){
            throw new ResourceNotFoundException("user","email",emailId);
        }
        UserDto userDto=CommonMapper.INSTANCE.toUserDto(user);

        CartDTO cartDTO=CommonMapper.INSTANCE.toCartDTO(user.getCart());
        List<ProductDto> productDtos=user.getCart().getCartItems().stream()
                .map(item -> CommonMapper.INSTANCE.toProductDto(item.getProduct())).toList();
        Set<RoleDTO> roleDTOS = user.getRoles().stream()
                .map(CommonMapper.INSTANCE::toRoleDTO).collect(Collectors.toSet());
        List<AddressDTO> addressDTOS = user.getAddresses().stream().map(
                address -> CommonMapper.INSTANCE.toAdressDTO(address)
        ).toList();
        userDto.setCartDTO(cartDTO);
        userDto.getCartDTO().setProducts(productDtos);
        userDto.setRoles(roleDTOS);
        userDto.setAddress(addressDTOS);

        return userDto;
    }

    public UserDto updateUser(UserDto userDto, HttpServletRequest request) {
        String token =jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);

        User users=userRepo.findByEmail(emailId);

        if(users == null){
            throw new ResourceNotFoundException("User","email",emailId);
        }
        users.setFirstName(userDto.getFirstName());
        users.setLastName(userDto.getLastName());
        users.setMobileNumber(userDto.getMobileNumber());

        userRepo.save(users);

        UserDto userDtos=CommonMapper.INSTANCE.toUserDto(users);
        CartDTO cartDTO=CommonMapper.INSTANCE.toCartDTO(users.getCart());
        List<ProductDto> productDto=users.getCart().getCartItems().stream()
                .map(item -> CommonMapper.INSTANCE.toProductDto(item.getProduct())).toList();

        Set<RoleDTO> roleDTOS = users.getRoles().stream()
                .map(CommonMapper.INSTANCE::toRoleDTO).collect(Collectors.toSet());

        List<AddressDTO> addressDTOS = users.getAddresses().stream().map(
                address -> CommonMapper.INSTANCE.toAdressDTO(address)
        ).toList();

        userDto.setCartDTO(cartDTO);
        userDto.getCartDTO().setProducts(productDto);
        userDto.setAddress(addressDTOS);
        userDto.setRoles(roleDTOS);


        return userDto;
    }

    public String deleteUser(UUID userId) {
        User user=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User","UserId",userId));
        List<CartItem> cartItems=user.getCart().getCartItems();
        UUID cartId=user.getCart().getCartId();
        cartItems.forEach(cartItem->{
            UUID productId=cartItem.getProduct().getProductId();
            cartService.deleteProductFromCartUsingCartId(cartId,productId);
        });

        userRepo.delete(user);

        return "User Deleted Successful";
    }
}
