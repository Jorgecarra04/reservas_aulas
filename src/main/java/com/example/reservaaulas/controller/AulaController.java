package com.example.reservaaulas.controller;

import com.example.reservaaulas.dto.AulaDTO;
import com.example.reservaaulas.dto.AulaRequest;
import com.example.reservaaulas.dto.ReservaDTO;
import com.example.reservaaulas.service.AulaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/aulas")
@RequiredArgsConstructor
public class AulaController {

    private final AulaService aulaService;

    @GetMapping
    public ResponseEntity<List<AulaDTO>> getAllAulas(
            @RequestParam(required = false) Integer capacidad,
            @RequestParam(required = false) Boolean ordenadores) {

        List<AulaDTO> aulas;

        // Filtrar por capacidad si se especifica
        if (capacidad != null) {
            aulas = aulaService.getAulasByCapacidad(capacidad);
        }
        // Filtrar por ordenadores si se especifica
        else if (ordenadores != null && ordenadores) {
            aulas = aulaService.getAulasConOrdenadores();
        }
        // Sin filtros, devolver todas
        else {
            aulas = aulaService.getAllAulas();
        }

        return ResponseEntity.ok(aulas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AulaDTO> getAulaById(@PathVariable Long id) {
        AulaDTO aula = aulaService.getAulaById(id);
        return ResponseEntity.ok(aula);
    }

    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<ReservaDTO>> getReservasByAula(@PathVariable Long id) {
        List<ReservaDTO> reservas = aulaService.getReservasByAula(id);
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AulaDTO> createAula(@Valid @RequestBody AulaRequest request) {
        AulaDTO aula = aulaService.createAula(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(aula);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AulaDTO> updateAula(
            @PathVariable Long id,
            @Valid @RequestBody AulaRequest request) {
        AulaDTO aula = aulaService.updateAula(id, request);
        return ResponseEntity.ok(aula);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAula(@PathVariable Long id) {
        aulaService.deleteAula(id);
        return ResponseEntity.noContent().build();
    }
}