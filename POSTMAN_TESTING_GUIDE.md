# Postman Testing Guide for ShopHub API

## Prerequisites

1. **Start the Application**
   ```bash
   cd api
   mvnw.cmd spring-boot:run
   ```
   - Application runs on: `http://localhost:8080`
   - Base URL: `http://localhost:8080/api`

2. **Postman Setup**
   - Create a new Collection: "ShopHub API Tests"
   - Set Collection Variable: `baseUrl` = `http://localhost:8080/api`
   - Use `{{baseUrl}}` in all requests

---

## Testing Order (Important!)

Test APIs in this order since some depend on others:
1. **Auth** → Register/Login to get userId
2. **Catalog** → Browse products (optional, but helpful)
3. **Cart** → Add items to cart (requires userId and productId)
4. **Checkout** → Place order (requires cart with items)
5. **Payment** → Mock payment (optional)
6. **Orders** → View orders (requires userId)

---

## 1️⃣ AUTH APIs

### 1.1 Register User
**POST** `{{baseUrl}}/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Expected Response:** `201 Created`
```json
{
  "id": "uuid-here",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "role": "CUSTOMER",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

**💡 Save the `id` from response as variable `userId` for later requests**

---

### 1.2 Login
**POST** `{{baseUrl}}/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Expected Response:** `200 OK`
```json
{
  "id": "uuid-here",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "role": "CUSTOMER"
}
```

---

### 1.3 Get Current User
**GET** `{{baseUrl}}/auth/me?userId={{userId}}`

**Expected Response:** `200 OK`
```json
{
  "id": "uuid-here",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "role": "CUSTOMER"
}
```

---

## 2️⃣ CATALOG APIs

### 2.1 Get All Products
**GET** `{{baseUrl}}/catalog`

**Expected Response:** `200 OK`
```json
[
  {
    "id": "uuid-here",
    "name": "Product Name",
    "description": "Product description",
    "price": 99.99,
    "imageUrl": "https://example.com/image.jpg",
    "category": {
      "id": "uuid-here",
      "name": "Electronics"
    },
    "inventory": {
      "id": "uuid-here",
      "stock": 100,
      "reservedStock": 0,
      "lowStockThreshold": 10
    }
  }
]
```

**💡 Note:** If empty, you'll need to create products first (via Admin API or directly in database)

---

### 2.2 Get Product by ID
**GET** `{{baseUrl}}/catalog/{{productId}}`

**Expected Response:** `200 OK` (same structure as above)

---

### 2.3 Search Products
**GET** `{{baseUrl}}/catalog/search?query=laptop`

**Expected Response:** `200 OK` (array of matching products)

---

### 2.4 Filter Products
**GET** `{{baseUrl}}/catalog/filter?category=Electronics&minPrice=50&maxPrice=200`

**Query Parameters:**
- `category` (optional): Category name
- `minPrice` (optional): Minimum price
- `maxPrice` (optional): Maximum price

**Expected Response:** `200 OK` (array of filtered products)

---

## 3️⃣ CART APIs

### 3.1 Get User's Cart
**GET** `{{baseUrl}}/users/{{userId}}/cart`

**Expected Response:** `200 OK` or `404 Not Found` (if cart doesn't exist yet)
```json
{
  "id": "uuid-here",
  "totalAmount": 199.98,
  "user": {
    "id": "uuid-here"
  },
  "cartItems": [
    {
      "id": "uuid-here",
      "quantity": 2,
      "priceAtTime": 99.99,
      "product": {
        "id": "uuid-here",
        "name": "Product Name",
        "price": 99.99
      }
    }
  ]
}
```

---

### 3.2 Add Item to Cart
**POST** `{{baseUrl}}/users/{{userId}}/cart/items`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "productId": "product-uuid-here",
  "quantity": 2
}
```

**Expected Response:** `200 OK` (cart with updated items)

**💡 Note:** If item already exists, quantity will be increased

---

### 3.3 Update Cart Item Quantity
**PUT** `{{baseUrl}}/users/{{userId}}/cart/items/{{itemId}}`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "quantity": 5
}
```

**Expected Response:** `200 OK` (updated cart)

**💡 Note:** If quantity is 0 or less, item will be removed

---

### 3.4 Remove Item from Cart
**DELETE** `{{baseUrl}}/users/{{userId}}/cart/items/{{itemId}}`

**Expected Response:** `204 No Content`

---

## 4️⃣ CHECKOUT API

### 4.1 Checkout (Place Order)
**POST** `{{baseUrl}}/users/{{userId}}/checkout`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "shippingAddress": "123 Main St, City, State, ZIP",
  "contactDetails": "john.doe@example.com, +1234567890",
  "paymentMethod": "CREDIT_CARD"
}
```

**Payment Methods:** `CREDIT_CARD`, `DEBIT_CARD`, `PAYPAL`, `BANK_TRANSFER`, `CASH_ON_DELIVERY`

**Expected Response:** `201 Created`
```json
{
  "id": "order-uuid-here",
  "totalAmount": 199.98,
  "status": "PROCESSING",
  "user": {
    "id": "uuid-here"
  },
  "orderItems": [
    {
      "id": "uuid-here",
      "quantity": 2,
      "priceAtPurchase": 99.99,
      "product": {
        "id": "uuid-here",
        "name": "Product Name"
      }
    }
  ],
  "payment": {
    "id": "uuid-here",
    "amount": 199.98,
    "method": "CREDIT_CARD",
    "status": "COMPLETED"
  }
}
```

**💡 Save the `id` from response as variable `orderId` for later requests**

---

## 5️⃣ PAYMENT API

### 5.1 Mock Payment
**POST** `{{baseUrl}}/payments/mock`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "orderId": "order-uuid-here",
  "amount": 199.98
}
```

**Expected Response:** `200 OK`
```json
{
  "status": "SUCCESS",
  "message": "Payment processed successfully",
  "transactionId": "uuid-here"
}
```

---

## 6️⃣ ORDER APIs

### 6.1 Get User's Orders
**GET** `{{baseUrl}}/users/{{userId}}/orders`

**Expected Response:** `200 OK`
```json
[
  {
    "id": "uuid-here",
    "totalAmount": 199.98,
    "status": "PROCESSING",
    "orderItems": [...],
    "payment": {...}
  }
]
```

---

### 6.2 Get Order by ID
**GET** `{{baseUrl}}/orders/{{orderId}}`

**Expected Response:** `200 OK` (order details)

---

### 6.3 Get Order Status
**GET** `{{baseUrl}}/orders/{{orderId}}/status`

**Expected Response:** `200 OK`
```json
{
  "status": "PROCESSING"
}
```

**Order Statuses:** `PENDING`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`

---

## 📝 Complete Test Flow Example

### Step-by-Step Workflow:

1. **Register User**
   ```
   POST /api/auth/register
   → Save userId from response
   ```

2. **Get All Products** (to find a productId)
   ```
   GET /api/catalog
   → Save productId from response
   ```

3. **Add Item to Cart**
   ```
   POST /api/users/{userId}/cart/items
   Body: { "productId": "...", "quantity": 2 }
   → Save itemId from response
   ```

4. **Get Cart** (verify items)
   ```
   GET /api/users/{userId}/cart
   ```

5. **Checkout**
   ```
   POST /api/users/{userId}/checkout
   Body: { "shippingAddress": "...", "contactDetails": "...", "paymentMethod": "CREDIT_CARD" }
   → Save orderId from response
   ```

6. **Get Orders**
   ```
   GET /api/users/{userId}/orders
   ```

7. **Get Order Status**
   ```
   GET /api/orders/{orderId}/status
   ```

---

## 🐛 Troubleshooting

### Empty Product List
- **Issue:** `GET /api/catalog` returns empty array
- **Solution:** Products need to be created first. You can:
  - Use Admin APIs (if implemented)
  - Insert directly into H2 database
  - Use SQL scripts

### Cart Not Found (404)
- **Issue:** `GET /api/users/{userId}/cart` returns 404
- **Solution:** This is normal if cart doesn't exist yet. Add an item first, and cart will be created automatically.

### Insufficient Stock Error
- **Issue:** Checkout fails with "Insufficient stock"
- **Solution:** Ensure products have inventory with stock > 0. Check inventory via product details.

### User Not Found
- **Issue:** Cart/Order operations fail with "User not found"
- **Solution:** Make sure you're using the correct userId from registration response.

---

## 🔧 Postman Environment Variables

Create a Postman Environment with:
- `baseUrl` = `http://localhost:8080/api`
- `userId` = (set after registration)
- `productId` = (set after getting products)
- `itemId` = (set after adding to cart)
- `orderId` = (set after checkout)

---

## 📊 Expected Status Codes

- `200 OK` - Successful GET/PUT requests
- `201 Created` - Successful POST (register, checkout)
- `204 No Content` - Successful DELETE
- `400 Bad Request` - Invalid request body/parameters
- `401 Unauthorized` - Invalid login credentials
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## 💡 Tips

1. **Use Postman Collections:** Organize all requests in a collection
2. **Use Variables:** Store userId, productId, etc. as variables
3. **Test Scripts:** Add scripts to auto-save IDs from responses
4. **H2 Console:** Access `http://localhost:8080/h2-console` to view database
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (empty)

---

## 🎯 Quick Test Script for Postman

Add this to your Postman request's "Tests" tab to auto-save userId:

```javascript
// For Register/Login - Save userId
if (pm.response.code === 201 || pm.response.code === 200) {
    const jsonData = pm.response.json();
    if (jsonData.id) {
        pm.environment.set("userId", jsonData.id);
    }
}

// For Add to Cart - Save itemId
if (pm.response.code === 200) {
    const jsonData = pm.response.json();
    if (jsonData.cartItems && jsonData.cartItems.length > 0) {
        pm.environment.set("itemId", jsonData.cartItems[0].id);
    }
}

// For Checkout - Save orderId
if (pm.response.code === 201) {
    const jsonData = pm.response.json();
    if (jsonData.id) {
        pm.environment.set("orderId", jsonData.id);
    }
}
```

---

Happy Testing! 🚀
