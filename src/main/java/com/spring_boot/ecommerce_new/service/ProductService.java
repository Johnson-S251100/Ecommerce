package com.spring_boot.ecommerce_new.service;

import com.spring_boot.ecommerce_new.exceptions.ApiException;
import com.spring_boot.ecommerce_new.exceptions.ResourceNotFoundException;
import com.spring_boot.ecommerce_new.model.Category;
import com.spring_boot.ecommerce_new.model.Product;
import com.spring_boot.ecommerce_new.payload.CommonMapper;
import com.spring_boot.ecommerce_new.payload.ProductDto;
import com.spring_boot.ecommerce_new.payload.ProductResponse;
import com.spring_boot.ecommerce_new.repository.CategoryRepo;
import com.spring_boot.ecommerce_new.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    public ProductDto addProducts(UUID categoryId, Product product) {
        Category category=categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
        boolean isNewProduct=true;
        List<Product> products=category.getProducts();
        for(int i=0;i<products.size();i++) {
            if (products.get(i).getProductName().equals(product.getProductName()) &&
                    products.get(i).getProductDescription().equals(product.getProductDescription())) {
                isNewProduct = false;
                break;
            }
        }
        if(isNewProduct){
            product.setCategory(category);
            double specialPrice= product.getPrice()-((product.getDiscount() *0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product saveProducts= productRepo.save(product);
            return CommonMapper.INSTANCE.toProductDto(saveProducts);
        }
        else {
            throw new ApiException("Product Already Exists..........");
        }

    }

    public ProductResponse getAllProducts(int pageNum, int pageSize) {

        //Pagination
        Pageable pageDetail= PageRequest.of(pageNum,pageSize);
        Page<Product> pageProducts=productRepo.findAll(pageDetail);

        //Get the products from Page products

        List<Product> products=pageProducts.getContent();

        //Converting the products to ProductDto
        List<ProductDto> productDtos=products.stream().map(CommonMapper.INSTANCE::toProductDto).toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDtos);
        productResponse.setPageNum(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        return productResponse;
    }

    public ProductDto updateTheProduct(UUID productId, Product product) {

        Product savedProducts=productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","ProductId",productId));
        product.setProductId(productId);
        product.setCategory(savedProducts.getCategory());
        double specialPrice=product.getPrice()-((product.getDiscount()*0.01)*product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product finalProducts=productRepo.save(product);
        return CommonMapper.INSTANCE.toProductDto(finalProducts);

    }

    public ProductResponse getByKeyWords(String keywords,int pageNum,int pageSize) {
        //Pagination
        Pageable pageRequest=PageRequest.of(pageNum,pageSize);

        Page<Product> products=productRepo.findByProductNameContainingOrProductDescriptionContaining(keywords,keywords,pageRequest);

        List<Product> productList=products.getContent();
        //Conversion of Product to productDTO
        List<ProductDto> productDtoList=productList.stream().map(CommonMapper.INSTANCE::toProductDto).toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDtoList);
        productResponse.setPageSize(products.getSize());
        productResponse.setPageNum(products.getNumber());

        return productResponse;
    }

    public ProductResponse usingByCategoryId(UUID categoryId,int pageNum,int pageSize) {
        Category category=categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));
        Pageable pageable=PageRequest.of(pageNum,pageSize);
        Page<Product> pagedProduct=productRepo.findByCategoryCategoryId(category.getCategoryId(),pageable);
        List<Product> productList=pagedProduct.getContent();
        List<ProductDto> productDtoList=productList.stream().map(CommonMapper.INSTANCE::toProductDto).toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDtoList);
        productResponse.setPageNum(pagedProduct.getNumber());
        productResponse.setPageSize(pagedProduct.getSize());
        return productResponse;
    }

    public String removeProduct(UUID productId) {

        Product product=productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","ProductId",productId));

        productRepo.delete(product);
        return "Product Deleted Successfully";
    }
}
