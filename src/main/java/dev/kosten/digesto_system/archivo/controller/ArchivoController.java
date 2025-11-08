package dev.kosten.digesto_system.archivo.controller;

import dev.kosten.digesto_system.archivo.dto.ArchivoDTO;
import dev.kosten.digesto_system.archivo.dto.ArchivoMapper;
import dev.kosten.digesto_system.archivo.entity.Archivo;
import dev.kosten.digesto_system.archivo.service.ArchivoService;
import dev.kosten.digesto_system.log.LogService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador REST para la gestión de archivos adjuntos a documentos.
 * Maneja operaciones de subida, visualización, descarga y eliminación de archivos.
 * @author Quique
 */
@RestController
@RequestMapping("/api/v1/archivos")
@RequiredArgsConstructor
public class ArchivoController {
    private final ArchivoService archivoService;
    private final ArchivoMapper archivoMapper;
    private final LogService logService;

    /**
     * Endpoint para SUBIR un nuevo archivo y asociarlo a un Documento.
     * Responde a POST /api/v1/archivos/subir/{idDocumento}
     * @param files El archivo binario (form-data).
     * @param idDocumento El ID del documento al que se adjunta.
     * @return 201 CREATED con el DTO del archivo creado.
     */
    @PostMapping("/subir/{idDocumento}")
    public ResponseEntity<List<ArchivoDTO>> subirArchivos(@RequestParam("file") MultipartFile[] files, @PathVariable Integer idDocumento) {
        logService.info("POST /api/v1/archivos/subir/" + idDocumento + " - Archivos recibidos: " + files.length);
        List<Archivo> nuevosArchivos = archivoService.subirArchivos(files, idDocumento);
        
        List<ArchivoDTO> dtos = nuevosArchivos.stream()
            .map(archivoMapper::toDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtos);
    }

    /**
     * Endpoint para borrar un archivo específico por su ID.
     * Responde a peticiones DELETE /api/v1/archivos/{id}
     * @param id El ID del archivo a eliminar.
     * @return 204 No Content si fue exitoso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarArchivo(@PathVariable Integer id) {
        archivoService.borrarArchivo(id);
        return ResponseEntity.noContent().build(); // Código 204
    }

    /**
     * Endpoint para visualizar un archivo en el navegador.
     * Responde a GET /api/v1/archivos/{id}
     * Utiliza Content-Disposition: inline para mostrar el archivo sin descargarlo.
     * @param id ID del archivo a visualizar.
     * @return ResponseEntity con el recurso del archivo y cabeceras para visualización inline.
     */
    @GetMapping("/{id}/{nombre:.+}")
    public ResponseEntity<Resource> visualizarArchivo(@PathVariable Integer id, @PathVariable String nombre) {
        Resource resource = archivoService.descargarArchivo(id);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }

    /**
     * Endpoint para DESCARGAR un archivo físico.
     * Responde a GET /api/v1/archivos/{id}
     * @param id El ID del archivo a descargar.
     * @return El archivo binario (Resource) con cabecera de descarga.
     */
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Integer id) {
        Resource resource = archivoService.descargarArchivo(id);
        
        // Prepara la cabecera para forzar la descarga en el navegador
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}
