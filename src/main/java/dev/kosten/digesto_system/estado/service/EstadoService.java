package dev.kosten.digesto_system.estado.service;

import dev.kosten.digesto_system.estado.entity.Estado;
import dev.kosten.digesto_system.estado.repository.EstadoRepository;
import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para la gestión de Estado de documentos.
 * Proporciona la lógica de negocio para operaciones CRUD (Crear, Leer, Actualizar, Borrar)
 * sobre la entidad Estado, asegurando la integridad de los datos y manejando excepciones.
 * @author micael
 * @author Quique
 */
@Service
@RequiredArgsConstructor
public class EstadoService {
    
    private final EstadoRepository estadoRepository;

    /**
     * Recupera una lista de todos los Estados de documento.
     * @return una Lista de entidades Estado.
     */
    @Transactional(readOnly = true)
    public List<Estado> listarTodos() {
        return estadoRepository.findAll();
    }

    /**
     * Busca y devuelve un Estado por su clave primaria (ID).
     * @param id La clave primaria del estado a buscar.
     * @return La entidad Estado correspondiente.
     * @throws RecursoNoEncontradoException si no se encuentra ningún Estado con ese ID.
     */
    @Transactional(readOnly = true)
    public Estado obtenerPorId(Integer id) {
        return estadoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + id));
    }

    /**
     * Crea y persiste un nuevo Estado en la base de datos.
     * Valida que no exista otro Estado con el mismo nombre.
     * @param estado La entidad Estado a crear. (Debe tener nombre).
     * @return La entidad {@link Estado} persistida con su ID asignado.
     * @throws RecursoDuplicadoException si ya existe un Estado con ese nombre.
     */
    @Transactional
    public Estado crearEstado(Estado estado) {
        Optional<Estado> existente = estadoRepository.findByNombre(estado.getNombre());
        if (existente.isPresent()) {
            throw new RecursoDuplicadoException("Ya existe un estado con el nombre: " + estado.getNombre());
        }
        return estadoRepository.save(estado);
    }

    /**
     * Actualiza un Estado existente en la base de datos.
     * @param id El ID del Estado a actualizar.
     * @param datosNuevos La entidad {@link Estado} con los datos a modificar (nombre, descripcion).
     * @return La entidad {@link Estado} actualizada.
     * @throws RecursoNoEncontradoException si el ID del estado no existe.
     * @throws RecursoDuplicadoException si se intenta renombrar a un nombre que ya existe.
     */
    @Transactional
    public Estado actualizarEstado(Integer id, Estado datosNuevos) {
        Optional<Estado> existenteConMismoNombre = estadoRepository.findByNombre(datosNuevos.getNombre());
        if (existenteConMismoNombre.isPresent() && !existenteConMismoNombre.get().getIdEstado().equals(id)) {
            throw new RecursoDuplicadoException("El nombre '" + datosNuevos.getNombre() + "' ya está en uso por otro estado.");
        }
        Estado estadoExistente = obtenerPorId(id);
        
        estadoExistente.setNombre(datosNuevos.getNombre());
        estadoExistente.setDescripcion(datosNuevos.getDescripcion());
        
        return estadoRepository.save(estadoExistente);
    }

    /**
     * Elimina permanentemente un Estado de la base de datos.
     * @param id El ID del Estado a eliminar.
     * @throws RecursoNoEncontradoException si el ID del estado no existe.
     */
    @Transactional
    public void eliminarEstado(Integer id) {
        Estado estadoExistente = obtenerPorId(id);
        estadoRepository.delete(estadoExistente);
    }
}
