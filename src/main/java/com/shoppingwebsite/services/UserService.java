package com.shoppingwebsite.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingwebsite.exceptions.EmailAlreadyExistsException;
import com.shoppingwebsite.exceptions.NotFoundException;
import com.shoppingwebsite.model.Address;
import com.shoppingwebsite.model.Cart;
import com.shoppingwebsite.model.Order;
import com.shoppingwebsite.model.OrderStatus;
import com.shoppingwebsite.model.User;
import com.shoppingwebsite.repository.CartRepository;
import com.shoppingwebsite.repository.OrderRepository;
import com.shoppingwebsite.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderServise orderServise;

    // Register User
    public User createUser(User user) throws EmailAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " already exists");
        }

        if (user.getEmail().equals("admin@gmail.com")) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }

        User savedUser = userRepository.save(user);

        // צור הזמנה ריקה + עגלה חדשה עם כתובות (משוכפלות מהכתובת של המשתמש)
        orderServise.createEmptyOrderAndCartForUser(savedUser);

        return savedUser;
    }

    // Login User
    public User login(String email, String password) {
        System.out.println("EMAIL: [" + email + "]");
        System.out.println("PASS: [" + password + "]");

        User user = userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // יצירת עגלה למשתמש אם אין לו עדיין
        // if (user.getOrders().isEmpty()) {
        // Cart newCart = new Cart();
        // Order newOrder = new Order();
        // newOrder.setUser(user);
        // newOrder.setOrderDate(new Date());
        // newOrder.setStatus(OrderStatus.PENDING);
        // newOrder.setCart(newCart);
        // newCart.setOrder(newOrder); // 💡 חשוב – שמירה של הקשר ההפוך

        // user.getOrders().add(newOrder); // 💡 שמירה של הקשר ההפוך גם ביוזר
        // orderRepository.save(newOrder);

        // }

        return user;
    }

    // Get User by ID
    public User getUser(Integer userId) throws NotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " was not found"));
    }

    // Get User by Email
    public User getUserByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    // Update User
    public User updateUser(User user) throws NotFoundException {
        if (!userRepository.existsById(user.getUserId())) {
            throw new NotFoundException("User with ID " + user.getUserId() + " was not found");
        }
        return userRepository.save(user);
    }

    // Delete User
    public void deleteUser(Integer userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with ID " + userId + " was not found");
        }
        userRepository.deleteById(userId);
    }
}

// package com.shoppingwebsite.services;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.shoppingwebsite.exceptions.EmailAlreadyExistsException;
// import com.shoppingwebsite.exceptions.NotFoundException;
// import com.shoppingwebsite.model.Cart;
// import com.shoppingwebsite.model.User;
// import com.shoppingwebsite.repository.CartRepository;
// import com.shoppingwebsite.repository.UserRepository;

// @Service
// public class UserService {

// @Autowired
// private UserRepository userRepository;

// public User getUserByEmail(String email) {
// return userRepository.findByEmail(email);
// }

// public User createUser(User user) throws EmailAlreadyExistsException {
// User existingUser = userRepository.findByEmail(user.getEmail());
// if (existingUser != null) {
// throw new EmailAlreadyExistsException("email " + user.getEmail() + " already
// exists");
// }
// user.setEnabled(true);
// userRepository.save(user);
// return user;
// }

// public User getUser(Integer userId) throws NotFoundException {
// Optional<User> existingUser = userRepository.findById(userId);
// if (existingUser.isEmpty()) {
// throw new NotFoundException("user id" + userId + " was not found");
// }
// return existingUser.get();
// }

// public User updateUser(User user) throws NotFoundException {
// Optional<User> existingUser = userRepository.findById(user.getUserId());
// if (existingUser.isEmpty()) {
// throw new NotFoundException("user id" + user.getUserId() + " was not found");
// }
// userRepository.save(user);
// return user;
// }

// public void deleteUser(Integer userId) throws NotFoundException {
// Optional<User> existingUser = userRepository.findById(userId);
// if (existingUser.isEmpty()) {
// throw new NotFoundException("user id" + userId + " was not found");
// }
// userRepository.deleteById(userId);
// }

// @Autowired
// private CartRepository cartRepository;

// public User login(String email, String password) {
// User user = userRepository.findByEmailAndPassword(email, password)
// .orElseThrow(() -> new RuntimeException("Invalid credentials"));

// // יצירת עגלה אם עדיין לא קיימת
// if (user.getCart() == null) {
// Cart newCart = new Cart();
// newCart.setUser(user);
// cartRepository.save(newCart);
// user.setCart(newCart);
// userRepository.save(user); // לשמור את הקשר
// }

// return user;
// }

// }
