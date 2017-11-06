package com.myProject.webStore.controller;

import com.myProject.webStore.domain.Product;
import com.myProject.webStore.exception.NoProductsFoundUnderCategoryException;
import com.myProject.webStore.exception.ProductNotFoundException;
import com.myProject.webStore.service.ProductService;
import com.myProject.webStore.validator.ProductValidator;
import com.myProject.webStore.validator.UnitsInStockValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.util.*;

@RequestMapping("/products")
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UnitsInStockValidator unitsInStockValidator;




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
        List<Product> products = productService.getProductsByCategory(productCategory);
        if (products == null || products.isEmpty()) {
            throw new NoProductsFoundUnderCategoryException();
        }
        model.addAttribute("products", products);
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
    public String processAddNewProductForm(@ModelAttribute("newProduct")@Valid Product productToBeAdded, BindingResult result, HttpServletRequest request) {
        if(result.hasErrors()) {
            return "addProduct";
        }
        String[] suppressedFields = result.getSuppressedFields();
        if (suppressedFields.length > 0) {
            throw new RuntimeException("Próba wiązania niedozwolonych " +
                    "pól:" + StringUtils.arrayToCommaDelimitedString(suppressedFields));
        }
        MultipartFile productImage = productToBeAdded.getProductImage();
        String rootDirectory = request.getSession().getServletContext().getRealPath("");
        int pos = rootDirectory.lastIndexOf("\\");
        String pathWithoutLastDirectory = rootDirectory.substring(0 , pos-6);
        if (productImage!=null && !productImage.isEmpty()) {
            try {
                File file = new File(pathWithoutLastDirectory+"resources\\static\\images\\"+ productToBeAdded.getProductId() + ".jpeg");
                productImage.transferTo(file);
            } catch (Exception e) {
                throw new RuntimeException("Niepowodzenie podczas próby zapisu obrazka produktu", e);
            }
        }
        MultipartFile productPdf = productToBeAdded.getProductPdf();
        if(productPdf!=null && !productPdf.isEmpty()){
            try {
                File filePdf = new File(pathWithoutLastDirectory+"resources\\static\\pdf\\"+ productToBeAdded.getProductId() + ".pdf");
                productPdf.transferTo(filePdf);
            } catch (Exception e) {
                throw new RuntimeException("Niepowodzenie podczas próby zapisu pdf produktu", e);
            }
        }

        productService.addProduct(productToBeAdded);
        return "redirect:/products";
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public ModelAndView handleError(HttpServletRequest req, ProductNotFoundException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("invalidProductId", exception.getProductId());
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL()+"?"+req.getQueryString());
        mav.setViewName("productNotFound");
        return mav;
    }

    @RequestMapping("/invalidPromoCode")
    public String invalidPromoCode() {
        return "invalidPromoCode";
    }

    @InitBinder
    public void initialiseBinder(WebDataBinder binder) {
        binder.setAllowedFields("productId", "name", "unitPrice",
                "description","manufacturer", "category", "unitsInStock",
                "productImage" , "productPdf", "language");
        binder.setDisallowedFields("unitsInOrder", "discontinued");
        binder.addValidators(unitsInStockValidator);
    }
}
