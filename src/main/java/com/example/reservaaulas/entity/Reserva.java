package com.example.reservaaulas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(nullable = false)
    private Integer numeroAsistentes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tramo_horario_id", nullable = false)
    private TramoHorario tramoHorario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}