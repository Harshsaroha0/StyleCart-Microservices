package com.stylecart.catalogservice.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName,
                                     String fieldName,
                                     Object fieldValue) {

        super(resourceName + " not found with "
                + fieldName + " = " + fieldValue);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}