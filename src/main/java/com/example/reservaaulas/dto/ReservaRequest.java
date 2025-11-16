package com.example.reservaaulas.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaRequest {

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser hoy o en el futuro")
    private LocalDate fecha;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(min = 5, max = 500, message = "El motivo debe tener entre 5 y 500 caracteres")
    private String motivo;

    @NotNull(message = "El número de asistentes es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 asistente")
    private Integer numeroAsistentes;

    @NotNull(message = "El ID del aula es obligatorio")
    private Long aulaId;

    @NotNull(message = "El ID del tramo horario es obligatorio")
    private Long tramoHorarioId;
}
