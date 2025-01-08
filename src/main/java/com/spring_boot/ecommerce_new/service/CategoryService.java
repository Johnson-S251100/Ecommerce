package com.spring_boot.ecommerce_new.service;

import com.spring_boot.ecommerce_new.exceptions.ApiException;
import com.spring_boot.ecommerce_new.exceptions.ResourceNotFoundException;
import com.spring_boot.ecommerce_new.model.Category;
import com.spring_boot.ecommerce_new.model.Product;
import com.spring_boot.ecommerce_new.payload.CategoryDto;
import com.spring_boot.ecommerce_new.payload.CategoryResponse;
import com.spring_boot.ecommerce_new.payload.CommonMapper;
import com.spring_boot.ecommerce_new.repository.CategoryRepo;
import com.spring_boot.ecommerce_new.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryrepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    public CategoryDto addNewCategory(Category category) {

        Category savedCategory= categoryrepo.findByCategoryName(category.getCategoryName());
        if(savedCategory!=null){
            throw new ApiException("Category name is already present"+ category.getCategoryName());
        }
        savedCategory=categoryrepo.save(category);
        System.out.println(savedCategory.getCategoryId());
        return CommonMapper.INSTANCE.toCategoryDto(savedCategory);
    }

    public CategoryResponse getCategory(int pageNum, int pageSize) {
        //Pagination
        Pageable categoryResponse= PageRequest.of(pageNum, pageSize);
        Page<Category> pagedCategories=categoryrepo.findAll(categoryResponse);
        List<Category> categories=pagedCategories.getContent();
        //Conversion Category to CategoryDTO
        List<CategoryDto> categoryDto= categories.stream().map(CommonMapper.INSTANCE::toCategoryDto).toList();
        CategoryResponse categoryresponse= new CategoryResponse();
        categoryresponse.setContent(categoryDto);
        categoryresponse.setPageNum(pagedCategories.getNumber());
        categoryresponse.setPageSize(pagedCategories.getSize());
        return categoryresponse;
    }

    public  CategoryDto updateCategoryDetails(Category category, UUID categoryId) {
    Category Categories=categoryrepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
    Categories.setCategoryId(categoryId);
    Categories=categoryrepo.save(category);
    return CommonMapper.INSTANCE.toCategoryDto(Categories);
    }

    public String deleteCategoryById(UUID categoryId) {
        Category savedCategory=categoryrepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
        List<Product> productList=savedCategory.getProducts();
        for(Product p:productList){
            productService.removeProduct(p.getProductId());
        }
        categoryrepo.delete(savedCategory);
        return "CategoryId is Deleted Successfully "+ categoryId;

    }
}
