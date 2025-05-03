package com.shoppingwebsite.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity
@Table(name = "cart")
public class Cart implements Serializable {

	private static final long serialVersionUID = 8436097833452420298L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer cartId;

	@OneToOne(mappedBy = "cart")
	@JsonIgnore // ⛔ אל תכלול את המשתמש כשמייצאים את ההזמנה

	private Order order;

	private String name;//// הוספתי name

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CartItem> cartItem;

	private double totalPrice;

	public String getName() {/// הוספתי name
		return name;
	}

	public void setName(String name) {/// הוספתי name
		this.name = name;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public List<CartItem> getCartItem() {
		return cartItem;
	}

	public void setCartItem(List<CartItem> cartItem) {
		this.cartItem = cartItem;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Cart(Integer cartId, User user, List<CartItem> cartItem, double totalPrice, String name) {
		this.cartId = cartId;
		this.cartItem = cartItem;
		this.totalPrice = totalPrice;
		this.name = name;
	}

	public Cart() {
	};

	public Cart(String name, User user) {/// היה חסר את זה
		this.name = name;
		this.cartItem = new java.util.ArrayList<>();
		this.totalPrice = 0.0;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}