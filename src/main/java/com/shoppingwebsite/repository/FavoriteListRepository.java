package com.shoppingwebsite.repository;

import com.shoppingwebsite.model.FavoriteList;
import com.shoppingwebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FavoriteListRepository extends JpaRepository<FavoriteList, Long> {
    Optional<FavoriteList> findByUser(User user);

    // public interface FavoriteListRepository extends JpaRepository<FavoriteList,
    // Long> {
    // Optional<FavoriteList> findByUser(User user);
}
