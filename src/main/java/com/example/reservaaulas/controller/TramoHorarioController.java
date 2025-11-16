package com.example.reservaaulas.controller;

import com.example.reservaaulas.dto.TramoHorarioDTO;
import com.example.reservaaulas.dto.TramoHorarioRequest;
import com.example.reservaaulas.service.TramoHorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tramo-horario")
@RequiredArgsConstructor
public class TramoHorarioController {

    private final TramoHorarioService tramoHorarioService;

    @GetMapping
    public ResponseEntity<List<TramoHorarioDTO>> getAllTramosHorarios() {
        List<TramoHorarioDTO> tramos = tramoHorarioService.getAllTramosHorarios();
        return ResponseEntity.ok(tramos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TramoHorarioDTO> getTramoHorarioById(@PathVariable Long id) {
        TramoHorarioDTO tramo = tramoHorarioService.getTramoHorarioById(id);
        return ResponseEntity.ok(tramo);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TramoHorarioDTO> createTramoHorario(
            @Valid @RequestBody TramoHorarioRequest request) {
        TramoHorarioDTO tramo = tramoHorarioService.createTramoHorario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tramo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTramoHorario(@PathVariable Long id) {
        tramoHorarioService.deleteTramoHorario(id);
        return ResponseEntity.noContent().build();
    }
}