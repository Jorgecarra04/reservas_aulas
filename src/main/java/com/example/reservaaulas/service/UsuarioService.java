package com.example.reservaaulas.service;

import com.example.reservaaulas.dto.CambiarPasswordRequest;
import com.example.reservaaulas.dto.UsuarioDTO;
import com.example.reservaaulas.dto.UsuarioUpdateRequest;
import com.example.reservaaulas.entity.Usuario;
import com.example.reservaaulas.exception.BadRequestException;
import com.example.reservaaulas.exception.ForbiddenException;
import com.example.reservaaulas.exception.ResourceNotFoundException;
import com.example.reservaaulas.mapper.Mapper;
import com.example.reservaaulas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    //Obtiene el perfil del usuario autenticado
    public UsuarioDTO getPerfil() {
        Usuario usuario = getUsuarioAutenticado();
        return mapper.toUsuarioDTO(usuario);
    }

    //Actualiza los datos de un usuario
    // No te hace falta el transaccional, solo usalo para cuando
    // accedas a referencias de la entidad ejemplo reserva.getAula
    @Transactional
    public UsuarioDTO updateUsuario(Long id, UsuarioUpdateRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + id
                ));

        Usuario usuarioActual = getUsuarioAutenticado();

        // ADMIN puede modificar cualquier usuario
        // PROFESOR solo puede modificar su propio perfil
        boolean esAdmin = usuarioActual.getRole() == Usuario.Role.ROLE_ADMIN;
        boolean esSuPerfil = usuario.getId().equals(usuarioActual.getId());

        if (!esAdmin && !esSuPerfil) {
            throw new ForbiddenException(
                    "No tienes permiso para modificar este usuario"
            );
        }

        // Verificar email duplicado (excepto el actual)
        if (!usuario.getEmail().equals(request.getEmail()) &&
                usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está en uso");
        }

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario = usuarioRepository.save(usuario);

        return mapper.toUsuarioDTO(usuario);
    }

    //Cambia la contraseña del usuario
    @Transactional
    public void cambiarPassword(CambiarPasswordRequest request) {

        Usuario usuario = getUsuarioAutenticado();

        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            throw new BadRequestException("La contraseña actual es incorrecta");
        }

        // Actualizar la contraseña
        usuario.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
        usuarioRepository.save(usuario);
    }

    //(Elimina un usuario
    @Transactional
    public void deleteUsuario(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }

        Usuario usuarioActual = getUsuarioAutenticado();

        // Solo ADMIN puede eliminar usuarios
        if (usuarioActual.getRole() != Usuario.Role.ROLE_ADMIN) {
            throw new ForbiddenException(
                    "No tienes permiso para eliminar usuarios"
            );
        }

        // No permitir que se elimine a sí mismo
        // Muy bien pensado
        if (id.equals(usuarioActual.getId())) {
            throw new BadRequestException("No puedes eliminar tu propio usuario");
        }

        usuarioRepository.deleteById(id);
    }

    //Obtiene el usuario autenticado actual
    // Por favor no uses transaccional si no estas accediendo a la referencia
    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado"
                ));
    }
}