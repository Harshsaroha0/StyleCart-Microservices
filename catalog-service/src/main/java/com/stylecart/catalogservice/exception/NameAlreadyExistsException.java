package com.stylecart.catalogservice.exception;


public class NameAlreadyExistsException  extends  RuntimeException{

    public NameAlreadyExistsException(String name) {
        super("name already exists" + name);
    }
}
