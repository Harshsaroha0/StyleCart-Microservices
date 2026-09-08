package com.stylecart.inventoryservice.exception;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int status;

    private String error;

    private String message;

    private String path;

    private String timestamp;
}