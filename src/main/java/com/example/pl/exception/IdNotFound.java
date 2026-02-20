package com.example.pl.exception;

public class IdNotFound extends RuntimeException{

    public IdNotFound(Long id) {
        super("The id " + id + " cannot be found");
    }
}
