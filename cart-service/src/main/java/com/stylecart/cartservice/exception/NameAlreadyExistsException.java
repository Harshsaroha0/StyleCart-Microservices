package com.stylecart.cartservice.exception;


public class NameAlreadyExistsException  extends  RuntimeException{

    public NameAlreadyExistsException(String name) {
        super("name already exists" + name);
    }
}
