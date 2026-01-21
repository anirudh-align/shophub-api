package com.shophub.api.controller;

import com.shophub.api.model.Cart;
import com.shophub.api.model.CartItem;
import com.shophub.api.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(@PathVariable UUID userId) {
        return cartService.getCartByUserId(userId)
                .map(cart -> ResponseEntity.ok(cart))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(
            @PathVariable UUID userId,
            @RequestBody Map<String, Object> request) {
        try {
            UUID productId = UUID.fromString(request.get("productId").toString());
            int quantity = Integer.parseInt(request.get("quantity").toString());

            Cart cart = cartService.addItemToCart(userId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Cart> updateCartItem(
            @PathVariable UUID userId,
            @PathVariable UUID itemId,
            @RequestBody Map<String, Object> request) {
        try {
            int quantity = Integer.parseInt(request.get("quantity").toString());

            Cart cart = cartService.updateCartItemQuantity(userId, itemId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable UUID userId,
            @PathVariable UUID itemId) {
        try {
            cartService.removeItemFromCart(itemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
