# JWT Authentication Troubleshooting Guide

## Issue: Not Getting JWT Token in Login Response

### Step 1: Restart the Application
**IMPORTANT:** After adding JWT authentication, you MUST restart the application:

```bash
# Stop the current application (Ctrl+C)
# Then restart:
cd api
mvnw.cmd spring-boot:run
```

### Step 2: Register a NEW User
If you registered a user BEFORE password hashing was added, that user's password is stored in **plain text**. The login will fail because we're now using BCrypt.

**Solution:** Register a NEW user after restarting:

```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "name": "Test User",
  "email": "test@example.com",
  "password": "password123"
}
```

### Step 3: Test Login
After registering a NEW user, test login:

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": "uuid-here",
    "name": "Test User",
    "email": "test@example.com",
    "role": "CUSTOMER"
  }
}
```

### Step 4: Verify JWT Configuration
Check that `application.yml` has JWT configuration:

```yaml
jwt:
  secret: your-secret-key-change-this-in-production-minimum-256-bits-for-hs256-algorithm
  expiration: 86400000 # 24 hours in milliseconds
```

### Common Issues:

#### Issue 1: "Invalid email or password"
**Cause:** User was registered before password hashing was implemented.

**Solution:** Register a new user after restarting the application.

#### Issue 2: Getting User object instead of LoginResponse
**Cause:** Application not restarted after code changes.

**Solution:** Restart the application completely.

#### Issue 3: 401 Unauthorized on protected endpoints
**Cause:** Not sending JWT token in Authorization header.

**Solution:** Include the token:
```
Authorization: Bearer <your-jwt-token>
```

### Testing in Postman:

1. **Register:**
   - Method: POST
   - URL: `http://localhost:8080/api/auth/register`
   - Body (JSON):
     ```json
     {
       "name": "John Doe",
       "email": "john@example.com",
       "password": "password123"
     }
     ```

2. **Login:**
   - Method: POST
   - URL: `http://localhost:8080/api/auth/login`
   - Body (JSON):
     ```json
     {
       "email": "john@example.com",
       "password": "password123"
     }
     ```
   - **Copy the `token` from response**

3. **Use Token:**
   - Method: GET
   - URL: `http://localhost:8080/api/auth/me`
   - Headers:
     - Key: `Authorization`
     - Value: `Bearer <paste-token-here>`

### Verify Application is Running with JWT:

Check the console logs when starting. You should see:
- Spring Security initialized
- No errors about JWT configuration

If you see errors about `jwt.secret` not found, check `application.yml` and `application-dev.yml` both have the JWT configuration.
