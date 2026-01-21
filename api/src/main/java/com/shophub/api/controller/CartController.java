package com.shophub.api.controller;

import com.shophub.api.dto.AddCartItemRequest;
import com.shophub.api.dto.UpdateCartItemRequest;
import com.shophub.api.exception.ResourceNotFoundException;
import com.shophub.api.model.Cart;
import com.shophub.api.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Cart cart = cartService.getCartByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(
            @PathVariable UUID userId,
            @Valid @RequestBody AddCartItemRequest request) {
        Cart cart = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Cart> updateCartItem(
            @PathVariable UUID userId,
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        Cart cart = cartService.updateCartItemQuantity(userId, itemId, request.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable UUID userId,
            @PathVariable UUID itemId) {
        cartService.removeItemFromCart(itemId);
        return ResponseEntity.noContent().build();
    }
}
