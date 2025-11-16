package com.example.reservaaulas.service;

import com.example.reservaaulas.dto.TramoHorarioDTO;
import com.example.reservaaulas.dto.TramoHorarioRequest;
import com.example.reservaaulas.entity.TramoHorario;
import com.example.reservaaulas.exception.BadRequestException;
import com.example.reservaaulas.exception.ResourceNotFoundException;
import com.example.reservaaulas.mapper.Mapper;
import com.example.reservaaulas.repository.TramoHorarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TramoHorarioService {

    private final TramoHorarioRepository tramoHorarioRepository;
    private final Mapper mapper;

    //Obtiene todos los tramos horarios
    public List<TramoHorarioDTO> getAllTramosHorarios() {
        return tramoHorarioRepository.findAll()
                .stream()
                .map(mapper::toTramoHorarioDTO)
                .collect(Collectors.toList());
    }

    //Obtiene un tramo horario por ID
    public TramoHorarioDTO getTramoHorarioById(Long id) {
        TramoHorario tramo = tramoHorarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tramo horario no encontrado con ID: " + id
                ));
        return mapper.toTramoHorarioDTO(tramo);
    }

    //Crea un nuevo tramo horario
    @Transactional
    public TramoHorarioDTO createTramoHorario(TramoHorarioRequest request) {

        // Validar que la hora de inicio sea antes que la de fin
        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new BadRequestException(
                    "La hora de inicio debe ser anterior a la hora de fin"
            );
        }

        TramoHorario tramo = mapper.toTramoHorario(request);
        tramo = tramoHorarioRepository.save(tramo);
        return mapper.toTramoHorarioDTO(tramo);
    }

    //Elimina un tramo horario
    @Transactional
    public void deleteTramoHorario(Long id) {
        //
        if (!tramoHorarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Tramo horario no encontrado con ID: " + id
            );
        }
        tramoHorarioRepository.deleteById(id);
    }
}