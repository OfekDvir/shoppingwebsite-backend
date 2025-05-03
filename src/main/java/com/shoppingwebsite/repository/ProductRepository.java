package com.shoppingwebsite.repository;

import com.shoppingwebsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Custom query method to find products by category
    List<Product> findByCategory(String category);

    // Add more custom query methods as needed
    List<Product> findByManufacturer(String manufacturer);

    List<Product> findByProductPriceLessThan(double maxPrice);

    List<Product> findByProductPriceGreaterThan(double minPrice);

    List<Product> findByProductNameContaining(String keyword);

}