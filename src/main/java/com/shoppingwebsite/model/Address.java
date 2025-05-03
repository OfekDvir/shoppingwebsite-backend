package com.shoppingwebsite.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String address;
    private String city;
    private String country;
    private String zipcode;
    private String state;

    public Address() {
    }

    public Address(Address other) {
        if (other != null) {
            this.address = other.getAddress();
            this.city = other.getCity();
            this.zipcode = other.getZipcode();
            this.country = other.getCountry();
            this.state = other.getState();
        }
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getState() {
        return state;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return id + " " + address;
    }

    // --- equals() and hashCode() based on address fields (not id) ---
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Address))
            return false;
        Address that = (Address) o;
        return Objects.equals(address, that.address) &&
                Objects.equals(city, that.city) &&
                Objects.equals(zipcode, that.zipcode) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, city, zipcode, country);
    }
}
