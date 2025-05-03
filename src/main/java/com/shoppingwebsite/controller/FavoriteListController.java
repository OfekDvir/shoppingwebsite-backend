package com.shoppingwebsite.controller;

import com.shoppingwebsite.model.FavoriteList;
import com.shoppingwebsite.model.Product;
import com.shoppingwebsite.model.User;
import com.shoppingwebsite.services.FavoriteListService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteListController {

    @Autowired
    private FavoriteListService favoriteListService;

    @GetMapping("/{userId}")
    public ResponseEntity<FavoriteList> getFavoriteList(@PathVariable Integer userId) {
        return favoriteListService.getFavoriteListByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<FavoriteList> addProductToFavorite(@RequestBody Map<String, Integer> payload) {
        Integer userId = payload.get("userId");
        Integer productId = payload.get("productId");
        return favoriteListService.addProductToFavoriteList(userId, productId);
    }

    @DeleteMapping
    public ResponseEntity<FavoriteList> removeProductFromFavorite(@RequestBody Map<String, Integer> payload) {
        Integer userId = payload.get("userId");
        Integer productId = payload.get("productId");
        return favoriteListService.removeProductFromFavoriteList(userId, productId);
    }
}

// public class FavoriteListController {

// @Autowired
// private FavoriteListService favoriteListService;

// @GetMapping("/{userId}")
// public ResponseEntity<FavoriteList> getFavoriteList(@PathVariable("userId")
// User user) {
// return favoriteListService.getFavoriteListByUser(user);
// }

// @PostMapping("/{userId}/add/{productId}")
// public ResponseEntity<FavoriteList>
// addProductToFavorite(@PathVariable("userId") User user,
// @PathVariable("productId") Product product) {
// return favoriteListService.addProductToFavoriteList(user, product);
// }

// @DeleteMapping("/{userId}/remove/{productId}")
// public ResponseEntity<FavoriteList>
// removeProductFromFavorite(@PathVariable("userId") User user,
// @PathVariable("productId") Product product) {
// return favoriteListService.removeProductFromFavoriteList(user, product);
// }
// }
