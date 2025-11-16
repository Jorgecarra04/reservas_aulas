package com.example.reservaaulas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tramos_horarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TramoHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana diaSemana;

    @Column(nullable = false)
    private Integer sesionDia;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTramo tipo;

    @OneToMany(mappedBy = "tramoHorario", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<Reserva> reservas = new ArrayList<>();

    public enum DiaSemana {
        LUNES, MARTES, MIERCOLES, JUEVES, VIERNES
    }

    public enum TipoTramo {
        LECTIVA,
        RECREO,
        MEDIODIA
    }
}