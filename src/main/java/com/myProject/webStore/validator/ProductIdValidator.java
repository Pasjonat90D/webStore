package com.myProject.webStore.validator;

import com.myProject.webStore.domain.Product;
import com.myProject.webStore.exception.ProductNotFoundException;
import com.myProject.webStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductIdValidator implements ConstraintValidator<ProductId, String> {

    @Autowired
    private ProductService productService;

    @Override
    public void initialize(ProductId constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Product product;
        try {
            product = productService.getProductById(value);
        } catch (ProductNotFoundException e) {
            return true;
        }
        if (product != null) {
            return false;
        }
        return true;
    }


}

