package com.wad.firstmvc.repositories;

import com.wad.firstmvc.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Optional custom query methods
}
