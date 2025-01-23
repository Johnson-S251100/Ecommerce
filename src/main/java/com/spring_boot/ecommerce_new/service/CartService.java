package com.spring_boot.ecommerce_new.service;

import com.spring_boot.ecommerce_new.exceptions.ApiException;
import com.spring_boot.ecommerce_new.exceptions.ResourceNotFoundException;
import com.spring_boot.ecommerce_new.model.Cart;
import com.spring_boot.ecommerce_new.model.CartItem;
import com.spring_boot.ecommerce_new.model.Product;
import com.spring_boot.ecommerce_new.model.User;
import com.spring_boot.ecommerce_new.payload.CartDTO;
import com.spring_boot.ecommerce_new.payload.CommonMapper;
import com.spring_boot.ecommerce_new.payload.ProductDto;
import com.spring_boot.ecommerce_new.repository.CartItemRepo;
import com.spring_boot.ecommerce_new.repository.CartRepo;
import com.spring_boot.ecommerce_new.repository.ProductRepo;
import com.spring_boot.ecommerce_new.repository.UserRepo;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CartItemRepo cartItemRepo;

    public CartDTO addProductToCart(String userEmail, UUID productId, int quantity) {

        User user=userRepo.findByEmail(userEmail);

        if(user==null){
            throw new ResourceNotFoundException("User","email",userEmail);
        }
        Cart cart=cartRepo.findByUserUserId(user.getUserId());

        Product product=productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        CartItem cartItem=cartItemRepo.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
        if(cartItem!=null){
            throw new ApiException("Product"+product.getProductName()+"already exists in the cart");
        }
        if(product.getQuantity()==0){
            throw new ApiException(product.getProductName()+"is not available");
        }
        if(product.getQuantity()<quantity){
            throw new ApiException("Please make an order"+product.getProductName()+"less than or equal to the quantity"+product.getQuantity());
        }
        CartItem newCartItem=new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setSpecialPrice(product.getSpecialPrice());

        cartItemRepo.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice()+(newCartItem.getSpecialPrice()*quantity));
        productRepo.save(product);
        cartRepo.save(cart);
        CartDTO cartDTO= CommonMapper.INSTANCE.toCartDTO(cart);

        List<ProductDto> productDtos=cart.getCartItems().stream()
                .map(a->CommonMapper.INSTANCE.toProductDto(a.getProduct())).toList();
    cartDTO.setProducts(productDtos);
    return cartDTO;
    }

    public List<CartDTO> getCarts() {

        List<Cart> carts=cartRepo.findAll();
        if(carts.isEmpty()){
            throw new ApiException("No carts Avaialbe/Exists");
        }
        List<CartDTO> cartDTOS=carts.stream().map(cart-> {
            CartDTO cartDTO=CommonMapper.INSTANCE.toCartDTO(cart);

            List<ProductDto> productDtos=cart.getCartItems().stream()
                            .map(products->CommonMapper.INSTANCE.toProductDto(products.getProduct())).toList();
            cartDTO.setProducts(productDtos);
            return  cartDTO;

        }).toList();
        return cartDTOS;
    }

    public CartDTO getCartById(String emailId) {

        User user=userRepo.findByEmail(emailId);
        if(user==null){
            throw new ResourceNotFoundException("user","email",emailId);
        }
        Cart cart=cartRepo.findCartByEmailAndCartId(emailId,user.getCart().getCartId());

        CartDTO cartDTO=CommonMapper.INSTANCE.toCartDTO(cart);

        List<ProductDto> productDtos=cart.getCartItems().stream()
                .map(prod->CommonMapper.INSTANCE.toProductDto(prod.getProduct())).toList();

        cartDTO.setProducts(productDtos);
        return cartDTO;
    }

    public CartDTO updateProductQuantity(String emailId, UUID productId, int quantities) {

        User user=userRepo.findByEmail(emailId);
        if(user==null){
            throw new ResourceNotFoundException("user","email",emailId);
        }
        Cart cart=cartRepo.findById(user.getCart().getCartId()).orElseThrow(()->new ResourceNotFoundException("user", "cart",user.getCart().getCartId()));

        Product product=productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("product","productId",productId));

        if(product.getQuantity()==0){
            throw new ApiException(product.getProductName() +"is not available");
        }
        if(product.getQuantity()<quantities){
            throw  new ApiException("Please make an order "+product.getProductName()+
                    " is less than or equal to the Quantity"+ product.getQuantity());
        }

        CartItem cartItem=cartItemRepo.findCartItemByProductIdAndCartId(cart.getCartId(),productId);

        if(cartItem== null){
            throw new ApiException("product "+product.getProductName()+" is not available in the cart");
        }

        int cartItemQuantity=cartItem.getQuantity()+quantities;
        if(product.getQuantity()<= cartItemQuantity){
            throw new ApiException("You have reached your Limit");
        }
        double cartPrice=cart.getTotalPrice()-(cartItem.getSpecialPrice()*cartItem.getQuantity());

        cartItem.setSpecialPrice(product.getSpecialPrice());
        cartItem.setQuantity(cartItemQuantity);
        cartItem.setDiscount(product.getDiscount());

        cart.setTotalPrice(cartPrice+(cartItem.getSpecialPrice()*cartItem.getQuantity()));
        cartItem= cartItemRepo.save(cartItem);
        productRepo.save(product);
        CartDTO cartDTO= CommonMapper.INSTANCE.toCartDTO(cart);
        List<ProductDto> productDtos=cart.getCartItems().stream().map(
                prod->CommonMapper.INSTANCE.toProductDto(prod.getProduct())
        ).toList();
        cartDTO.setProducts(productDtos);
        return cartDTO;
    }

    public String deleteProductFromCart(String emailId, UUID productId) {

        User user=userRepo.findByEmail(emailId);

        if(user ==null){
            throw new ResourceNotFoundException("User","emailId",emailId);
        }
        Cart cart=cartRepo.findCartByEmailAndCartId(emailId,user.getCart().getCartId());
        CartItem cartItem=cartItemRepo.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
        cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getSpecialPrice()*cartItem.getQuantity()));
        Product product=cartItem.getProduct();
        cartItemRepo.deleteCartItemByProductIdAndCartId(cart.getCartId(),productId);
        return "product" +cartItem.getProduct().getProductName() +" deleted Successfully";


    }

    public String deleteProductFromCartUsingCartId(UUID cartId, UUID productId) {

        Cart cart=cartRepo.findById(cartId).orElseThrow(()->new ResourceNotFoundException("cart","cartid",cartId));
        CartItem cartItem=cartItemRepo.findCartItemByProductIdAndCartId(cartId,productId);
        cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getSpecialPrice()*cartItem.getQuantity()));
        Product product=cartItem.getProduct();
        cartItemRepo.deleteCartItemByProductIdAndCartId(cart.getCartId(),productId);
        return "Product" + cartItem.getProduct().getProductName()+ " deleted Successfully";
    }
}
