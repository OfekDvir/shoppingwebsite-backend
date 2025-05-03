package com.shoppingwebsite.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "favorite_lists")
public class FavoriteList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToMany
    @JoinTable(name = "favorite_list_items", joinColumns = @JoinColumn(name = "favorite_list_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products;

    public FavoriteList() {
    }

    public FavoriteList(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}