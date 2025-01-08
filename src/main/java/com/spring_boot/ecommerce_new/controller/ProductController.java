package com.spring_boot.ecommerce_new.controller;

import com.spring_boot.ecommerce_new.model.Product;
import com.spring_boot.ecommerce_new.payload.ProductDto;
import com.spring_boot.ecommerce_new.payload.ProductResponse;
import com.spring_boot.ecommerce_new.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ecommerce")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/products/add/{categoryId}")
    public ResponseEntity<ProductDto> addProduct(@RequestBody Product product, @PathVariable UUID categoryId){
        ProductDto productDto= productService.addProducts(categoryId,product);
        return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
    }
    @GetMapping("/public/products/{pageNum}/{pageSize}")
    public ResponseEntity<ProductResponse> getAllProduct(@PathVariable int pageNum,@PathVariable int pageSize){
    ProductResponse productResponse= productService.getAllProducts(pageNum,pageSize);
    return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/products/{keywords}/{pageNum}/{pageSize}")
    public ResponseEntity<ProductResponse> searchByKeyword(@PathVariable String keywords,@PathVariable int pageNum,@PathVariable int pageSize) {
        ProductResponse productResponse= productService.getByKeyWords(keywords,pageNum,pageSize);
        return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/category/{categoryId}/products/{pageNum}/{pageSize}")
    public ResponseEntity<ProductResponse> getByCategory(@PathVariable UUID categoryId,@PathVariable int pageNum,@PathVariable int pageSize){
        ProductResponse productResponse=productService.usingByCategoryId(categoryId,pageNum,pageSize);
        return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
    }


    @PutMapping("/admin/products/update/{productId}")
    public  ResponseEntity<ProductDto> updateProduct(@RequestBody Product product,@PathVariable UUID productId){
        ProductDto productDto=productService.updateTheProduct(productId,product);
        return new ResponseEntity<ProductDto>(productDto,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID productId){
        String deleteFinish=productService.removeProduct(productId);
        return new ResponseEntity<String>(deleteFinish,HttpStatus.OK);
    }
}
