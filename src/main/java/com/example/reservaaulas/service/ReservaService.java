package com.example.reservaaulas.service;

import com.example.reservaaulas.dto.ReservaDTO;
import com.example.reservaaulas.dto.ReservaRequest;
import com.example.reservaaulas.entity.Aula;
import com.example.reservaaulas.entity.Reserva;
import com.example.reservaaulas.entity.TramoHorario;
import com.example.reservaaulas.entity.Usuario;
import com.example.reservaaulas.exception.BadRequestException;
import com.example.reservaaulas.exception.ForbiddenException;
import com.example.reservaaulas.exception.ResourceNotFoundException;
import com.example.reservaaulas.mapper.Mapper;
import com.example.reservaaulas.repository.AulaRepository;
import com.example.reservaaulas.repository.ReservaRepository;
import com.example.reservaaulas.repository.TramoHorarioRepository;
import com.example.reservaaulas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final AulaRepository aulaRepository;
    private final TramoHorarioRepository tramoHorarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final Mapper mapper;

    //Obtiene todas las reservas
    public List<ReservaDTO> getAllReservas() {
        return reservaRepository.findAll()
                .stream()
                .map(mapper::toReservaDTO)
                .collect(Collectors.toList());
    }

    //Obtiene una reserva por ID
    public ReservaDTO getReservaById(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con ID: " + id
                ));
        return mapper.toReservaDTO(reserva);
    }

    //Crea una nueva reserva
    @Transactional
    public ReservaDTO createReserva(ReservaRequest request) {

        // Obtener el usuario autenticado
        Usuario usuario = getUsuarioAutenticado();

        // Buscar el aula
        Aula aula = aulaRepository.findById(request.getAulaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aula no encontrada con ID: " + request.getAulaId()
                ));

        // Buscar el tramo horario
        TramoHorario tramoHorario = tramoHorarioRepository.findById(request.getTramoHorarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tramo horario no encontrado con ID: " + request.getTramoHorarioId()
                ));

        // No permitir reservas en el pasado
        if (request.getFecha().isBefore(LocalDate.now())) {
            throw new BadRequestException("No se pueden hacer reservas en el pasado");
        }

        // Verificar capacidad del aula
        if (request.getNumeroAsistentes() > aula.getCapacidad()) {
            throw new BadRequestException(
                    "El número de asistentes (" + request.getNumeroAsistentes() +
                            ") supera la capacidad del aula (" + aula.getCapacidad() + ")"
            );
        }

        // Verificar solapamiento
        if (reservaRepository.existeSolapamiento(
                request.getAulaId(),
                request.getFecha(),
                request.getTramoHorarioId(),
                null)) {
            throw new BadRequestException(
                    "Ya existe una reserva para esa aula, fecha y tramo horario"
            );
        }

        // Crear la reserva
        Reserva reserva = Reserva.builder()
                .fecha(request.getFecha())
                .motivo(request.getMotivo())
                .numeroAsistentes(request.getNumeroAsistentes())
                .aula(aula)
                .tramoHorario(tramoHorario)
                .usuario(usuario)
                .build();

        reserva = reservaRepository.save(reserva);
        return mapper.toReservaDTO(reserva);
    }

    //Elimina una reserva
    @Transactional
    public void deleteReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con ID: " + id
                ));

        Usuario usuarioActual = getUsuarioAutenticado();

        // ADMIN puede borrar cualquier reserva
        // PROFESOR solo puede borrar sus propias reservas
        boolean esAdmin = usuarioActual.getRole() == Usuario.Role.ROLE_ADMIN;
        boolean esSuReserva = reserva.getUsuario().getId().equals(usuarioActual.getId());

        if (!esAdmin && !esSuReserva) {
            throw new ForbiddenException(
                    "No tienes permiso para eliminar esta reserva"
            );
        }


        // Cuando elimines intenta quita las relaciones que tenga la entidad por que te va ha dar
        // error de clave foranea
        // y usa el delete por que esta obteniendo la entidad en  reserva

        reservaRepository.deleteById(id);
    }

    //Obtiene el usuario autenticado actual
    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado"
                ));
    }
}