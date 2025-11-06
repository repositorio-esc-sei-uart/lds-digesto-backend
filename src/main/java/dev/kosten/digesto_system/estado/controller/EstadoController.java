package dev.kosten.digesto_system.estado.controller;

import dev.kosten.digesto_system.estado.dto.EstadoDTO;
import dev.kosten.digesto_system.estado.dto.EstadoMapper;
import dev.kosten.digesto_system.estado.entity.Estado;
import dev.kosten.digesto_system.estado.service.EstadoService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de la entidad Estado de documentos.
 * Expone los endpoints de la API para las operaciones CRUD (Crear, Leer, Actualizar, Borrar)
 * sobre los estados de los documentos (ej. "Vigente", "Derogado").
 * @author micael
 * @author Quique
 */

@RestController
@RequestMapping("/api/v1/estados")
@RequiredArgsConstructor
public class EstadoController {
    
    // Inyección por constructor
    private final EstadoService estadoService;
    private final EstadoMapper estadoMapper;

    /**
     * Recupera una lista de todos los estados de documento.
     * Endpoint: GET /api/v1/estados
     * @return ResponseEntity con una lista de EstadoDTO y estado HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<EstadoDTO>> obtenerTodos() {
        List<EstadoDTO> dtos = estadoService.listarTodos().stream()
            .map(estadoMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene un estado de documento específico por su ID.
     * Endpoint: GET /api/v1/estados/{id}
     * @param id El ID (Integer) del estado a buscar.
     * @return ResponseEntity con el {@link EstadoDTO} encontrado y estado HTTP 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> obtenerPorId(@PathVariable Integer id) {
        Estado estado = estadoService.obtenerPorId(id);
        return ResponseEntity.ok(estadoMapper.toDTO(estado));
    }

    /**
     * Crea un nuevo estado de documento.
     * Endpoint: POST /api/v1/estados
     * @param dto El EstadoDTO con la información del nuevo estado.
     * @return ResponseEntity con el EstadoDTO creado y estado HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<EstadoDTO> crear(@RequestBody EstadoDTO dto) {
        Estado estadoParaGuardar = estadoMapper.toEntity(dto);
        Estado estadoGuardado = estadoService.crearEstado(estadoParaGuardar);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoMapper.toDTO(estadoGuardado));
    }

    /**Crea un nuevo estado de documento.
     * Endpoint: POST /api/v1/estados
     * @param dto El EstadoDTO con la información del nuevo estado.
     * @return ResponseEntity con el EstadoDTO creado y estado HTTP 201 Created.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> actualizar(@PathVariable Integer id, @RequestBody EstadoDTO dto) {
        Estado datosNuevos = estadoMapper.toEntity(dto);
        Estado estadoActualizado = estadoService.actualizarEstado(id, datosNuevos);
        return ResponseEntity.ok(estadoMapper.toDTO(estadoActualizado));
    }

    /**
     * Elimina un estado de documento por su ID.
     * Endpoint: DELETE /api/v1/estados/{id}
     * @param id El ID (Integer) del estado a eliminar.
     * @return ResponseEntity con estado HTTP 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        estadoService.eliminarEstado(id);
        return ResponseEntity.noContent().build();
    }
}
