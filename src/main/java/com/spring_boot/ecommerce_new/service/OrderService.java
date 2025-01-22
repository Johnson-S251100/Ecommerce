package com.spring_boot.ecommerce_new.service;


import com.spring_boot.ecommerce_new.exceptions.ApiException;
import com.spring_boot.ecommerce_new.exceptions.ResourceNotFoundException;
import com.spring_boot.ecommerce_new.model.*;
import com.spring_boot.ecommerce_new.payload.CommonMapper;
import com.spring_boot.ecommerce_new.payload.OrderDTO;
import com.spring_boot.ecommerce_new.payload.OrderResponse;
import com.spring_boot.ecommerce_new.payload.UserResponse;
import com.spring_boot.ecommerce_new.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductRepo productRepo;

    public OrderDTO orderProducts(String emailId, String paymentMethod) {

        User user=userRepo.findByEmail(emailId);

        if(user==null){
            throw new ResourceNotFoundException("user","email",emailId);
        }
        Cart cart=cartRepo.findByUserUserId(user.getUserId());
        Order order=new Order();
        order.setEmail(user.getEmail());
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted");

        Payment payment=new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);

        payment=paymentRepo.save(payment);
        order.setPayment(payment);

      Order savedOrders=orderRepo.save(order);

        List<CartItem> cartItems=cart.getCartItems();

        if(cartItems.isEmpty()){
            throw new ApiException("Cart is Empty");
        }

        List<OrderItem> orderItems=new ArrayList<>();

        for(CartItem cartItem:cartItems){
            OrderItem orderItem=new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrices(cartItem.getSpecialPrice());
            orderItem.setOrder(savedOrders);
            orderItems.add(orderItem);
        }
        orderItems=orderItemRepo.saveAll(orderItems);
        cart.getCartItems().forEach(item-> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            cartService.deleteProductFromCartUsingCartId(cart.getCartId(), item.getProduct().getProductId());
            product.setQuantity(product.getQuantity() - quantity);
            productRepo.save(product);
        });
        OrderDTO orderDTO= CommonMapper.INSTANCE.toOrderDTO(order);
        orderItems.forEach(item->{
            orderDTO.getOrderItemDTOS().add(CommonMapper.INSTANCE.toOrderItemDTO(item));
        });

        return orderDTO;
    }

    public OrderResponse getAllOrders(int pageNumber, int pageSize) {

        PageRequest orderResponse= PageRequest.of(pageNumber,pageSize);
        Page<Order> pagedOrders=orderRepo.findAll(orderResponse);
        List<Order> orders=pagedOrders.getContent();
        List<OrderDTO> orderDTOS=orders.stream().map(CommonMapper.INSTANCE::toOrderDTO).toList();
        if(orderDTOS.isEmpty()){
            throw  new ApiException("No Orders placed yet");
        }
        OrderResponse orderResponses=new OrderResponse();
        orderResponses.setContent(orderDTOS);
        orderResponses.setPageNumber(pagedOrders.getNumber());
        orderResponses.setPageSize(pagedOrders.getSize());
        return orderResponses;
    }

    public List<OrderDTO> getOrdersByUser(String emailId) {

        List<Order> orders=orderRepo.findByEmail(emailId);
        List<OrderDTO> orderDTOS=orders.stream().map(CommonMapper.INSTANCE::toOrderDTO).toList();
        if(orderDTOS.isEmpty()){
            throw new ApiException("No Order placed yet for the user "+emailId);
        }
        return orderDTOS;
    }

    public OrderDTO updateOrders(String emailId, UUID orderId, String orderStatus) {

        Order order=orderRepo.findByEmailAndOrderId(emailId,orderId);

        if (order == null) {

            throw new ResourceNotFoundException("Order","orderId ",orderId);
        }
        order.setOrderStatus(orderStatus);
        orderRepo.save(order);
        return CommonMapper.INSTANCE.toOrderDTO(order);
    }
}
