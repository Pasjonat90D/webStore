package com.myProject.webStore.controller;

import com.myProject.webStore.domain.Product;
import com.myProject.webStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@RequestMapping("/products")
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping
    public String list(Model model) {
        model.addAttribute("products",productService.getAllProducts());
        return "products";
    }

    @RequestMapping("/all")
    public String allProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @RequestMapping("/{category}")
    public String getProductsByCategory(Model model, @PathVariable("category")String productCategory) {
        model.addAttribute("products", productService.getProductsByCategory(productCategory));
        return "products";
    }

    @RequestMapping("/filter/{ByCriteria}")
    public String getProductsByFilter(@MatrixVariable(pathVar="ByCriteria") Map<String,List<String>> filterParams, Model model) {
        model.addAttribute("products", productService.getProductsByFilter(filterParams));
        return "products";
    }
    @RequestMapping("/product")
    public String getProductById(@RequestParam("id") String productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        return "product";
    }
    @RequestMapping("/{category}/{byCriteria}")
    public String filterProducts(@PathVariable("category") String category,@MatrixVariable(pathVar = "byCriteria") Map<String,String> filterParams,@RequestParam("manufacturer")String manufacturer, Model model){
        Set<Product> products = new HashSet<>();
        productService.getProductsByPriceFilter(Float.parseFloat(filterParams.get("low")),Float.parseFloat(filterParams.get("high"))).forEach(product -> products.add(product));
        products.containsAll(productService.getProductsByCategory(category));
        products.containsAll(productService.getProductsByManufacturer(manufacturer));
        model.addAttribute("products",products);
        return "products";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAddNewProductForm(Model model) {
        Product newProduct = new Product();
        model.addAttribute("newProduct", newProduct);
        return "addProduct";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddNewProductForm(@ModelAttribute("newProduct") Product productToBeAdded, BindingResult result, HttpServletRequest request) {
        String[] suppressedFields = result.getSuppressedFields();
        if (suppressedFields.length > 0) {
            throw new RuntimeException("Próba wiązania niedozwolonych " +
                    "pól:" + StringUtils.arrayToCommaDelimitedString(suppressedFields));
        }
        MultipartFile productImage = productToBeAdded.getProductImage();
        String rootDirectory = request.getSession().getServletContext().getRealPath("");

        int pos = rootDirectory.lastIndexOf("\\");

        String x2 =rootDirectory.substring(0 , pos-6);

        System.out.println("rootDirectory = "+x2);
        if (productImage!=null && !productImage.isEmpty()) {
            try {
                System.out.println(new File(rootDirectory+"resources\\static\\images\\"+ productToBeAdded.getProductId() + ".jpeg"));
                File file = new File(x2+"resources\\static\\images\\"+ productToBeAdded.getProductId() + ".jpeg");
                // H:\Ostatnie projekty\webStore\src\main\resources\static
                String filePath = "/"+ productToBeAdded.getProductId() + ".jpeg";
                System.out.println(file);
                productImage.transferTo(file);
            } catch (Exception e) {
                throw new RuntimeException("Niepowodzenie podczas próby zapisu obrazka produktu", e);
            }
        }
        productService.addProduct(productToBeAdded);
        return "redirect:/products";
    }

    @InitBinder
    public void initialiseBinder(WebDataBinder binder) {
        binder.setAllowedFields("productId", "name", "unitPrice", "description","manufacturer", "category", "unitsInStock", "productImage");
        binder.setDisallowedFields("unitsInOrder", "discontinued");
    }
}
