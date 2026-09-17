package com.stylecart.paymentservice.serviceImpl;

import com.stylecart.paymentservice.client.OrderClient;
import com.stylecart.paymentservice.dto.OrderResponse;
import com.stylecart.paymentservice.dto.PaymentRequest;
import com.stylecart.paymentservice.dto.PaymentResponse;
import com.stylecart.paymentservice.entity.Payment;
import com.stylecart.paymentservice.enums.PaymentStatus;
import com.stylecart.paymentservice.exception.PaymentNotFoundException;
import com.stylecart.paymentservice.repository.PaymentRepository;
import com.stylecart.paymentservice.serviceInterface.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final PaymentOrderValidator paymentOrderValidator;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        OrderResponse order = orderClient.getOrderById(request.getOrderId());

        paymentOrderValidator.validate(request, order);

        Payment payment = new Payment();

        payment.setOrderId(order.getId());
        payment.setUserId(order.getUserId());
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId(null);

        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponse(savedPayment);

    }

    @Override
    public PaymentResponse getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                "Payment not found with id: " + id
                        )
                );

        return mapToResponse(payment);

    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                "Payment not found with id: " + orderId
                        )
                );

        return mapToResponse(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentByUserId(Long userId) {

        List<Payment> payments = paymentRepository.findByUserId(userId);

        return payments.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PaymentResponse mapToResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setOrderId(payment.getOrderId());
        response.setUserId(payment.getUserId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setTransactionId(payment.getTransactionId());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());

        return response;
    }
}
