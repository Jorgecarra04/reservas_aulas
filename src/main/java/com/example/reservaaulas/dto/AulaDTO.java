package com.example.reservaaulas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AulaDTO {
    private Long id;
    private String nombre;
    private Integer capacidad;
    private Boolean esAulaDeOrdenadores;
    private Integer numeroOrdenadores;
}
