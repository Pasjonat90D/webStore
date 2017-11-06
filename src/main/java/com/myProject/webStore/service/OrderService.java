package com.myProject.webStore.service;

import com.myProject.webStore.domain.Order;

public interface OrderService {
    void processOrder(String productId, int count);
    Long saveOrder(Order order);
}
