package com.stylecart.paymentservice.dto;

import com.stylecart.paymentservice.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private Long userId;

    @NotNull
    private PaymentMethod paymentMethod;
}
