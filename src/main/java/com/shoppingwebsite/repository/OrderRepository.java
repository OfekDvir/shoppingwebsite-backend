package com.shoppingwebsite.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shoppingwebsite.model.Order;
import com.shoppingwebsite.model.OrderStatus;
import com.shoppingwebsite.model.Product;
import com.shoppingwebsite.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser_UserId(Integer userId);

    Optional<Order> findByUser_UserIdAndStatus(Integer userId, OrderStatus status); // TEMP order

    List<Order> findAllByUser_UserIdOrderByStatusAscOrderDateDesc(Integer userId); // מסודר: TEMP ראשון, אחר כך סגורות

    Optional<Order> findByUserAndStatus(User user, OrderStatus status);

}
