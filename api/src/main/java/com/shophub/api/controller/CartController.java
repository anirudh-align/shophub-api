package com.shophub.api.controller;

import com.shophub.api.model.Cart;
import com.shophub.api.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable UUID userId) {
        return cartService.getCartByUserId(userId)
                .map(cart -> ResponseEntity.ok(cart))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(
            @RequestParam UUID userId,
            @RequestParam UUID productId,
            @RequestParam int quantity) {
        try {
            Cart cart = cartService.addItemToCart(userId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable UUID cartItemId) {
        try {
            cartService.removeItemFromCart(cartItemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
