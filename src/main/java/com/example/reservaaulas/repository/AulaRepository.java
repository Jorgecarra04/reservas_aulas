package com.example.reservaaulas.repository;

import com.example.reservaaulas.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {

    // Buscar aulas con capacidad mayor o igual a un valor
    List<Aula> findByCapacidadGreaterThanEqual(Integer capacidad);

    // Buscar aulas que tengan ordenadores
    List<Aula> findByEsAulaDeOrdenadoresTrue();

    // Verificar si existe un nombre de aula
    boolean existsByNombre(String nombre);
}
