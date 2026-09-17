package com.stylecart.paymentservice.dto;

import com.stylecart.paymentservice.enums.PaymentMethod;
import com.stylecart.paymentservice.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
