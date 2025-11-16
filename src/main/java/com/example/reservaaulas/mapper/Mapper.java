package com.example.reservaaulas.mapper;

import com.example.reservaaulas.dto.*;
import com.example.reservaaulas.entity.*;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    // ============= AULA =============
    public AulaDTO toAulaDTO(Aula aula) {
        return AulaDTO.builder()
                .id(aula.getId())
                .nombre(aula.getNombre())
                .capacidad(aula.getCapacidad())
                .esAulaDeOrdenadores(aula.getEsAulaDeOrdenadores())
                .numeroOrdenadores(aula.getNumeroOrdenadores())
                .build();
    }

    public Aula toAula(AulaRequest request) {
        return Aula.builder()
                .nombre(request.getNombre())
                .capacidad(request.getCapacidad())
                .esAulaDeOrdenadores(request.getEsAulaDeOrdenadores())
                .numeroOrdenadores(request.getNumeroOrdenadores())
                .build();
    }

    public void updateAulaFromRequest(Aula aula, AulaRequest request) {
        aula.setNombre(request.getNombre());
        aula.setCapacidad(request.getCapacidad());
        aula.setEsAulaDeOrdenadores(request.getEsAulaDeOrdenadores());
        aula.setNumeroOrdenadores(request.getNumeroOrdenadores());
    }

    // ============= TRAMO HORARIO =============
    public TramoHorarioDTO toTramoHorarioDTO(TramoHorario tramo) {
        return TramoHorarioDTO.builder()
                .id(tramo.getId())
                .diaSemana(tramo.getDiaSemana().name())
                .sesionDia(tramo.getSesionDia())
                .horaInicio(tramo.getHoraInicio().toString())
                .horaFin(tramo.getHoraFin().toString())
                .tipo(tramo.getTipo().name())
                .build();
    }

    public TramoHorario toTramoHorario(TramoHorarioRequest request) {
        return TramoHorario.builder()
                .diaSemana(request.getDiaSemana())
                .sesionDia(request.getSesionDia())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .tipo(request.getTipo())
                .build();
    }

    // ============= USUARIO =============
    public UsuarioDTO toUsuarioDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .build();
    }

    // ============= RESERVA =============
    public ReservaDTO toReservaDTO(Reserva reserva) {
        return ReservaDTO.builder()
                .id(reserva.getId())
                .fecha(reserva.getFecha())
                .motivo(reserva.getMotivo())
                .numeroAsistentes(reserva.getNumeroAsistentes())
                .fechaCreacion(reserva.getFechaCreacion())
                .aula(toAulaDTO(reserva.getAula()))
                .tramoHorario(toTramoHorarioDTO(reserva.getTramoHorario()))
                .usuario(toUsuarioDTO(reserva.getUsuario()))
                .build();
    }
}