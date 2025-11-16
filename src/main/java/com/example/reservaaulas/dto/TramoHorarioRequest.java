package com.example.reservaaulas.dto;

import com.example.reservaaulas.entity.TramoHorario;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramoHorarioRequest {

    @NotNull(message = "El día de la semana es obligatorio")
    private TramoHorario.DiaSemana diaSemana;

    @NotNull(message = "La sesión del día es obligatoria")
    @Min(value = 1, message = "La sesión debe ser al menos 1")
    @Max(value = 10, message = "La sesión no puede superar 10")
    private Integer sesionDia;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "El tipo de tramo es obligatorio")
    private TramoHorario.TipoTramo tipo;
}


