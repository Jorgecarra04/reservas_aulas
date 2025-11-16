package com.example.reservaaulas.controller;

import com.example.reservaaulas.dto.ReservaDTO;
import com.example.reservaaulas.dto.ReservaRequest;
import com.example.reservaaulas.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> getAllReservas() {
        List<ReservaDTO> reservas = reservaService.getAllReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> getReservaById(@PathVariable Long id) {
        ReservaDTO reserva = reservaService.getReservaById(id);
        return ResponseEntity.ok(reserva);
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> createReserva(@Valid @RequestBody ReservaRequest request) {
        ReservaDTO reserva = reservaService.createReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }
}