package com.shoppingwebsite.repository;

import com.shoppingwebsite.model.Cart;
import com.shoppingwebsite.model.CartItem;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @EntityGraph(attributePaths = { "cartItem", "cartItem.product" })
    Optional<Cart> findById(Integer id);

}