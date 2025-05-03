package com.shoppingwebsite.services;

import com.shoppingwebsite.model.FavoriteList;
import com.shoppingwebsite.model.Product;
import com.shoppingwebsite.model.User;
import com.shoppingwebsite.repository.FavoriteListRepository;
import com.shoppingwebsite.repository.ProductRepository;
import com.shoppingwebsite.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class FavoriteListService {

    @Autowired
    private FavoriteListRepository favoriteListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<FavoriteList> getFavoriteListByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<FavoriteList> favoriteList = favoriteListRepository.findByUser(user.get());
        return favoriteList.map(list -> new ResponseEntity<>(list, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<FavoriteList> addProductToFavoriteList(Integer userId, Integer productId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Product> product = productRepository.findById(productId);

        if (user.isEmpty() || product.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FavoriteList favoriteList = favoriteListRepository.findByUser(user.get())
                .orElseGet(() -> {
                    FavoriteList newList = new FavoriteList(user.get());
                    newList.setProducts(new HashSet<>());
                    return favoriteListRepository.save(newList);
                });

        if (favoriteList.getProducts() == null) {
            favoriteList.setProducts(new HashSet<>());
        }

        if (!favoriteList.getProducts().contains(product.get())) {
            favoriteList.getProducts().add(product.get());
        }

        FavoriteList updatedList = favoriteListRepository.save(favoriteList);
        return new ResponseEntity<>(updatedList, HttpStatus.OK);
    }

    public ResponseEntity<FavoriteList> removeProductFromFavoriteList(Integer userId, Integer productId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Product> product = productRepository.findById(productId);

        if (user.isEmpty() || product.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<FavoriteList> favoriteListOptional = favoriteListRepository.findByUser(user.get());
        if (favoriteListOptional.isPresent()) {
            FavoriteList favoriteList = favoriteListOptional.get();

            if (favoriteList.getProducts() != null) {
                favoriteList.getProducts().remove(product.get());
            }

            FavoriteList updatedList = favoriteListRepository.save(favoriteList);
            return new ResponseEntity<>(updatedList, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

// public class FavoriteListService {
