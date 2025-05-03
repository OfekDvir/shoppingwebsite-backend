# 🛒 Shopping Website - Backend

This is the backend part of a full-stack e-commerce application.  
It handles all the business logic, database operations, and API endpoints for product management, user authentication, cart, orders, and favorites.

## ✨ Features

- 🔐 User registration and login with role-based access (Admin/User)
- 🛍️ Product CRUD operations (Admin only)
- 🛒 Shopping cart for each user
- ❤️ Favorites management
- 📦 Order creation and tracking (TEMP and DOONE status)
- 🧾 Address and billing handling

## 🧠 Project Logic

- Each user can manage their own cart and place orders.
- Admins can add/edit/delete products.
- Orders are stored with linked CartItems and shipping details.
- Products are loaded with stock control (unitStock).
- Favorite products are stored separately per user.

## 🧰 Technology Stack

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Hibernate (JPA)**
- **MySQL**
- **RESTful APIs**
- **Maven**

## 🛠️ Run Locally

```bash
git clone https://github.com/OfekDvir/shoppingwebsite-backend.git
cd shoppingwebsite-backend
./mvnw spring-boot:run
