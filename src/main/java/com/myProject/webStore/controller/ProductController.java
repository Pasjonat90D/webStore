package com.myProject.webStore.controller;

import com.myProject.webStore.domain.Product;
import com.myProject.webStore.domain.repository.ProductRepository;
import com.myProject.webStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/products")
    public String list(Model model) {
        model.addAttribute("products",productService.getAllProducts());
        return "products";
    }

}
