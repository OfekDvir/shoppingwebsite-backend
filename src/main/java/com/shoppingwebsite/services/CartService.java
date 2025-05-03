package com.shoppingwebsite.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingwebsite.exceptions.NoContentExcetion;
import com.shoppingwebsite.exceptions.NotFoundException;
import com.shoppingwebsite.model.Cart;
import com.shoppingwebsite.model.CartItem;
import com.shoppingwebsite.model.Order;
import com.shoppingwebsite.model.Product;
import com.shoppingwebsite.model.User;
import com.shoppingwebsite.repository.CartItemRepository;
import com.shoppingwebsite.repository.CartRepository;
import com.shoppingwebsite.repository.ProductRepository;
import com.shoppingwebsite.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart createCart(Integer customerId, Order order) {
        Optional<User> opt = userRepository.findById(customerId);
        if (opt.isEmpty()) {
            return null;
        }

        User customer = opt.get();

        Cart cart = new Cart(customer.getFirstName() + " cart", customer);

        userRepository.save(customer);
        cartRepository.save(cart);

        return cart;
    }

    public Cart geCart(Integer id) {
        return cartRepository.findById(id).orElse(null);
    }

    // public Cart getCartByUserId(Integer customerId) throws NotFoundException,
    // NoContentExcetion {
    // Optional<User> opt = userRepository.findById(customerId);
    // if (opt.isEmpty()) {
    // throw new NotFoundException("customerId was not found");
    // }

    // User customer = opt.get();

    // if (customer.getCart() == null) {
    // throw new NoContentExcetion("customer has no cart");
    // }

    // return customer.getCart();
    // }

    public Cart updateCartTotal(Integer id) throws NotFoundException {
        Cart cart = geCart(id);
        if (cart == null) {
            throw new NotFoundException("Cart was not found");
        }

        updateCartTotalPrice(cart);
        return cartRepository.save(cart);
    }

    public Cart clearCart(Integer id) throws NotFoundException {
        Cart cart = geCart(id);
        if (cart == null) {
            throw new NotFoundException("Cart was not found");
        }

        cart.setCartItem(new ArrayList<>());
        cart.setTotalPrice(0.0);
        return cartRepository.save(cart);
    }

    public void deleteCart(Integer id) throws NotFoundException {
        Cart cart = geCart(id);
        if (cart == null) {
            throw new NotFoundException("Cart was not found");
        }
        cart.getOrder();// detele otder instead

        cartRepository.deleteById(id);
    }

    // הפונקציה החשובה שהוספנו טיפול בבדיקת מוצר קיים
    public CartItem addCartItem(CartItem cartItem, Integer cartId) throws NotFoundException {

        Cart cart = geCart(cartId);
        if (cart == null) {
            throw new NotFoundException("Cart was not found");
        }

        Integer productId = cartItem.getProduct().getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));

        cartItem.setProduct(product);

        Optional<CartItem> existingItemOpt = cart.getCartItem().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        int requestedQuantity = cartItem.getQuantity();
        if (product.getUnitStock() < requestedQuantity) {
            throw new IllegalStateException("Not enough stock available");
        }

        // מוריד מלאי
        product.setUnitStock(product.getUnitStock() - requestedQuantity);
        productRepository.save(product); // חשוב

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + requestedQuantity);
            cartItemRepository.save(existingItem);
        } else {
            cart.getCartItem().add(cartItem);
            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);
        }

        double itemTotal = requestedQuantity * product.getProductPrice();
        cart.setTotalPrice(cart.getTotalPrice() + itemTotal);
        cartRepository.save(cart);

        return cartItem;
    }

    public CartItem updateCartItem(Integer cartId, CartItem updatedCartItem) throws NotFoundException {
        Cart cart = geCart(cartId);
        if (cart == null) {
            throw new NotFoundException("Cart was not found");
        }

        CartItem existingItem = cartItemRepository.findById(updatedCartItem.getCartItemId())
                .orElseThrow(() -> new NotFoundException("CartItem was not found"));

        existingItem.setQuantity(updatedCartItem.getQuantity());
        cartItemRepository.save(existingItem);

        updateCartTotalPrice(cart);
        cartRepository.save(cart);

        return existingItem;
    }

    public void deleteCartItem(Integer cartId, Integer id) throws NotFoundException {
        Cart cart = geCart(cartId);
        if (cart == null) {
            throw new NotFoundException("Cart was not found");
        }

        CartItem itemToRemove = cart.getCartItem().stream()
                .filter(item -> item.getCartItemId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("CartItem not found"));

        cart.getCartItem().remove(itemToRemove);
        cartItemRepository.deleteById(id);

        updateCartTotalPrice(cart);
        cartRepository.save(cart);
    }

    private void updateCartTotalPrice(Cart cart) {
        double total = cart.getCartItem().stream()
                .mapToDouble(CartItem::calcPrice)
                .sum();

        cart.setTotalPrice(total);
    }

    //// הוספתי

    public List<CartItem> getItemsByCartId(Integer cartId) {
        return cartItemRepository.findByCart_CartId(cartId);
    }

}
