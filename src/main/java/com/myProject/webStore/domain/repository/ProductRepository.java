package com.myProject.webStore.domain.repository;

import com.myProject.webStore.domain.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> getAllProducts();
    Product getProductById(String productId);
}
