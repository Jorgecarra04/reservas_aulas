package com.example.reservaaulas.service;

import com.example.reservaaulas.dto.*;
import com.example.reservaaulas.entity.Usuario;
import com.example.reservaaulas.exception.BadRequestException;
import com.example.reservaaulas.exception.UnauthorizedException;
import com.example.reservaaulas.mapper.Mapper;
import com.example.reservaaulas.repository.UsuarioRepository;
import com.example.reservaaulas.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final Mapper mapper;

    //Registra un nuevo usuario
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        // Crear el usuario
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Encriptar password
                .role(request.getRole())
                .build();

        // Guardar en la base de datos
        usuario = usuarioRepository.save(usuario);

        // Generar token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtService.generateToken(userDetails);

        // Devolver respuesta con el token
        return AuthResponse.builder()
                .token(token)
                .usuario(mapper.toUsuarioDTO(usuario))
                .build();
    }

    //Inicia sesión con email y password
    public AuthResponse login(LoginRequest request) {

        try {
            // Intentar autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Si llegamos aquí, las credenciales son correctas
            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UnauthorizedException("Credenciales incorrectas"));

            // Generar token JWT
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            // Devolver respuesta con el token
            return AuthResponse.builder()
                    .token(token)
                    .usuario(mapper.toUsuarioDTO(usuario))
                    .build();

        } catch (Exception e) {
            throw new UnauthorizedException("Credenciales incorrectas");
        }
    }
}