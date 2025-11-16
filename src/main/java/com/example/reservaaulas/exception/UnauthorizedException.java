package com.example.reservaaulas.exception;

//Cuando no hay autorización
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String mensaje) {
        super(mensaje);
    }
}
