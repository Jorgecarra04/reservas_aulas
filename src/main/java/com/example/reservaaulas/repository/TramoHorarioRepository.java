package com.example.reservaaulas.repository;

import com.example.reservaaulas.entity.TramoHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TramoHorarioRepository extends JpaRepository<TramoHorario, Long> {

    // Buscar tramos por día de la semana
    List<TramoHorario> findByDiaSemana(TramoHorario.DiaSemana diaSemana);
}
