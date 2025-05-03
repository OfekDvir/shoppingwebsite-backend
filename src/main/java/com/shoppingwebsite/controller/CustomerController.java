// package com.shoppingwebsite.controller;

// import com.shoppingwebsite.model.Address;
// import com.shoppingwebsite.model.Cart;
// import com.shoppingwebsite.model.Customer;
// import com.shoppingwebsite.model.Address;
// import com.shoppingwebsite.model.User;
// import com.shoppingwebsite.repository.CustomerRepository;
// import com.shoppingwebsite.services.CustomerService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/customers")
// public class CustomerController {

// @Autowired
// private CustomerRepository customerRepository;

// @Autowired
// private CustomerService customerService;

// // Create a new customer
// @PostMapping
// public ResponseEntity<Customer> createCustomer(@RequestBody Customer
// customer) {
// try {
// Customer newCustomer = customerService.createCustomer(customer);
// return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
// } catch (Exception e) {
// return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// //// לא חושב שצריך
// // Get all customers
// // @GetMapping
// // public ResponseEntity<List<Customer>> getAllCustomers() {
// // try {
// // Iterable<CustomerRepository> customers = customerRepository.findAll();

// // if (customers.isEmpty()) {
// // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// // }

// // return new ResponseEntity<>(customers, HttpStatus.OK);
// // } catch (Exception e) {
// // return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
// // }
// // }

// // Get customer by id

// @GetMapping("/{id}")
// public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Integer
// id) {

// Optional<Customer> customerData = customerService.getCustomerById(id);
// return customerData
// .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
// .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
// }

// // Get customer by user id
// @GetMapping("/user/{userId}")
// public ResponseEntity<Customer> getCustomerByUserId(@PathVariable("userId")
// Integer userId) {
// Customer customerData = customerService.getCustomerByUserId(userId);

// if (customerData == null) {
// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// }
// return new ResponseEntity<>(customerData, HttpStatus.OK);

// // return customerData == null? new ResponseEntity<>(HttpStatus.NOT_FOUND)
// :new
// // ResponseEntity<>(customerData, HttpStatus.OK);

// // return customerData
// // .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
// // .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

// }

// /// לא הצלחתי
// // Update customer
// @PutMapping("/{id}")
// public ResponseEntity<Customer> updateCustomer(@PathVariable("id") String id,
// @RequestBody Customer customer) {

// try {
// customerService.updateCustomer(customer);
// return new ResponseEntity<>(HttpStatus.OK);
// } catch (Exception exception) {
// return new ResponseEntity<>(HttpStatus.NOT_FOUND);

// }
// }

// // Update shipping address
// @PutMapping("/{id}/shipping-address")
// public ResponseEntity<Customer> updateShippingAddress(
// @PathVariable("id") Integer id,
// @RequestBody Address shippingAddress) {
// try {
// Customer customer = customerService.updateShippingAddress(id,
// shippingAddress);
// return new ResponseEntity<>(customer, HttpStatus.OK);

// } catch (Exception exception) {
// return new ResponseEntity<>(HttpStatus.NOT_FOUND);

// }

// }
// // public ResponseEntity<Customer> updateShippingAddress(
// // @PathVariable("id") String id,
// // @RequestBody Address shippingAddress) {

// // Optional<Customer> customerData = customerRepository.findById(id);

// // if (customerData.isPresent()) {
// // Customer customer = customerData.get();

// // if (customer.getShippingAddress() == null) {
// // customer.setShippingAddress(new Address());
// // }

// // Address existingAddress = customer.getShippingAddress();
// // existingAddress.setAddress(shippingAddress.getAddress());
// // existingAddress.setCity(shippingAddress.getCity());
// // existingAddress.setState(shippingAddress.getState());
// // existingAddress.setZipcode(shippingAddress.getZipcode());
// // existingAddress.setCountry(shippingAddress.getCountry());

// // return new ResponseEntity<>(customerRepository.save(customer),
// // HttpStatus.OK);
// // } else {
// // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// // }

// // Update billing address
// @PutMapping("/{id}/billing-address")
// public ResponseEntity<Customer> updateBillingAddress(
// @PathVariable("id") Integer id,
// @RequestBody Address billingAddress) {
// try {
// Customer customer = customerService.updateBillingAddress(id, billingAddress);
// return new ResponseEntity<>(customer, HttpStatus.OK);

// } catch (Exception exception) {
// return new ResponseEntity<>(HttpStatus.NOT_FOUND);

// }
// }
// // public ResponseEntity<Customer> updateAddress(
// // @PathVariable("id") String id,
// // @RequestBody Address billingAddress) {

// // Optional<Customer> customerData = customerRepository.findById(id);

// // if (customerData.isPresent()) {
// // Customer customer = customerData.get();

// // if (customer.getAddress() == null) {
// // customer.setAddress(new Address());
// // }

// // Address existingAddress = customer.getAddress();
// // existingAddress.setAddress(billingAddress.getAddress());
// // existingAddress.setCity(billingAddress.getCity());
// // existingAddress.setState(billingAddress.getState());
// // existingAddress.setZipcode(billingAddress.getZipcode());
// // existingAddress.setCountry(billingAddress.getCountry());

// // return new ResponseEntity<>(customerRepository.save(customer),
// // HttpStatus.OK);
// // } else {
// // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// // }

// // Delete customer
// @DeleteMapping("/{id}")
// public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") Integer
// id) {
// return customerService.deleteCustomer(id);
// }
// // public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id")
// String
// // id) {
// // try {
// // Optional<Customer> customerData = customerRepository.findById(id);

// // if (!customerData.isPresent()) {
// // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// // }

// // customerRepository.deleteById(id);
// // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// // } catch (Exception e) {
// // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
// // }
// // }

// // Delete all customers
// @DeleteMapping
// public ResponseEntity<HttpStatus> deleteAllCustomers() {
// return customerService.deleteAllCustomers();
// }
// }
// // @PostMapping("/register")
// // public ResponseEntity<Customer> registerCustomer(@RequestBody
// RegisterRequest
// // request) {
// // // 1. Create and save the User entity
// // User user = new User();
// // user.setUsername(request.getEmail());
// // user.setPassword(passwordEncoder.encode(request.getPassword())); // Always
// // encode passwords!
// // userRepository.save(user);

// // // 2. Create Customer and set User
// // Customer customer = new Customer();
// // customer.setFirstName(request.getFirstName());
// // customer.setLastName(request.getLastName());
// // customer.setCustomerPhone(request.getPhone());
// // customer.setUser(user);

// // // 3. Create and assign empty Cart
// // Cart cart = new Cart();
// // cart.setCustomer(customer);
// // cart.setCartItem(new ArrayList<>());
// // cart.setTotalPrice(0.0);
// // cartRepository.save(cart); // Save cart to DB

// // customer.setCart(cart);
// // customerRepository.save(customer); // Save full customer

// // return new ResponseEntity<>(customer, HttpStatus.CREATED);
// // }