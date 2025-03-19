package com.dailycodework.dreamshops.request;

import com.dailycodework.dreamshops.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory; //quantity
    private String description;
    private Category category;


}
