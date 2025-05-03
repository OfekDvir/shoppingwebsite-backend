package com.shoppingwebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.CrudRepository;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
// import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import com.shoppingwebsite.model.Cart;
////
import com.shoppingwebsite.model.CartItem;
import com.shoppingwebsite.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    // Find all items in a cart
    List<CartItem> findByCart(Cart cart);

    // Find a specific product in a cart
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // Delete all items in a cart
    void deleteByCart(Cart cart);

    // Find items by product
    List<CartItem> findByProduct(Product product);

    List<CartItem> findByCart_CartId(Integer cartId);

}
