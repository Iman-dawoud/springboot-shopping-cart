package com.dailycodework.shoppingcart.service.order;


import com.dailycodework.shoppingcart.dto.OrderDto;
import com.dailycodework.shoppingcart.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}