package com.stylecart.cartservice.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
    }


    @ExceptionHandler(NameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleNameExists (
            NameAlreadyExistsException ex , HttpServletRequest request) {

        return buildResponse(HttpStatus.CONFLICT,
                "Name Already Exists" ,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleStock (
            InsufficientStockException ex , HttpServletRequest request) {

        return buildResponse(HttpStatus.BAD_REQUEST,
                "stock insufficient" ,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(
            FeignException ex) {

        HttpStatus status;

        try {
            status = HttpStatus.valueOf(ex.status());
        } catch (IllegalArgumentException e) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        }

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatus(status.value());
        errorResponse.setMessage(
                "Unable to communicate with required service"
        );

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }


    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String message,
            String path) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
