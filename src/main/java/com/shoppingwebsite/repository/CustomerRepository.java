// package com.shoppingwebsite.repository;

// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;
// // import org.springframework.transaction.annotation.Transactional;

// import com.shoppingwebsite.model.Customer;

// // import com.touhid.onlineshop.model.Customer;

// @Repository

// public interface CustomerRepository extends JpaRepository<Customer, Integer>
// {

// Customer findByUser_email(String email);

// Customer findByUser_userId(Integer userId);

// Customer findCustomerByUser_emailAndUser_Password(String email, String
// password);

// }

// // //@Repository
// // public interface CustomerRepository extends JpaRepository<Customer,
// String> {
// // // Find customer by user
// // Optional<Customer> findByUsers(User user);

// // // Find customer by user ID
// // Optional<Customer> findByUsers_UserId(String userId);

// // // Find customers by name
// // List<Customer> findByFirstNameContainingOrLastNameContaining(String
// // firstName, String lastName);

// // // Find customer by phone
// // Optional<Customer> findByCustomerPhone(String phone);
// // }