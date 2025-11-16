package com.example.reservaaulas.exception;

//Cuando no se tiene permiso
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String mensaje) {
        super(mensaje);
    }
}