package com.wad.firstmvc.services;

import com.wad.firstmvc.domain.Product;
import com.wad.firstmvc.domain.ProductSearchCriteria;
import com.wad.firstmvc.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> searchProducts(ProductSearchCriteria criteria) {
        // In production, you might use a custom query instead of filtering in memory.
        return productRepository.findAll().stream().filter(product -> {
            boolean matches = true;
            if (criteria.getCategory() != null && !criteria.getCategory().isEmpty()) {
                matches &= product.getCategory() != null &&
                        product.getCategory().equalsIgnoreCase(criteria.getCategory());
            }
            if (criteria.getMinPrice() != null) {
                matches &= product.getPrice() != null &&
                        product.getPrice() >= criteria.getMinPrice();
            }
            if (criteria.getMaxPrice() != null) {
                matches &= product.getPrice() != null &&
                        product.getPrice() <= criteria.getMaxPrice();
            }
            return matches;
        }).collect(Collectors.toList());
    }
}
