package com.shoppingwebsite.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // ⛔ אל תכלול את המשתמש כשמייצאים את ההזמנה
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "order_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

    @Column(name = "total_price")
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(User user, Date orderDate, Address shippingAddress, Address billingAddress, double totalPrice,
            OrderStatus status) {
        this.user = user;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<CartItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CartItem> items) {
        this.orderItems.clear(); // מנקה את הרשימה הקיימת
        if (items != null) {
            for (CartItem item : items) {
                item.setOrder(this); // שמירה על הקשר הדו-צדדי
                this.orderItems.add(item);
            }
        }
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}

// public void setOrderItems(List<CartItem> orderItems) {
// this.orderItems = orderItems;

// if (orderItems != null) {
// for (CartItem item : orderItems) {
// item.setOrder(this); // קשר דו-צדדי הכרחי
// }
// }
// }
// }

// package com.shoppingwebsite.model;

// import jakarta.persistence.*;

// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
// import org.springframework.core.Ordered;

// @Entity
// @Table(name = "orders")
// public class Order {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Integer id;

// @ManyToOne
// @JoinColumn(name = "user_id", nullable = false)
// private User user;

// @Column(name = "order_date", nullable = false)
// @Temporal(TemporalType.TIMESTAMP)
// private Date orderDate;

// @ManyToOne
// @JoinColumn(name = "shipping_address_id", nullable = false)
// private Address shippingAddress;

// @ManyToOne
// @JoinColumn(name = "billing_address_id", nullable = false)
// private Address billingAddress;

// @Column(name = "total_price", nullable = false)
// private double totalPrice;

// @Enumerated(EnumType.STRING)
// @Column(name = "status", nullable = false)
// private OrderStatus status;

// @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval =
// true)
// private List<CartItem> orderItems = new ArrayList<>();

// public Order() {
// }

// public Order(User user, Date orderDate, Address shippingAddress, Address
// billingAddress, double totalPrice,
// OrderStatus status) {
// this.user = user;
// this.orderDate = orderDate;
// this.shippingAddress = shippingAddress;
// this.billingAddress = billingAddress;
// this.totalPrice = totalPrice;
// this.status = status;
// }

// public Integer getId() {
// return id;
// }

// public void setId(Integer id) {
// this.id = id;
// }

// public User getUser() {
// return user;
// }

// public void setUser(User user) {
// this.user = user;
// }

// public Date getOrderDate() {
// return orderDate;
// }

// public void setOrderDate(Date orderDate) {
// this.orderDate = orderDate;
// }

// public Address getShippingAddress() {
// return shippingAddress;
// }

// public void setShippingAddress(Address shippingAddress) {
// this.shippingAddress = shippingAddress;
// }

// public Address getBillingAddress() {
// return billingAddress;
// }

// public void setBillingAddress(Address billingAddress) {
// this.billingAddress = billingAddress;
// }

// public double getTotalPrice() {
// return totalPrice;
// }

// public void setTotalPrice(double totalPrice) {
// this.totalPrice = totalPrice;
// }

// public OrderStatus getStatus() {
// return status;
// }

// public void setStatus(OrderStatus status) {
// this.status = status;
// }

// public List<CartItem> getOrderItems() {
// return orderItems;
// }

// public void setOrderItems(List<CartItem> orderItems) {
// this.orderItems = orderItems;

// // חשוב: לוודא שכל CartItem מקבל הפניה לאובייקט Order
// for (CartItem item : orderItems) {
// item.setOrder(this);
// }
// }

// }