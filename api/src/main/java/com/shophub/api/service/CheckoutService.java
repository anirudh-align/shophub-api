package com.shophub.api.service;

import com.shophub.api.model.*;
import com.shophub.api.model.enums.OrderStatus;
import com.shophub.api.model.enums.PaymentMethod;
import com.shophub.api.model.enums.PaymentStatus;
import com.shophub.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;
    private final PaymentRepository paymentRepository;

    public CheckoutService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            InventoryService inventoryService,
            PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.inventoryService = inventoryService;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Order checkout(UUID userId, String shippingAddress, String contactDetails, PaymentMethod paymentMethod) {
        // Get user's cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout: Cart is empty");
        }

        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);
        // TODO: Store shipping address and contact details in order
        order = orderRepository.save(order);

        // Convert cart items to order items
        for (CartItem cartItem : cart.getCartItems()) {
            // Reduce inventory stock
            inventoryService.reduceStock(cartItem.getProduct().getId(), cartItem.getQuantity());

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getPriceAtTime());
            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        }

        // Create payment (mock - always success)
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(paymentMethod);
        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);

        // Update order status
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        // Clear cart
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);

        return order;
    }
}
