package com.dailycodework.shoppingcart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) //orphanRemoval = true: When a CartItem is removed from the items set, Hibernate automatically deletes it from the database.
    //cascade = CascadeType.ALL: This means that when we delete or persist a Cart, all its associated CartItems will be affected.
    private Set<CartItem> items = new HashSet<>();



    @OneToOne
    @JoinColumn(name ="user_id")
    private User user;

    public void addItem(CartItem item){
        this.items.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItem item){
        this.items.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount(){
        this.totalAmount = items.stream().map(item -> {
            BigDecimal unitPrice = item.getUnitPrice();
            if(unitPrice == null){
                return BigDecimal.ZERO;
            }
            return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}


















