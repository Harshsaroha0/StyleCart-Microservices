package com.stylecart.paymentservice.serviceImpl;

import com.stylecart.paymentservice.dto.OrderResponse;
import com.stylecart.paymentservice.dto.PaymentRequest;
import com.stylecart.paymentservice.exception.PaymentValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentOrderValidator {

    public void validate(PaymentRequest request, OrderResponse order) {

        if (order == null) {
            throw new PaymentValidationException(
                    "Order not found"
            );
        }

        if (!request.getOrderId().equals(order.getId())) {
            throw new PaymentValidationException(
                    "Invalid order"
            );
        }

        if (!request.getUserId().equals(order.getUserId())) {
            throw new PaymentValidationException(
                    "Order does not belong to this user"
            );
        }

        if (order.getTotalAmount() == null ||
                order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new PaymentValidationException(
                    "Invalid order amount"
            );
        }

        if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new PaymentValidationException(
                    "Payment is not allowed for order status: "
                            + order.getStatus()
            );
        }
    }
}
