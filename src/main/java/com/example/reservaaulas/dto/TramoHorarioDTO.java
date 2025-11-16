package com.example.reservaaulas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramoHorarioDTO {
    private Long id;
    private String diaSemana;
    private Integer sesionDia;
    private String horaInicio;
    private String horaFin;
    private String tipo;
}
