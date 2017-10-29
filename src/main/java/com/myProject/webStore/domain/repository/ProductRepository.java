package com.myProject.webStore.domain.repository;

import com.myProject.webStore.domain.Product;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductRepository {

    List<Product> getAllProducts();
    Product getProductById(String productId);
    List<Product> getProductsByCategory(String category);
    Set<Product> getProductsByFilter(Map<String, List<String>> filterParams);
    List <Product> getProductsByManufacturer(String manufacturer);
    Set<Product> getProductsByPriceFilter(float a, float b);
    void addProduct(Product product);
}
