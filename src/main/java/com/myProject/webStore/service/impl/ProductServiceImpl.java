package com.myProject.webStore.service.impl;

import com.myProject.webStore.domain.Product;
import com.myProject.webStore.domain.repository.ProductRepository;
import com.myProject.webStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }
}
