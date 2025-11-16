package com.example.reservaaulas.controller;

import com.example.reservaaulas.dto.AuthResponse;
import com.example.reservaaulas.dto.LoginRequest;
import com.example.reservaaulas.dto.RegisterRequest;
import com.example.reservaaulas.dto.UsuarioDTO;
import com.example.reservaaulas.service.AuthService;
import com.example.reservaaulas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> getPerfil() {
        UsuarioDTO usuario = usuarioService.getPerfil();
        return ResponseEntity.ok(usuario);
    }
}