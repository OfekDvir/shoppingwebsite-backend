package com.shoppingwebsite.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shoppingwebsite.exceptions.NotFoundException;
import com.shoppingwebsite.model.Product;
import com.shoppingwebsite.repository.ProductRepository;

import jakarta.persistence.Id;
import jakarta.persistence.criteria.Path;
import jakarta.validation.Valid;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // public ResponseEntity<Product> createProduct(Product product) {
    // Product savedProduct = productRepository.save(product);
    // return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    // }

    // public Product createProduct(Product product) {
    // Product savedProduct = productRepository.save(product);
    // System.out.println("Saved Product: " + savedProduct.getProductName());
    // return savedProduct;
    // }

    public Product createProduct(Product product) {
        try {
            Product savedProduct = productRepository.save(product);
            System.out.println("Saved Product: " + savedProduct.getProductName());
            return savedProduct;
        } catch (Exception e) {
            System.err.println("Error saving product: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;

    }

    public Product getProductById(Integer id) throws NotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));
    }

    // public ResponseEntity<Product> getProductById(Integer productId) {
    // Optional<Product> productData = productRepository.findById(productId);
    // return productData.map(product -> new ResponseEntity<>(product,
    // HttpStatus.OK))
    // .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    // }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    // public ResponseEntity<List<Product>> getProductsByCategory(String category) {
    // List<Product> products = productRepository.findByCategory(category);
    // return new ResponseEntity<>(products, HttpStatus.OK);
    // }

    public void updateProduct(Integer productId, Product productDetails) throws NotFoundException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));

        existingProduct.setProductName(productDetails.getProductName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setProductPrice(productDetails.getProductPrice());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setProductImage(productDetails.getProductImage());

        productRepository.save(existingProduct);
    }

    // public ResponseEntity<Product> updateProduct(Integer productId, Product
    // productDetails) {
    // Optional<Product> productData = productRepository.findById(productId);
    // if (productData.isPresent()) {
    // Product product = productData.get();
    // product.setProductName(productDetails.getProductName());
    // product.setDescription(productDetails.getDescription());
    // product.setProductPrice(productDetails.getProductPrice());
    // product.setCategory(productDetails.getCategory());
    // product.setProductImage(productDetails.getProductImage());
    // return new ResponseEntity<>(productRepository.save(product), HttpStatus.OK);
    // } else {
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }
    // }

    private void updateProductDetails(Product existingProduct, Product productDetails) {
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setManufacturer(productDetails.getManufacturer());
        existingProduct.setProductName(productDetails.getProductName());
        existingProduct.setProductPrice(productDetails.getProductPrice());
        existingProduct.setUnitStock(productDetails.getUnitStock());
    }

    public void deleteProduct(Integer id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));

        deleteProductImage(product.getProductImage());
        productRepository.deleteById(id);
    }

    private void deleteProductImage(String imagePath) {
        if (imagePath != null) {
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public Product uploadProductImage(Integer id, MultipartFile file) throws NotFoundException, IOException {
        Optional<Product> productData = productRepository.findById(id);
        if (productData.isPresent()) {
            Product product = productData.get();

            String filePath = "uploads/" + file.getOriginalFilename();
            Files.write(Paths.get(filePath), file.getBytes());
            product.setProductImage(filePath);
            productRepository.save(product);
            return product;

        } else {
            throw new NotFoundException("product id not found");
        }
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getUnitStock() < 5)
                .collect(Collectors.toList());
    }
}
