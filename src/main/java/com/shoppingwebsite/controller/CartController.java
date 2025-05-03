package com.shoppingwebsite.controller;

import com.shoppingwebsite.exceptions.NoContentExcetion;
import com.shoppingwebsite.exceptions.NotFoundException;
import com.shoppingwebsite.model.Cart;
import com.shoppingwebsite.model.CartItem;
import com.shoppingwebsite.model.Order;
import com.shoppingwebsite.model.User;
import com.shoppingwebsite.repository.CartRepository;
import com.shoppingwebsite.services.CartService;
import com.shoppingwebsite.services.OrderServise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderServise orderService;

    // Create a new cart
    @PostMapping("/{customerId}")
    public ResponseEntity<Cart> createCart(@PathVariable("customerId") Integer customerId) {
        try {
            Order order = orderService.createPindingOrder(customerId);
            Cart cart = cartService.createCart(customerId, order);

            if (cart == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(cart, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all carts
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        try {
            List<Cart> carts = cartRepository.findAll();

            if (carts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(carts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get cart by id
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable("id") Integer id) {
        Cart cart = cartService.geCart(id);

        // טריגר לטעינת המוצרים (להבטיח שה-product לא יישאר null)
        if (cart != null && cart.getCartItem() != null) {
            cart.getCartItem().forEach(item -> {
                if (item.getProduct() != null) {
                    item.getProduct().getProductName(); // טוען את שם המוצר
                    item.getProduct().getProductPrice(); // טוען את המחיר
                }
            });
        }

        return cart != null
                ? new ResponseEntity<>(cart, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get cart by customer id
    // @GetMapping("/user/{userId}")
    // public ResponseEntity<Cart> getCartByUserId(@PathVariable("userId") Integer
    // userId) {
    // try {
    // Cart cart = cartService.getCartByUserId(userId);

    // return new ResponseEntity<>(cart, HttpStatus.OK);

    // } catch (NotFoundException ex) {
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // } catch (NoContentExcetion ex) {
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // } catch (Exception e) {
    // return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    // Update cart total price****בדיקה
    @PutMapping("/{id}/update-total")
    public ResponseEntity<Cart> updateCartTotal(@PathVariable("id") Integer id) {
        try {
            Cart cart = cartService.updateCartTotal(id);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (NotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Clear cart (remove all items)
    @PutMapping("/{id}/clear")
    public ResponseEntity<Cart> clearCart(@PathVariable("id") Integer id) {
        try {
            Cart cart = cartService.clearCart(id);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (NotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete cart
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCart(@PathVariable("id") Integer id) {
        try {
            cartService.deleteCart(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Add a new cart item
    @PostMapping("/{cartId}/cartItem")
    public ResponseEntity<?> addCartItem(@PathVariable("cartId") Integer cartId,
            @RequestBody CartItem cartItem) {
        try {
            // בדיקה אם העגלה ריקה
            // List<CartItem> existingItems = cartService.getItemsByCartId(cartId);
            // boolean isCartEmpty = existingItems == null || existingItems.isEmpty();

            // if (isCartEmpty) {
            // // יצירת הזמנה חדשה עם סטטוס PENDING
            // User user = cartService.getUserByCartId(cartId); // הנח שיש לך פונקציה כזו
            // Order pendingOrder = orderService.getOrCreatePendingOrder(user); // מוודא
            // שאין כבר אחת קיימת

            // // קישור ה-CartItem להזמנה
            // // cartItem.setOrder(pendingOrder);
            // }
            // // IF(CART EMPTY)
            // // NEW ORDER WITH STATUS PENDING
            // // TO CONNECT IT WITH CARTITEM
            CartItem createdCartItem = cartService.addCartItem(cartItem, cartId);
            return new ResponseEntity<>(createdCartItem, HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{cartId}/cartItem")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Integer cartId,
            @RequestBody CartItem updatedCartItem) {
        try {
            System.out.println(
                    "עדכון מוצר בעגלה. Cart ID: " + cartId + ", CartItem ID: " + updatedCartItem.getCartItemId());
            CartItem cartItem = cartService.updateCartItem(cartId, updatedCartItem);
            return ResponseEntity.ok(cartItem);
        } catch (Exception ex) {
            System.out.println("שגיאה בעדכון מוצר בעגלה: " + ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{cartId}/cartItem/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Integer cartId, @PathVariable Integer id) {
        try {
            cartService.deleteCartItem(cartId, id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete all carts - admin functionality
    // @DeleteMapping
    // public ResponseEntity<HttpStatus> deleteAllCarts() {
    // try {
    // cartService.deleteAllCarts();
    // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // } catch (Exception e) {
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }
}
