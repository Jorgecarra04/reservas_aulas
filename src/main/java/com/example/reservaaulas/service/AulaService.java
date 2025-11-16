package com.example.reservaaulas.service;

import com.example.reservaaulas.dto.AulaDTO;
import com.example.reservaaulas.dto.AulaRequest;
import com.example.reservaaulas.dto.ReservaDTO;
import com.example.reservaaulas.entity.Aula;
import com.example.reservaaulas.exception.BadRequestException;
import com.example.reservaaulas.exception.ResourceNotFoundException;
import com.example.reservaaulas.mapper.Mapper;
import com.example.reservaaulas.repository.AulaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AulaService {

    private final AulaRepository aulaRepository;
    private final Mapper mapper;

    //Obtiene todas las aulas
    public List<AulaDTO> getAllAulas() {
        return aulaRepository.findAll()
                .stream()
                .map(mapper::toAulaDTO)
                .collect(Collectors.toList());
    }

    //Obtiene un aula por ID
    public AulaDTO getAulaById(Long id) {
        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aula no encontrada con ID: " + id
                ));
        return mapper.toAulaDTO(aula);
    }

    //Crea una nueva aula
    @Transactional
    public AulaDTO createAula(AulaRequest request) {

        // Verificar que el nombre no esté duplicado
        if (aulaRepository.existsByNombre(request.getNombre())) {
            throw new BadRequestException("Ya existe un aula con ese nombre");
        }

        // Validar número de ordenadores
        if (request.getEsAulaDeOrdenadores() &&
                (request.getNumeroOrdenadores() == null || request.getNumeroOrdenadores() <= 0)) {
            throw new BadRequestException(
                    "Si es aula de ordenadores, debe indicar el número de ordenadores"
            );
        }

        Aula aula = mapper.toAula(request);
        aula = aulaRepository.save(aula);
        return mapper.toAulaDTO(aula);
    }

    //Actualiza un aula existente
    @Transactional
    public AulaDTO updateAula(Long id, AulaRequest request) {

        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aula no encontrada con ID: " + id
                ));

        // Verificar nombre duplicado (excepto el actual)
        if (!aula.getNombre().equals(request.getNombre()) &&
                aulaRepository.existsByNombre(request.getNombre())) {
            throw new BadRequestException("Ya existe un aula con ese nombre");
        }

        mapper.updateAulaFromRequest(aula, request);
        aula = aulaRepository.save(aula);
        return mapper.toAulaDTO(aula);
    }

    //Elimina un aula
    @Transactional
    public void deleteAula(Long id) {
        if (!aulaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aula no encontrada con ID: " + id);
        }
        aulaRepository.deleteById(id);
    }

    //Obtiene las reservas de un aula
    public List<ReservaDTO> getReservasByAula(Long aulaId) {
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aula no encontrada con ID: " + aulaId
                ));

        return aula.getReservas()
                .stream()
                .map(mapper::toReservaDTO)
                .collect(Collectors.toList());
    }

    //Obtiene aulas con capacidad mínima
    public List<AulaDTO> getAulasByCapacidad(Integer capacidad) {
        return aulaRepository.findByCapacidadGreaterThanEqual(capacidad)
                .stream()
                .map(mapper::toAulaDTO)
                .collect(Collectors.toList());
    }

    //Obtiene aulas con ordenadores
    public List<AulaDTO> getAulasConOrdenadores() {
        return aulaRepository.findByEsAulaDeOrdenadoresTrue()
                .stream()
                .map(mapper::toAulaDTO)
                .collect(Collectors.toList());
    }
}