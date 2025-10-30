package com.wad.firstmvc.controllers;

import com.wad.firstmvc.domain.Product;
import com.wad.firstmvc.domain.ProductSearchCriteria;
import com.wad.firstmvc.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // List all products
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products"; // corresponds to products.html
    }

    // Show form for a new product
    @GetMapping("/new")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "productForm"; // corresponds to productForm.html
    }

    // Save new product
    @PostMapping
    public String saveProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/products";
    }

    // Display search form
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("searchCriteria", new ProductSearchCriteria());
        return "productSearchForm"; // corresponds to productSearchForm.html
    }

    // Process search form submission
    @PostMapping("/search")
    public String searchProducts(@ModelAttribute ProductSearchCriteria criteria, Model model) {
        model.addAttribute("products", productService.searchProducts(criteria));
        return "products"; // reuse products.html to display search results
    }
}
