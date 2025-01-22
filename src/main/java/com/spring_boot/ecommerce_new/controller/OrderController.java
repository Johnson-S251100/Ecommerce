package com.spring_boot.ecommerce_new.controller;

import com.spring_boot.ecommerce_new.model.Order;
import com.spring_boot.ecommerce_new.payload.OrderDTO;
import com.spring_boot.ecommerce_new.payload.OrderResponse;
import com.spring_boot.ecommerce_new.service.JwtService;
import com.spring_boot.ecommerce_new.service.OrderService;
import jakarta.persistence.GeneratedValue;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ecommerce")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/public/users/cart/payment/{paymentMethod}/order")
    public ResponseEntity<OrderDTO> orderProduct(HttpServletRequest request, @PathVariable String paymentMethod){
        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);
        OrderDTO orderDTO=orderService.orderProducts(emailId,paymentMethod);
        return new ResponseEntity<OrderDTO>(orderDTO, HttpStatus.CREATED);
    }

    @GetMapping("/admin/orders/{pageNumber}/{pageSize}")
    public ResponseEntity<OrderResponse> getAllOrders(@PathVariable int pageNumber,@PathVariable int pageSize){
        OrderResponse orderResponse=orderService.getAllOrders(pageNumber,pageSize);
        return new ResponseEntity<OrderResponse>(orderResponse,HttpStatus.OK);
    }
    @GetMapping("/public/users/orders")
    public ResponseEntity<List<OrderDTO>> getOrderByUsers(HttpServletRequest request){
        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);
        List<OrderDTO> orderDTOS=orderService.getOrdersByUser(emailId);
        return new ResponseEntity<List<OrderDTO>>(orderDTOS,HttpStatus.OK);
    }
    @PutMapping("/public/users/orders/{orderId}/orderStatus/{orderStatus}")
    public ResponseEntity<OrderDTO> updateOrder(HttpServletRequest request, @PathVariable UUID orderId, @PathVariable String orderStatus){
        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);
        OrderDTO orderDTO=orderService.updateOrders(emailId,orderId,orderStatus);
        return new ResponseEntity<OrderDTO>(orderDTO,HttpStatus.OK);
    }





}
