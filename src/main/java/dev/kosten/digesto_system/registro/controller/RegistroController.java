package dev.kosten.digesto_system.registro.controller;

import dev.kosten.digesto_system.log.LogService;
import dev.kosten.digesto_system.registro.dto.RegistroDTO;
import dev.kosten.digesto_system.registro.service.RegistroService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CONTROLLER DE AUDITORÍA: Registro.
 * Expone endpoints REST de SÓLO LECTURA para consultar el historial
 * de carga de documentos.
 * @author Quique
 */
@RestController
@RequestMapping("/api/v1/registros")
@RequiredArgsConstructor
public class RegistroController {

    private final RegistroService registroService;
    private final LogService logService;

    /**
     * Endpoint para OBTENER todos los registros de auditoría.
     * Responde a: GET /api/v1/registros
     * @return ResponseEntity con la lista de DTOs y estado 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<RegistroDTO>> listarTodos() {
        logService.info("GET /api/v1/registros - Listando todo el historial");
        List<RegistroDTO> historial = registroService.listarTodos();
        return ResponseEntity.ok(historial);
    }

    /**
     * Endpoint para OBTENER el historial de un documento específico.
     * Responde a: GET /api/v1/registros/documento/{idDocumento}
     * @param idDocumento El ID del documento (Integer) cuyo historial se quiere ver.
     * @return ResponseEntity con la lista de DTOs y estado 200 (OK).
     */
    @GetMapping("/documento/{idDocumento}")
    public ResponseEntity<List<RegistroDTO>> obtenerHistorialPorDocumento(
            @PathVariable Integer idDocumento) {
        
        logService.info("GET /api/v1/registros/documento/" + idDocumento + " - Buscando historial");
        List<RegistroDTO> historial = registroService.obtenerHistorialPorDocumento(idDocumento);
        return ResponseEntity.ok(historial);
    }

    /**
     * Endpoint para OBTENER el historial de un usuario específico.
     * Responde a: GET /api/v1/registros/usuario/{idUsuario}
     * @param idUsuario El ID del usuario (Integer) cuyo historial se quiere ver.
     * @return ResponseEntity con la lista de DTOs y estado 200 (OK).
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<RegistroDTO>> obtenerHistorialPorUsuario(
            @PathVariable Integer idUsuario) {
        
        logService.info("GET /api/v1/registros/usuario/" + idUsuario + " - Buscando historial");
        List<RegistroDTO> historial = registroService.obtenerHistorialPorUsuario(idUsuario);
        return ResponseEntity.ok(historial);
    }
}
