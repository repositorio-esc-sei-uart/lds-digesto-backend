package dev.kosten.digesto_system.documento.controller;

// --- Imports de las clases necesarias ---
import dev.kosten.digesto_system.documento.dto.DocumentoDTO;
import dev.kosten.digesto_system.documento.dto.DocumentoMapper;
import dev.kosten.digesto_system.documento.dto.DocumentoTablaDTO;
import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.documento.service.DocumentoService;
import dev.kosten.digesto_system.log.LogService;
import java.security.Principal;
import java.util.Date;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

// --- Imports de Spring Framework ---
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar las peticiones HTTP relacionadas con Documentos.
 * Actúa como la puerta de entrada a la API para el módulo de documentos,
 * delegando la lógica de negocio al DocumentoService.
 * @author micael
 * @author Quique
 */
@RestController
@RequestMapping("/api/v1/documentos")
@RequiredArgsConstructor
public class DocumentoController {
    
    // --- Dependencias Inyectadas ---
    private final DocumentoService documentoService;
    private final DocumentoMapper documentoMapper;
    private final LogService logService;

    /**
     * Endpoint para OBTENER una lista RESUMIDA de todos los documentos.
     * Utiliza un DTO "ligero" (DocumentoTablaDTO) optimizado para tablas.
     * @param page El número de página (base 0) para la paginación.
     * @param size El tamaño de la página (cantidad de elementos).
     * @param search(Búsqueda Simple) Término de texto a buscar en todos los campos.
     * @param titulo (Búsqueda Avanzada) Filtra por palabras parciales en el Título.
     * @param numDocumento (Búsqueda Avanzada) Filtra por coincidencia parcial en el N° Documento.
     * @param idTipoDocumento (Búsqueda Avanzada/Simple) Filtra por el ID del Tipo de Documento.
     * @param idSector (Búsqueda Avanzada) Filtra por el ID del Sector.
     * @param idEstado (Búsqueda Avanzada) Filtra por el ID del Estado.
     * @param fechaDesde (Búsqueda Avanzada) Rango de fecha de creación (inicio).
     * @param fechaHasta (Búsqueda Avanzada) Rango de fecha de creación (fin)
     * @param excluirPalabras (Búsqueda Avanzada) Filtra excluyendo palabras en todos los campos.
     * @return 200 OK con una lista de DocumentoTablaDTO.
     */
    @GetMapping
    public ResponseEntity<Page<DocumentoTablaDTO>> listarDocumentos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            //@RequestParam(required = false) Integer idTipoDocumento,
            @RequestParam(required = false) String search, // Búsqueda simple de texto
            
            // (Filtros avanzados)
            @RequestParam(required = false) String titulo, // Búsqueda solo por Título
            @RequestParam(required = false) String numDocumento, // Búsqueda solo por Nro
            @RequestParam(required = false) Integer idTipoDocumento,
            @RequestParam(required = false) Integer idSector,
            @RequestParam(required = false) Integer idEstado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaHasta,
            @RequestParam(required = false) String excluirPalabras) {

        logService.info("GET /api/v1/documentos - page=" + page + ", size=" + size + ", idTipoDocumento=" + idTipoDocumento + ", search=" + search);
        // Crea el objeto de paginación con ordenamiento
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());

        Page<DocumentoTablaDTO> documentos = documentoService.buscarConFiltros(
            // Paginación
            pageable,
            // Búsqueda Simple
            search,
            // Filtros Avanzados
            titulo,
            numDocumento,
            idTipoDocumento,
            idSector,
            idEstado,
            fechaDesde,
            fechaHasta,
            excluirPalabras
        );

        logService.info("GET /api/v1/documentos - Devolviendo página " + page + " con " + documentos.getContent().size() + " documentos.");
        return ResponseEntity.ok(documentos);
    }

    /**
     * Endpoint para OBTENER un documento específico por su ID.
     * Devuelve el DTO "completo" (DocumentoDTO) con todos sus detalles y relaciones.
     * @param id El ID (Integer) del documento a buscar.
     * @return 200 OK con el DocumentoDTO si se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO> obtenerDocumentoPorId(@PathVariable Integer id) {
        logService.info("GET /api/v1/documentos/" + id + " - Solicitud de documento por ID.");
        DocumentoDTO dto = documentoService.obtenerPorIdComoDTO(id);
        logService.info("GET /api/v1/documentos/" + id + " - Documento encontrado: " + dto.getNumDocumento());
        return ResponseEntity.ok(dto);
    }

    /**
     * Endpoint para CREAR un nuevo documento.
     * Recibe un DTO con los datos del documento y sus relaciones (IDs).
     * @param documentoDTO El DocumentoDTO con los datos de creación.
     * @param principal El usuario autenticado (inyectado por Spring Security).
     * @return 201 CREATED con el DTO del documento recién creado.
     */
    @PostMapping
    public ResponseEntity<DocumentoDTO> crearDocumento(@RequestBody DocumentoDTO documentoDTO, Principal principal) {
        String userEmail = principal.getName();
        logService.info("POST /api/v1/documentos - Solicitud para crear documento: " + documentoDTO.getNumDocumento() + " por " + userEmail);
        Documento nuevoDocumento = documentoService.crearDocumento(documentoDTO, userEmail);
        
        logService.info("POST /api/v1/documentos - Documento creado con ID: " + nuevoDocumento.getIdDocumento());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentoMapper.toDTO(nuevoDocumento));
    }

    /**
     * Endpoint para ACTUALIZAR un documento existente.
     * Responde a peticiones PUT /api/documentos/{id}
     * @param id El ID (Integer) del documento a actualizar.
     * @param documentoDTO El {@link DocumentoDTO} con los nuevos datos.
     * @param principal El usuario autenticado (inyectado por Spring Security).
     * @return 200 OK con el DTO del documento actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoDTO> actualizarDocumento(@PathVariable Integer id, @RequestBody DocumentoDTO documentoDTO, Principal principal) {
        String userEmail = principal.getName();
        logService.info("PUT /api/v1/documentos/" + id + " - Solicitud para actualizar documento por " + userEmail);
        Documento documentoActualizado = documentoService.actualizarDocumento(id, documentoDTO, userEmail);
        
        logService.info("PUT /api/v1/documentos/" + id + " - Documento actualizado: " + documentoActualizado.getNumDocumento());
        return ResponseEntity.ok(documentoMapper.toDTO(documentoActualizado));
    }

    /**
     * Endpoint para ELIMINAR un documento por su ID.
     * Responde a peticiones DELETE /api/documentos/{id}
     * @param id El ID del documento a eliminar.
     * @param principal El usuario autenticado (inyectado por Spring Security).
     * @return Un ResponseEntity sin contenido y un código 204 (NO CONTENT).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDocumento(@PathVariable Integer id, Principal principal) {
        String userEmail = principal.getName();
        logService.info("DELETE /api/v1/documentos/" + id + " - Solicitud para eliminar documento por " + userEmail);
        documentoService.eliminarDocumento(id, userEmail);
        
        logService.info("DELETE /api/v1/documentos/" + id + " - Documento eliminado exitosamente por " + userEmail);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para obtener el conteo de documentos por tipo.
     * Responde a GET /api/v1/documentos/count-by-type
     * @return Map con idTipoDocumento -> cantidad
     */
    @GetMapping("/count-by-type")
    public ResponseEntity<Map<Integer, Long>> contarPorTipo() {
        logService.info("GET /api/v1/documentos/count-by-type");
        Map<Integer, Long> conteos = documentoService.contarPorTipo();
        return ResponseEntity.ok(conteos);
    }
}
