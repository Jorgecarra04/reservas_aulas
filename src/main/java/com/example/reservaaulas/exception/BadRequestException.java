package com.example.reservaaulas.exception;

//Cuando la petición tiene datos incorrectos
public class BadRequestException extends RuntimeException {
    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}
