package com.dailycodework.shoppingcart.service.cart;

import com.dailycodework.shoppingcart.exceptions.ResourceNotFoundException;
import com.dailycodework.shoppingcart.model.Cart;
import com.dailycodework.shoppingcart.model.User;
import com.dailycodework.shoppingcart.repository.CartItemRepository;
import com.dailycodework.shoppingcart.repository.CartRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }



    @Transactional // because we are doing a lot of operations we're getting the cart, and we are doing massive delete , so we need to make everything a single unit of transaction if one fails everything fails.
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }


    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }



    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}