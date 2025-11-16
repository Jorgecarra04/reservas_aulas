package com.example.reservaaulas.security;

import com.example.reservaaulas.entity.Usuario;
import com.example.reservaaulas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Normalizamos el email por seguridad
        String emailLower = email.trim().toLowerCase();

        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(emailLower)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con el email: " + emailLower
                ));

        // Convertir el rol en formato ROLE_XXX para Spring Security
        String roleName = "ROLE_" + usuario.getRole().name();

        // Crear objeto UserDetails
        return User.builder()
                .username(usuario.getEmail()) // el email original
                .password(usuario.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(roleName)))
                .build();
    }
}