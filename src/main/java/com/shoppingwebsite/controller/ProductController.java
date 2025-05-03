package com.shoppingwebsite.controller;

import com.shoppingwebsite.exceptions.NotFoundException;
import com.shoppingwebsite.model.Product;
import com.shoppingwebsite.repository.ProductRepository;
import com.shoppingwebsite.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    private static final String UPLOAD_DIR = "uploads/";

    //// בדיקה הוספתי
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // ← כאן אנחנו מנקים רווחים מהשם
            String fileName = file.getOriginalFilename().replaceAll("\\s+", "_");

            Path uploadPath = Paths.get("uploads/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("/uploads/" + fileName);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    @PostMapping
    public ResponseEntity<Product> createsavedProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);

        if (savedProduct != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    // Get a product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Integer productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok().body(product);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

    // Get products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("category") String category) {
        List<Product> productList = productService.getProductsByCategory(category);
        return ResponseEntity.ok().body(productList);
    }

    // Update an existing product
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("productId") Integer productId,
            @Valid @RequestBody Product productDetails) {
        try {
            productService.updateProduct(productId, productDetails);
            return ResponseEntity.ok().body(productDetails);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();

        }

    }

    // Delete all products
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.ok().build();
    }

    // Upload product image
    @PostMapping("/{id}/image")
    public ResponseEntity<Product> uploadProductImage(
            @PathVariable("id") Integer id,
            @RequestParam("file") MultipartFile file) {
        try {
            Product p = productService.uploadProductImage(id, file);
            return new ResponseEntity<>(p, HttpStatus.OK);

        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get all products with low stock (less than 5 units)
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        List<Product> lowStock = productService.getLowStockProducts();
        return ResponseEntity.ok(lowStock);
    }
}
