package com.shophub.api.service;

import com.shophub.api.model.Order;
import com.shophub.api.model.Payment;
import com.shophub.api.model.enums.PaymentMethod;
import com.shophub.api.model.enums.PaymentStatus;
import com.shophub.api.repository.OrderRepository;
import com.shophub.api.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Payment mockPayment(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Check if payment already exists
        if (order.getPayment() != null) {
            Payment existingPayment = order.getPayment();
            existingPayment.setStatus(PaymentStatus.COMPLETED);
            return paymentRepository.save(existingPayment);
        }

        // Create new payment - always return success
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(PaymentMethod.CREDIT_CARD);
        payment.setStatus(PaymentStatus.COMPLETED);

        return paymentRepository.save(payment);
    }
}
