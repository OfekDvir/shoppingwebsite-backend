package com.shoppingwebsite.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "item")

public class Product implements Serializable {

	private static final long serialVersionUID = 5186013952828648626L;

	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer productId;

	@Column(name = "category")
	private String category;

	@Column(name = "description")
	private String description;

	@Column(name = "manufacturer")
	private String manufacturer;

	@NotEmpty(message = "Product Name is mandatory")
	@Column(name = "name")
	private String productName;

	@NotNull(message = "Please provide some price")
	@Min(value = 100, message = "Minimum value should be greater than 100")
	@Column(name = "price")
	private double productPrice;

	@Column(name = "unit")
	private int unitStock;

	@Column(name = "imagepath")
	private String productImage;

	// Getters and Setter

	public Integer getProductId() {
		return productId;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getProductName() {
		return productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public int getUnitStock() {
		return unitStock;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public void setCategory(String productCategory) {
		this.category = productCategory;
	}

	public void setDescription(String productDescription) {
		this.description = productDescription;
	}

	public void setManufacturer(String productManufacturer) {
		this.manufacturer = productManufacturer;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public void setUnitStock(int unitStock) {
		this.unitStock = unitStock;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public boolean isLowStock() {
		return this.unitStock < 5;
	}

	// Constructors
	public Product(Integer productId, String productCategory, String productDescription, String productManufacturer,
			String productName, double productPrice, int unitStock) {
		this.productId = productId;
		this.category = productCategory;
		this.description = productDescription;
		this.manufacturer = productManufacturer;
		this.productName = productName;
		this.productPrice = productPrice;
		this.unitStock = unitStock;
	}

	public Product() {

	}

}