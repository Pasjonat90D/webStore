package com.myProject.webStore.service.impl;

import com.myProject.webStore.domain.Order;
import com.myProject.webStore.domain.Product;
import com.myProject.webStore.domain.repository.OrderRepository;
import com.myProject.webStore.domain.repository.ProductRepository;
import com.myProject.webStore.service.CartService;
import com.myProject.webStore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;

    public void processOrder(String productId, int count) {
        Product productById = productRepository.getProductById(productId);
        if (productById.getUnitsInStock() < count) {
            throw new IllegalArgumentException("Zbyt mało towaru." +
                    " Obecna liczba sztuk w magazynie: " + productById.getUnitsInStock());
        }
        productById.setUnitsInStock(productById.getUnitsInStock() - count);
    }
    public Long saveOrder(Order order) {
        Long orderId = orderRepository.saveOrder(order);
        cartService.delete(order.getCart().getCartId());
        return orderId;
    }

}
