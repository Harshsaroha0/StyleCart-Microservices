package com.stylecart.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderResponse {

    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
}
