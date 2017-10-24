package com.myProject.webStore.service.impl;

import com.myProject.webStore.domain.Product;
import com.myProject.webStore.domain.repository.ProductRepository;
import com.myProject.webStore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSeriveImpl implements OrderService {

    @Autowired
    private ProductRepository productRepository;

    public void processOrder(String productId, int count) {
        Product productById = productRepository.getProductById(productId);
        if (productById.getUnitsInStock() < count) {
            throw new IllegalArgumentException("Zbyt mało towaru." +
                    " Obecna liczba sztuk w magazynie: " + productById.getUnitsInStock());
        }
        productById.setUnitsInStock(productById.getUnitsInStock() - count);
    }

}
