/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.documento.controller;

// --- Imports de las clases necesarias ---
import dev.kosten.digesto_system.documento.dto.DocumentoDTO;
import dev.kosten.digesto_system.documento.dto.DocumentoMapper;
import dev.kosten.digesto_system.documento.dto.DocumentoTablaDTO;
import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.documento.service.DocumentoService;


import java.util.List;
import java.util.stream.Collectors;

// --- Imports de Spring Framework ---
import org.springframework.beans.factory.annotation.Autowired;
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
 *
 * @author micae
 */

/**
 * Controlador REST para gestionar las peticiones HTTP relacionadas con Documentos.
 * Actúa como la puerta de entrada a la API para el módulo de documentos.
 */
@RestController
@RequestMapping("/api/v1/documentos")
public class DocumentoController {
    
    /**
     * Inyecta el Servicio de Documento, que contiene toda la lógica de negocio.
     * El Controlador solo delega tareas al Servicio.
     */
    @Autowired
    private DocumentoService documentoService;

    /**
     * --- MÉTODO MODIFICADO ---
     * Endpoint para OBTENER una lista RESUMIDA de todos los documentos para la tabla principal.
     * Responde a peticiones GET /api/documentos
     * @return Una lista de DocumentoTablaDTO (el DTO "liviano").
     */
    @GetMapping
    public List<DocumentoTablaDTO> obtenerTodosLosDocumentos() {
        // 1. Pide al servicio la lista de Entidades (esto no cambia)
        List<Documento> documentos = documentoService.listarTodos();
        
        // 2. ¡AQUÍ ESTÁ EL CAMBIO!
        //    Usamos el nuevo método del Mapper 'toTablaDTO' para crear una lista
        //    con solo la información necesaria para el frontend.
        return documentos.stream()
            .map(DocumentoMapper::toTablaDTO) // <-- CAMBIADO DE toDTO A toTablaDTO
            .collect(Collectors.toList());
    }

    /**
     * Endpoint para OBTENER un documento específico por su ID.
     * Responde a peticiones GET /api/documentos/{id}
     * @param id El ID del documento a buscar (viene en la URL).
     * @return Un ResponseEntity con el DocumentoDTO o un error 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO> obtenerDocumentoPorId(@PathVariable Integer id) {
        // 1. El servicio busca la Entidad
        Documento documento = documentoService.obtenerPorId(id);
        
        // 2. El controlador la traduce a DTO y la envía con un código 200 (OK).
        return ResponseEntity.ok(DocumentoMapper.toDTO(documento));
    }

    /**
     * Endpoint para CREAR un nuevo documento.
     * Responde a peticiones POST /api/documentos
     * @param documentoDTO El DTO con los datos del nuevo documento (viene en el cuerpo JSON).
     * @return Un ResponseEntity con el DocumentoDTO recién creado y un código 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<DocumentoDTO> crearDocumento(@RequestBody DocumentoDTO documentoDTO) {
        // 1. Pasa el DTO (con los IDs de estado, sector, etc.) al Servicio.
        //    El Servicio se encarga de la lógica de buscar esos IDs y armar la Entidad.
        Documento nuevoDocumento = documentoService.crearDocumento(documentoDTO);
        
        // 2. Traduce la nueva Entidad (ya guardada) a un DTO para devolverla.
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DocumentoMapper.toDTO(nuevoDocumento));
    }

    /**
     * Endpoint para ACTUALIZAR un documento existente.
     * Responde a peticiones PUT /api/documentos/{id}
     * @param id El ID del documento a actualizar.
     * @param documentoDTO El DTO con los nuevos datos.
     * @return Un ResponseEntity con el DocumentoDTO actualizado y un código 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoDTO> actualizarDocumento(@PathVariable Integer id, @RequestBody DocumentoDTO documentoDTO) {
        // 1. Pasa el ID y el DTO al servicio.
        Documento documentoActualizado = documentoService.actualizarDocumento(id, documentoDTO);
        
        // 2. Traduce la Entidad (ya actualizada) a un DTO para devolverla.
        return ResponseEntity.ok(DocumentoMapper.toDTO(documentoActualizado));
    }

    /**
     * Endpoint para ELIMINAR un documento.
     * Responde a peticiones DELETE /api/documentos/{id}
     * @param id El ID del documento a eliminar.
     * @return Un ResponseEntity sin contenido y un código 204 (NO CONTENT).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDocumento(@PathVariable Integer id) {
        // 1. Le ordena al servicio que elimine el documento.
        documentoService.eliminarDocumento(id);
        
        // 2. Devuelve una respuesta vacía para confirmar el borrado.
        return ResponseEntity.noContent().build();
    }
}
