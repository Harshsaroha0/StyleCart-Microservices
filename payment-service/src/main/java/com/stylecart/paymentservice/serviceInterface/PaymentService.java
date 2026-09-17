package com.stylecart.paymentservice.serviceInterface;

import com.stylecart.paymentservice.dto.PaymentRequest;
import com.stylecart.paymentservice.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    PaymentResponse getPaymentByOrderId(Long orderId);
    List<PaymentResponse> getPaymentByUserId(Long userId);
}
