package com.example.reservaaulas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aulas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    @Builder.Default
    private Boolean esAulaDeOrdenadores = false;

    private Integer numeroOrdenadores;

    @OneToMany(mappedBy = "aula", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<Reserva> reservas = new ArrayList<>();
}