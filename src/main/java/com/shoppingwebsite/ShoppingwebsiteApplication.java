package com.shoppingwebsite;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.shoppingwebsite.exceptions.NotFoundException;
import com.shoppingwebsite.model.Address;
import com.shoppingwebsite.model.Cart;
import com.shoppingwebsite.model.CartItem;
import com.shoppingwebsite.model.Customer;
import com.shoppingwebsite.model.FavoriteList;
import com.shoppingwebsite.model.Order;
import com.shoppingwebsite.model.OrderStatus;
import com.shoppingwebsite.model.Product;
import com.shoppingwebsite.model.User;
import com.shoppingwebsite.services.CartService;
import com.shoppingwebsite.services.FavoriteListService;
import com.shoppingwebsite.services.OrderServise;
import com.shoppingwebsite.services.ProductService;
import com.shoppingwebsite.services.UserService;

@SpringBootApplication
public class ShoppingwebsiteApplication implements ApplicationRunner {

	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderServise orderService;

	public static void main(String[] args) {
		SpringApplication.run(ShoppingwebsiteApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Product p1 = null, p2 = null;
		if (productService.getAllProducts().size() != 2) {

			p1 = new Product();
			p1.setCategory("Electronics");
			p1.setDescription("Latest model Apple Watch");
			p1.setProductImage("");
			p1.setProductName("Apple Watch");
			p1.setManufacturer("Apple");
			p1.setProductPrice(105.6);
			p1.setUnitStock(50);

			productService.createProduct(p1);
			System.out.println("Product 1 (Apple) saved successfully.");

			p2 = new Product();
			p2.setCategory("Electronics");
			p2.setDescription("Latest model smartphone with 128GB storage");
			p2.setProductImage("");
			p2.setProductName("Smartphone X");
			p2.setManufacturer("Samsung");
			p2.setProductPrice(799.99);
			p2.setUnitStock(30);

			productService.createProduct(p2);
			System.out.println("Product 2 (Smartphone X) saved successfully.");

		} else {
			List<Product> products = productService.getAllProducts();
			p1 = products.get(0);
			p2 = products.get(1);
		}
		try {
			userService.getUser(1);

		} catch (NotFoundException ex) {
			User user1 = new User(null, "dvir.ron87@gmail.com", "123", true, "ofek", "dvir", "0535258091", null);

			user1 = userService.createUser(user1);
			System.out.println("Customer Id" + user1.getUserId());
		}

		try {
			User user1 = userService.getUser(1);
			if (user1.getAddress() == null) {
				Address newAddress = new Address();
				newAddress.setAddress("123 Main Street");
				newAddress.setCity("New York");
				newAddress.setState("NY");
				newAddress.setZipcode("10001");
				newAddress.setCountry("USA");
				user1.setAddress(newAddress);
				userService.updateUser(user1);
				System.out.println("Address Created: " + newAddress.getAddress());
			}
			// Check if an Order exists; if not, create one
			// Order newOrder = new Order();
			// newOrder.setUser(user1);
			// newOrder.setOrderDate(new Date());
			// newOrder.setShippingAddress(user1.getShippingAddress());
			// newOrder.setBillingAddress(user1.getShippingAddress());
			// newOrder.setTotalPrice(199.99);
			// newOrder.setStatus(OrderStatus.PENDING);
			// newOrder = orderService.createOrder2(newOrder);
			// System.out.println("Order Created with Status: " + newOrder.getStatus());
			// Cart cart = cartService.createCart(1, newOrder);
			// System.out.println(cart.getCartId());
			// user1.setCart(cart);
			// userService.updateUser(user1);

			// // Check if a CartItem exists; if not, create one
			// CartItem newCartItem = new CartItem(null, 2, p1, cart);
			// cartService.addCartItem(newCartItem, cart.getCartId());
			// System.out.println("CartItem Created with Quantity: " +
			// newCartItem.getQuantity());

		} catch (NotFoundException ex) {
			System.out.println("user1 does ont exust for shipping address");
		}

	}
}
