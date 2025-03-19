package com.dailycodework.shoppingcart.service.category;

import com.dailycodework.shoppingcart.exceptions.AlreadyExistsException;
import com.dailycodework.shoppingcart.exceptions.ResourceNotFoundException;
import com.dailycodework.shoppingcart.model.Category;
import com.dailycodework.shoppingcart.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor

public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String category) {
        return categoryRepository.findByName(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistsException(category.getName()+" already exists"));
    }

    @Override
    public Category updateCategory(Category category , Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete , ()-> {
            throw new ResourceNotFoundException("Category not found!");
        });
    }
}
