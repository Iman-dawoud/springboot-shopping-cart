package com.dailycodework.dreamshops.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
//we use @Getter and @Setter instead of @Data annotation because it is not safe to use it on a class that is an entity in the database
@AllArgsConstructor
//@Data
//@NoArgsConstructor
@Entity


//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // ✅ Prevents infinite recursion
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory; //quantity
    private String description;

    //  @JsonIgnore  // This completely excludes the `category` field from the JSON response.
    //@JsonBackReference // This marks the child side and prevents infinite recursion when serializing.
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL , orphanRemoval = true) //when a product is being deleted all the images that are associated with that product are going to be deleted along
    private List<Image> images;

    public Product(){}

    public Product(String name, String brand , BigDecimal price , int inventory , String description , Category category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.inventory = inventory;
        this.description = description;
        this.category = category;
    }


}
