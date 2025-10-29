/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.estadoU;

import dev.kosten.digesto_system.estadoU.EstadoUDTO;
import dev.kosten.digesto_system.estadoU.EstadoUMapper;
import dev.kosten.digesto_system.estadoU.EstadoU;
import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.estadoU.EstadoURepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona los estados de usuario ({@link EstadoU}).
 * 
 * Proporciona métodos para listar, crear, obtener y eliminar estados,
 * aplicando las validaciones necesarias para evitar duplicados y manejar
 * correctamente los errores de inexistencia.
 * 
 * Utiliza {@link EstadoURepository} para la persistencia y {@link EstadoUMapper}
 * para la conversión entre entidad y DTO.
 * 
 */
@Service
public class EstadoUService {

    @Autowired
    private EstadoURepository estadoRepo;

    @Autowired
    private EstadoUMapper mapper;

    /**
     * Lista todos los estados de usuario existentes.
     * 
     * @return lista de {@link EstadoU}
     */
    public List<EstadoU> listarEstados() {
        return estadoRepo.findAll();
    }

    /**
     * Crea un nuevo estado de usuario.
     * 
     * @param dto objeto {@link EstadoUDTO} con la información del estado
     * @return {@link EstadoUDTO} creado
     * @throws RecursoDuplicadoException si ya existe un estado con el mismo nombre
     */
    public EstadoUDTO crearEstado(EstadoUDTO dto) {
        if (estadoRepo.findByNombre(dto.getNombre()).isPresent()) {
            throw new RecursoDuplicadoException("Ya existe un estado con ese nombre");
        }
        EstadoU estado = mapper.toEntity(dto);
        return mapper.toDTO(estadoRepo.save(estado));
    }

    /**
     * Obtiene un estado por su identificador.
     * 
     * @param id identificador del estado
     * @return {@link EstadoU} encontrado
     * @throws RecursoNoEncontradoException si no existe el estado
     */
    public EstadoU obtenerPorId(Integer id) {
        return estadoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado"));
    }

    /**
     * Convierte una entidad {@link EstadoU} a su DTO correspondiente.
     * 
     * @param estado entidad a convertir
     * @return {@link EstadoUDTO} resultante
     */
    public EstadoUDTO toDTO(EstadoU estado) {
        return mapper.toDTO(estado);
    }

    /**
     * Elimina un estado de usuario por su identificador.
     * 
     * @param id identificador del estado a eliminar
     * @throws RecursoNoEncontradoException si no existe el estado
     */
    public void eliminarPorID(Integer id) {
        EstadoU estado = estadoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado"));
        estadoRepo.delete(estado);
    }
}

