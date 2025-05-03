// package com.shoppingwebsite.services;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;

// import com.shoppingwebsite.exceptions.NotFoundException;
// import com.shoppingwebsite.model.Address;
// import com.shoppingwebsite.model.Customer;
// import com.shoppingwebsite.repository.CustomerRepository;

// @Service
// public class CustomerService {

// @Autowired
// private CustomerRepository customerRepository;

// public Customer createCustomer(Customer customer) {
// Customer newCustomer = customerRepository.save(customer);
// return newCustomer;

// }

// public Optional<Customer> getCustomerById(Integer id) {
// Optional<Customer> customerData = customerRepository.findById(id);
// return customerData;

// }

// public Customer getCustomerByUserId(Integer id) {
// Customer customerData = customerRepository.findByUser_userId(id);
// return customerData;

// }

// public void updateCustomer(Customer customer) throws NotFoundException {
// Optional<Customer> customerData =
// customerRepository.findById(customer.getCustomerId());
// if (customerData.isPresent()) {
// Customer existingCustomer = customerData.get();

// // Update basic info
// existingCustomer.setFirstName(customer.getFirstName());
// existingCustomer.setLastName(customer.getLastName());
// existingCustomer.setCustomerPhone(customer.getCustomerPhone());

// // Only update relationships if they're provided in the request
// if (customer.getShippingAddress() != null) {
// Address shippingAddress = existingCustomer.getShippingAddress();
// if (shippingAddress == null) {
// shippingAddress = new Address();
// existingCustomer.setShippingAddress(shippingAddress);
// }

// // Update shipping address fields
// shippingAddress.setAddress(customer.getShippingAddress().getAddress());
// shippingAddress.setCity(customer.getShippingAddress().getCity());
// shippingAddress.setState(customer.getShippingAddress().getState());
// shippingAddress.setZipcode(customer.getShippingAddress().getZipcode());
// shippingAddress.setCountry(customer.getShippingAddress().getCountry());
// }

// if (customer.getBillingAddress() != null) {
// Address billingAddress = existingCustomer.getBillingAddress();
// if (billingAddress == null) {
// billingAddress = new Address();
// existingCustomer.setBillingAddress(billingAddress);
// }

// // Update billing address fields
// billingAddress.setAddress(customer.getBillingAddress().getAddress());
// billingAddress.setCity(customer.getBillingAddress().getCity());
// billingAddress.setState(customer.getBillingAddress().getState());
// billingAddress.setZipcode(customer.getBillingAddress().getZipcode());
// billingAddress.setCountry(customer.getBillingAddress().getCountry());
// }
// customerRepository.save(existingCustomer);
// } else {
// throw new NotFoundException("coustomer not found");
// }

// }

// public Customer updateShippingAddress(Integer id, Address shippingAddress)
// throws NotFoundException {
// Optional<Customer> customerData = customerRepository.findById(id);

// if (customerData.isPresent()) {
// Customer customer = customerData.get();

// // Ensure shipping address exists
// if (customer.getShippingAddress() == null) {
// customer.setShippingAddress(new Address());
// }

// // Update shipping address fields
// copyAddressFields(customer.getShippingAddress(), shippingAddress);

// // Save and return updated customer
// return customer;
// } else {
// throw new NotFoundException("coustomer not found");
// }
// }

// public Customer updateBillingAddress(Integer id, Address billingAddress)
// throws NotFoundException {
// Optional<Customer> customerData = customerRepository.findById(id);

// if (customerData.isPresent()) {
// Customer customer = customerData.get();

// // Ensure billing address exists
// if (customer.getBillingAddress() == null) {
// customer.setBillingAddress(new Address());
// }

// // Update billing address fields
// copyAddressFields(customer.getBillingAddress(), billingAddress);

// // Save and return updated customer
// return customer;
// } else {
// throw new NotFoundException("coustomer not found");
// }
// }

// //// מה זה הוסיף
// private void copyAddressFields(Address billingAddress, Address
// billingAddress2) {
// // TODO Auto-generated method stub
// throw new UnsupportedOperationException("Unimplemented method
// 'copyAddressFields'");
// }

// public ResponseEntity<HttpStatus> deleteCustomer(Integer id) {
// try {
// Optional<Customer> customerData = customerRepository.findById(id);

// if (customerData.isEmpty()) {
// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// }

// customerRepository.deleteById(id);
// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// } catch (Exception e) {
// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// public ResponseEntity<HttpStatus> deleteAllCustomers() {
// try {
// customerRepository.deleteAll();
// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// } catch (Exception e) {
// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// }
