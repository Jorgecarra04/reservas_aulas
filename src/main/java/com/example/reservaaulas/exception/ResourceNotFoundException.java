package com.example.reservaaulas.exception;

//Cuando un recurso no se encuentra
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}