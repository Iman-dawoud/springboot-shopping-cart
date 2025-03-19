package com.dailycodework.shoppingcart.controller;

import com.dailycodework.shoppingcart.exceptions.ResourceNotFoundException;
import com.dailycodework.shoppingcart.model.Category;
import com.dailycodework.shoppingcart.response.ApiResponse;
import com.dailycodework.shoppingcart.service.category.ICategoryService;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@RestController
@RequestMapping("${api.prefix}/category")
public class CategoryController {
    private final ICategoryService categoryService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found!", categories));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category  newCategory){
        try {
            Category theCategory = categoryService.addCategory(newCategory);
            return ResponseEntity.ok(new ApiResponse("Success!", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found!", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found!", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }


    @DeleteMapping("/category/{id}/Delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found!", null));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id , @RequestBody Category category){
        try {
            Category updateCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updateCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }
}
