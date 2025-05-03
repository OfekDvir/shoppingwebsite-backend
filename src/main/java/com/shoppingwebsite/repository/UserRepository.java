package com.shoppingwebsite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingwebsite.model.User;

@Repository

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    User findByUserId(Integer userId);

    // User findByEmailAndPassword(String email, String password);

    Optional<User> findByEmailAndPassword(String email, String password);

}