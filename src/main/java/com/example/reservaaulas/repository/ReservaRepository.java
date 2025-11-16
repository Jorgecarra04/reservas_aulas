package com.example.reservaaulas.repository;

import com.example.reservaaulas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Buscar todas las reservas de un aula
    List<Reserva> findByAulaId(Long aulaId);

    // Buscar todas las reservas de un usuario
    List<Reserva> findByUsuarioId(Long usuarioId);

    // Verificar si existe una reserva que solape con los parámetros dados
    // Hechale un vistalo a la consulta por que te puede dar error
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE " +
            "r.aula.id = :aulaId AND " +
            "r.fecha = :fecha AND " +
            "r.tramoHorario.id = :tramoHorarioId AND " +
            "(:reservaId IS NULL OR r.id != :reservaId)")
    boolean existeSolapamiento(
            @Param("aulaId") Long aulaId,
            @Param("fecha") LocalDate fecha,
            @Param("tramoHorarioId") Long tramoHorarioId,
            @Param("reservaId") Long reservaId
    );
}
