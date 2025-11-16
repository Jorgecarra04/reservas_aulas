package com.example.reservaaulas.controller;

import com.example.reservaaulas.dto.CambiarPasswordRequest;
import com.example.reservaaulas.dto.UsuarioDTO;
import com.example.reservaaulas.dto.UsuarioUpdateRequest;
import com.example.reservaaulas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest request) {
        UsuarioDTO usuario = usuarioService.updateUsuario(id, request);
        return ResponseEntity.ok(usuario);
    }

    @PatchMapping("/cambiar-pass")
    public ResponseEntity<Map<String, String>> cambiarPassword(
            @Valid @RequestBody CambiarPasswordRequest request) {
        usuarioService.cambiarPassword(request);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Contraseña actualizada correctamente");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}