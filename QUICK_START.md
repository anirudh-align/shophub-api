# Quick Start - Postman Testing

## 🚀 Start the Application

```bash
cd api
mvnw.cmd spring-boot:run
```

Wait for: `Started ShophubApiApplication in X.XXX seconds`

---

## 📋 Postman Setup (5 minutes)

### 1. Create Environment
- Click **Environments** → **+** → Name: "ShopHub Local"
- Add variables:
  - `baseUrl` = `http://localhost:8080/api`
  - `userId` = (leave empty, will be set automatically)
  - `productId` = (leave empty)
  - `orderId` = (leave empty)

### 2. Import/Test in Order

#### ✅ Step 1: Register User
```
POST {{baseUrl}}/auth/register
Body (JSON):
{
  "name": "Test User",
  "email": "test@example.com",
  "password": "test123"
}
```
**→ Copy `id` from response → Set as `userId` variable**

---

#### ✅ Step 2: Browse Products
```
GET {{baseUrl}}/catalog
```
**→ Copy a `productId` from response → Set as `productId` variable**

---

#### ✅ Step 3: Add to Cart
```
POST {{baseUrl}}/users/{{userId}}/cart/items
Body (JSON):
{
  "productId": "{{productId}}",
  "quantity": 2
}
```

---

#### ✅ Step 4: View Cart
```
GET {{baseUrl}}/users/{{userId}}/cart
```

---

#### ✅ Step 5: Checkout
```
POST {{baseUrl}}/users/{{userId}}/checkout
Body (JSON):
{
  "shippingAddress": "123 Main St, City, State 12345",
  "contactDetails": "test@example.com, +1234567890",
  "paymentMethod": "CREDIT_CARD"
}
```
**→ Copy `id` from response → Set as `orderId` variable**

---

#### ✅ Step 6: View Orders
```
GET {{baseUrl}}/users/{{userId}}/orders
```

---

#### ✅ Step 7: Check Order Status
```
GET {{baseUrl}}/orders/{{orderId}}/status
```

---

## 🎯 All Endpoints Summary

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login user |
| GET | `/api/auth/me?userId={id}` | Get current user |
| GET | `/api/catalog` | List all products |
| GET | `/api/catalog/{id}` | Get product details |
| GET | `/api/catalog/search?query=...` | Search products |
| GET | `/api/catalog/filter?category=...&minPrice=...&maxPrice=...` | Filter products |
| GET | `/api/users/{userId}/cart` | Get user's cart |
| POST | `/api/users/{userId}/cart/items` | Add item to cart |
| PUT | `/api/users/{userId}/cart/items/{itemId}` | Update item quantity |
| DELETE | `/api/users/{userId}/cart/items/{itemId}` | Remove item |
| POST | `/api/users/{userId}/checkout` | Place order |
| POST | `/api/payments/mock` | Mock payment |
| GET | `/api/users/{userId}/orders` | List user orders |
| GET | `/api/orders/{orderId}` | Get order details |
| GET | `/api/orders/{orderId}/status` | Get order status |

---

## 🔍 View Database (Optional)

1. Open browser: `http://localhost:8080/h2-console`
2. JDBC URL: `jdbc:h2:mem:shophubdb`
3. Username: `sa`
4. Password: (leave empty)
5. Click **Connect**

---

## ⚠️ Common Issues

**No products?** → Check H2 console, or restart app (data.sql should load)

**Cart 404?** → Normal if cart doesn't exist. Add item first.

**User not found?** → Make sure `userId` variable is set correctly.

---

For detailed guide, see: `POSTMAN_TESTING_GUIDE.md`
