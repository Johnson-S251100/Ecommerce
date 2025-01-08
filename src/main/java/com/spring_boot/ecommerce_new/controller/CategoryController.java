package com.spring_boot.ecommerce_new.controller;

import com.spring_boot.ecommerce_new.model.Category;
import com.spring_boot.ecommerce_new.payload.CategoryDto;
import com.spring_boot.ecommerce_new.payload.CategoryResponse;
import com.spring_boot.ecommerce_new.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ecommerce")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody Category category){
        CategoryDto categories=categoryService.addNewCategory(category);
        return new ResponseEntity<CategoryDto>(categories, HttpStatus.CREATED);
    }

    @GetMapping("/public/categories/{pageNum}/{pageSize}")
    public ResponseEntity<CategoryResponse> getCategories(@PathVariable int pageNum, @PathVariable int pageSize){
    CategoryResponse categoryResponse= categoryService.getCategory(pageNum, pageSize);
    return  new ResponseEntity<CategoryResponse>(categoryResponse,HttpStatus.FOUND);
    }
    @PutMapping("/admin/category/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody Category category, @PathVariable UUID categoryId){
        CategoryDto categories= categoryService.updateCategoryDetails(category,categoryId);
                return new ResponseEntity<CategoryDto>(categories,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/admin/category/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID categoryId){

        return new ResponseEntity<String>(categoryService.deleteCategoryById(categoryId),HttpStatus.OK);
    }

}
