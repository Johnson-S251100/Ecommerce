package com.spring_boot.ecommerce_new.controller;

import com.spring_boot.ecommerce_new.payload.CartDTO;
import com.spring_boot.ecommerce_new.service.CartService;
import com.spring_boot.ecommerce_new.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ecommerce")
public class CartController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private CartService cartService;

    @PostMapping("/public/cart/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductsToCart(HttpServletRequest request, @PathVariable UUID productId,@PathVariable int quantity){

        String token= jwtService.extractToken(request);
        String userEmail=jwtService.extractUserName(token);

        CartDTO cartDTO= cartService.addProductToCart(userEmail,productId,quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }
    @GetMapping("/public/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> cartDTOS=cartService.getCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS,HttpStatus.OK);
    }
    @GetMapping("/public/user/carts")
    public ResponseEntity<CartDTO> getCartById(HttpServletRequest request){
        String token =jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);

        CartDTO cartDTO=cartService.getCartById(emailId);

        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }
    @PutMapping("/public/cart/products/{productId}/quantity/{quantities}")
    public ResponseEntity<CartDTO> updateCartProduct(HttpServletRequest request,@PathVariable UUID productId,@PathVariable int quantities){
        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);
        CartDTO cartDTO=cartService.updateProductQuantity(emailId,productId,quantities);
        return  new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("/public/cart/products/{productId}")
    public ResponseEntity<String> deleteCartProduct(HttpServletRequest request,@PathVariable UUID productId){
        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);
        String response= cartService.deleteProductFromCart(emailId,productId);
        return new ResponseEntity<String>(response,HttpStatus.OK);
    }

}
