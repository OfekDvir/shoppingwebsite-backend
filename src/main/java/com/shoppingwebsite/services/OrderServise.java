package com.shoppingwebsite.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingwebsite.exceptions.NotFoundException;
import com.shoppingwebsite.model.Address;
import com.shoppingwebsite.model.Cart;
import com.shoppingwebsite.model.CartItem;
import com.shoppingwebsite.model.Order;
import com.shoppingwebsite.model.OrderStatus;
import com.shoppingwebsite.model.Product;
import com.shoppingwebsite.repository.OrderRepository;
import com.shoppingwebsite.repository.ProductRepository;
import com.shoppingwebsite.repository.UserRepository;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import com.shoppingwebsite.model.User;

@Service
public class OrderServise {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Order addOrUpdateTempOrder(User user, Product product, int quantity) {
        Optional<Order> optionalTemp = orderRepository.findByUser_UserIdAndStatus(user.getUserId(), OrderStatus.TEMP);

        Order tempOrder;
        if (optionalTemp.isPresent()) {
            tempOrder = optionalTemp.get();
        } else {
            tempOrder = new Order();
            tempOrder.setUser(user);
            tempOrder.setOrderDate(new Date());
            tempOrder.setStatus(OrderStatus.TEMP);
            tempOrder.setOrderItems(new ArrayList<>());
        }

        // צור CartItem חדש וקשר אותו להזמנה
        CartItem orderItem = new CartItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setOrder(tempOrder);

        // הוסף את הפריט להזמנה (לרשימת הפריטים)
        tempOrder.getOrderItems().add(orderItem);

        // חישוב מחיר כולל אחרי שהוספנו את הפריט
        double total = tempOrder.getOrderItems().stream()
                .mapToDouble(ci -> ci.getProduct().getProductPrice() * ci.getQuantity())
                .sum();
        tempOrder.setTotalPrice(total);

        // שמירה
        return orderRepository.save(tempOrder);
    }

    @Transactional
    public void deleteTempOrderIfEmpty(Integer userId) {
        Optional<Order> optionalTemp = orderRepository.findByUser_UserIdAndStatus(userId, OrderStatus.TEMP);
        if (optionalTemp.isPresent()) {
            Order temp = optionalTemp.get();
            if (temp.getOrderItems().isEmpty()) {
                orderRepository.delete(temp);
            }
        }
    }

    public Order createOrder(User user, Address shippingAddress, Address billingAddress,
            List<Map<String, Object>> rawItems) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
        order.setStatus(OrderStatus.DOONE);

        double total = 0;
        List<CartItem> items = new ArrayList<>();

        for (Map<String, Object> itemMap : rawItems) {
            int quantity = (int) itemMap.get("quantity");
            Map<String, Object> productMap = (Map<String, Object>) itemMap.get("product");
            Integer productId = (Integer) productMap.get("productId");

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found"));

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setOrder(order);
            items.add(cartItem);

            total += product.getProductPrice() * quantity;
        }

        order.setOrderItems(items);
        order.setTotalPrice(total);
        System.out.println(order.getTotalPrice());
        return orderRepository.save(order);// new to do
    }

    public Order createOrder2(Order order) {
        return orderRepository.save(order);
    }

    public Order createPindingOrder(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);

        return order; // ⛔️ אל תשמור כאן עדיין!
    }

    // public Order createPindingOrder(int id) {/////////////////////////////////
    // Order order = new Order();
    // User user = userRepository.findByUserId(id);
    // order.setUser(user);
    // order.setOrderDate(new Date());
    // order.setStatus(OrderStatus.PENDING);
    // return orderRepository.save(order);
    // }

    public Optional<Order> getOrderById(Integer id) {
        Optional<Order> orderData = orderRepository.findById(id);
        return orderData;
    }

    public void updateOrder(Order order) {
        Optional<Order> orderData = orderRepository.findById(order.getId());

        if (orderData.isPresent()) {
            orderRepository.save(order);
        }

    }

    public List<Order> getOrdersByUser(Integer userId) {
        return orderRepository.findByUser_UserId(userId); // ❗ ודא שזה תואם
    }

    public Order getOrCreatePendingOrder(User user) {
        Optional<Order> existing = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING);
        if (existing.isPresent()) {
            return existing.get();
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);
        return order;
    }

    // public Order getOrCreatePendingOrder(User user) {
    // return orderRepository.findByUser_UserIdAndStatus(user.getUserId(),
    // OrderStatus.PENDING)///////////////////////////////
    // .orElseGet(() -> {
    // Order order = new Order();
    // order.setUser(user);
    // order.setOrderDate(new Date());
    // order.setStatus(OrderStatus.PENDING);
    // return orderRepository.save(order);
    // });
    // }
    public void addItemToOrder(Order order, Integer productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setOrder(order);

        order.getOrderItems().add(item);
        orderRepository.save(order);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    /// הוספתי
    public Optional<Order> getOrderByUserAndStatus(Integer userId, OrderStatus status) {
        return orderRepository.findByUser_UserIdAndStatus(userId, status);
    }

    public Order createEmptyOrderAndCartForUser(User user) {
        Cart newCart = new Cart();

        // ✅ Use copy constructor instead of manual field copying
        Address shippingAddress = new Address(user.getAddress());
        Address billingAddress = new Address(user.getAddress());

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setCart(newCart);
        newOrder.setShippingAddress(shippingAddress);
        newOrder.setBillingAddress(billingAddress);

        newCart.setOrder(newOrder); // bidirectional link
        user.getOrders().add(newOrder); // optional if JPA cascade handles it

        return orderRepository.save(newOrder);
    }

}
