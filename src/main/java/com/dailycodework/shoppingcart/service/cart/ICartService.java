package com.dailycodework.shoppingcart.service.cart;

import com.dailycodework.shoppingcart.model.Cart;
import com.dailycodework.shoppingcart.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
