package com.shoppingwebsite.controller;

import com.shoppingwebsite.exceptions.NotFoundException;
import com.shoppingwebsite.model.*;
import com.shoppingwebsite.repository.*;
import com.shoppingwebsite.services.OrderServise;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServise orderServise;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> payload) {
        try {
            Integer userId = (Integer) payload.get("userId");

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            Order order = orderServise.getOrderByUserAndStatus(userId, OrderStatus.PENDING)
                    .orElseThrow(() -> new NotFoundException("No pending order found"));

            Map<String, String> shipping = (Map<String, String>) payload.get("shippingInfo");

            Address newAddress = new Address();
            newAddress.setAddress(shipping.get("address"));
            newAddress.setCity(shipping.get("city"));
            newAddress.setZipcode(shipping.get("zip"));
            newAddress.setCountry(shipping.get("country"));

            if (!newAddress.equals(order.getShippingAddress())) {
                addressRepository.save(newAddress);
                order.setShippingAddress(newAddress);
                order.setBillingAddress(newAddress);
            }

            order.setStatus(OrderStatus.DOONE);
            order.setTotalPrice(order.getCart().getTotalPrice());
            orderServise.createOrder2(order);
            Order newOrder = orderServise.createEmptyOrderAndCartForUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @PostMapping
    // @Transactional
    // public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object>
    // payload) {
    // try {
    // Integer userId = (Integer) payload.get("userId");

    // // שליפת המשתמש
    // User user = userRepository.findById(userId)
    // .orElseThrow(() -> new NotFoundException("User not found"));

    // // שליפת פרטי משלוח מה־payload
    // Map<String, String> shipping = (Map<String, String>)
    // payload.get("shippingInfo");

    // Address shippingAddress = new Address();
    // shippingAddress.setAddress(shipping.get("address"));
    // shippingAddress.setCity(shipping.get("city"));
    // shippingAddress.setZipcode(shipping.get("zip"));
    // shippingAddress.setCountry(shipping.get("country"));
    // addressRepository.save(shippingAddress);

    // Address billingAddress = new Address();
    // billingAddress.setAddress(shipping.get("address"));
    // billingAddress.setCity(shipping.get("city"));
    // billingAddress.setZipcode(shipping.get("zip"));
    // billingAddress.setCountry(shipping.get("country"));
    // addressRepository.save(billingAddress);

    // // יצירת ההזמנה
    // // Order order = new Order();// RETRIVE THE RIGHT ORDER
    // // Optional<Order> optionalOrder =
    // orderServise.getOrderByUserAndStatus(userId,
    // // OrderStatus.PENDING);/////////

    // // Order order = optionalOrder.orElseThrow(() -> new NotFoundException("No
    // // pending order found for user"));////////////
    // Optional<Order> optionalOrder = orderServise.getOrderByUserAndStatus(userId,
    // OrderStatus.PENDING);
    // Order order = orderServise
    // .getOrderByUserAndStatus(userId, OrderStatus.PENDING)
    // .orElseGet(() -> orderServise.createPindingOrder(userId)); // 👈 יצירה במקום
    // זריקת שגיאה

    // order.setShippingAddress(shippingAddress);
    // order.setBillingAddress(billingAddress);
    // order.setStatus(OrderStatus.DOONE);
    // // order = orderServise.createOrder2(order);

    // // שליפת המוצרים מהעגלה
    // Map<String, Object> cartMap = (Map<String, Object>) payload.get("cart");
    // List<Map<String, Object>> cartItems = (List<Map<String, Object>>)
    // cartMap.get("cartItem");

    // double totalPrice = 0;
    // List<CartItem> orderItems = new ArrayList<>();
    // System.out.println("🧾 פריטים שהגיעו להזמנה:");

    // for (Map<String, Object> itemMap : cartItems) {
    // int quantity = (int) itemMap.get("quantity");

    // Map<String, Object> productMap = (Map<String, Object>)
    // itemMap.get("product");
    // Integer productId = (Integer) productMap.get("productId");

    // Product product = productRepository.findById(productId)
    // .orElseThrow(() -> new NotFoundException("Product not found"));

    // CartItem orderItem = new CartItem();
    // orderItem.setProduct(product);
    // orderItem.setQuantity(quantity);
    // orderItem.setOrder(order);/////// ⬅️ הוספה חשובה/////////////////
    // orderItems.add(orderItem);
    // totalPrice += product.getProductPrice() * quantity;
    // }

    // // קישור הפריטים והמחיר להזמנה
    // order.setOrderItems(orderItems);
    // order.setTotalPrice(totalPrice);

    // // שמירת ההזמנה
    // // Order saved = orderServise.createOrder(user, shippingAddress,
    // billingAddress,
    // // cartItems);
    // Order saved = orderServise.createOrder2(order);
    // return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    // } catch (Exception e) {
    // e.printStackTrace();
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    // }
    // }

    // Get all orders for user (including TEMP if exists)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Integer userId) {
        List<Order> orders = orderServise.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    // Get order by ID
    @GetMapping("/{orderId}")

    public ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
        Optional<Order> order = orderServise.getOrderById(orderId);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update order (for TEMP status only)
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer orderId, @RequestBody Order updatedOrder) {
        Optional<Order> existingOrder = orderServise.getOrderById(orderId);

        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();

            if (order.getStatus() != OrderStatus.DOONE) {
                order.setShippingAddress(updatedOrder.getShippingAddress());
                order.setBillingAddress(updatedOrder.getBillingAddress());
                order.setTotalPrice(updatedOrder.getTotalPrice());
                order.setStatus(updatedOrder.getStatus());
                orderServise.updateOrder(order);
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/add-item")
    public ResponseEntity<?> addItemToPendingOrder(@RequestBody Map<String, Object> payload) {
        Integer userId = (Integer) payload.get("userId");
        Integer productId = (Integer) payload.get("productId");
        int quantity = (int) payload.get("quantity");

        User user = userRepository.findById(userId).orElseThrow();
        Order pendingOrder = orderServise.getOrCreatePendingOrder(user);
        orderServise.addItemToOrder(pendingOrder, productId, quantity);

        return ResponseEntity.ok(pendingOrder);
    }

    @PutMapping("/complete/{orderId}")
    public ResponseEntity<?> completeOrder(@PathVariable Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.DOONE);
        orderRepository.save(order);
        return ResponseEntity.ok("Order completed");
    }

}