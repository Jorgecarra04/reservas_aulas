package com.example.reservaaulas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AulaRequest {

    @NotBlank(message = "El nombre del aula es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Max(value = 500, message = "La capacidad no puede superar 500")
    private Integer capacidad;

    @NotNull(message = "Debes indicar si es aula de ordenadores")
    private Boolean esAulaDeOrdenadores;

    @Min(value = 0, message = "El número de ordenadores no puede ser negativo")
    private Integer numeroOrdenadores;
}

