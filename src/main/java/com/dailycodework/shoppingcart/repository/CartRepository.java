package com.dailycodework.shoppingcart.repository;


import com.dailycodework.shoppingcart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}