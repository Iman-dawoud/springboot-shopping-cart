package com.dailycodework.shoppingcart.service.product;

import com.dailycodework.shoppingcart.dto.ImageDto;
import com.dailycodework.shoppingcart.dto.ProductDto;
import com.dailycodework.shoppingcart.exceptions.AlreadyExistsException;
import com.dailycodework.shoppingcart.exceptions.ProductNotFoundException;
import com.dailycodework.shoppingcart.exceptions.ResourceNotFoundException;
import com.dailycodework.shoppingcart.model.Category;
import com.dailycodework.shoppingcart.model.Image;
import com.dailycodework.shoppingcart.model.Product;
import com.dailycodework.shoppingcart.repository.CategoryRepository;
import com.dailycodework.shoppingcart.repository.ImageRepository;
import com.dailycodework.shoppingcart.repository.ProductRepository;
import com.dailycodework.shoppingcart.request.AddProductRequest;
import com.dailycodework.shoppingcart.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ProductService implements IProductService{
    private final ProductRepository productRepository;//it must be final to be injected
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        // Check if the category is found in the DB
        // if Yes, set it as a new product category
        // if No, then save it as a new category
        // Then set it as a new product category

        if(productExists(request.getName() , request.getBrand())){
            throw new AlreadyExistsException(request.getBrand() +" "+ request.getName()+ " already exists. you may update this product instead");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                        }
                );
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));

    }


    //helper method
    private boolean productExists(String name , String brand){
        return productRepository.existsByNameAndBrand(name , brand);

    }
    //helper method
    private Product createProduct(AddProductRequest request , Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }
    @Override
    public Product getProductById(Long id) {
        //return productRepository.findById(id).orElse(() -> new ProductNotFoundException("Product not found!")); //Target type of a lambda conversion must be an interface
        return productRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete ,
                        () -> {throw new ResourceNotFoundException("Product not found!");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    //helper method
    public Product updateExistingProduct(Product existingProduct , ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {

        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {

        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {

        return productRepository.countByBrandAndName(brand, name);
    }


    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product , ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
