package dev.kosten.digesto_system.archivo.service;

import dev.kosten.digesto_system.almacenamiento.AlmacenamientoService;
import dev.kosten.digesto_system.archivo.entity.Archivo;
import dev.kosten.digesto_system.archivo.repository.ArchivoRepository;
import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.documento.repository.DocumentoRepository;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.log.LogService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio de negocio para la gestión de Archivos.
 * Responsable de la lógica para crear, leer, borrar y descargar archivos,
 * conectando la metadata de la base de datos (ArchivoRepository) con el
 * almacenamiento físico (AlmacenamientoService).
 * @author Quique
 */
@Service
@RequiredArgsConstructor
public class ArchivoService {
    private final ArchivoRepository archivoRepo;
    private final AlmacenamientoService almacenamientoService;
    private final DocumentoRepository documentoRepo; 
    private final LogService logService;
    
    /**
     * Obtiene una entidad Archivo por su clave primaria.
     * @param idArchivo El ID del archivo.
     * @return La entidad Archivo.
     */
    public Archivo obtenerPorId(Integer idArchivo) {
        return archivoRepo.findById(idArchivo)
            .orElseThrow(() -> new RecursoNoEncontradoException("Archivo no encontrado con ID: " + idArchivo));
    }

    /**
     * Almacena uno o más archivos físicos y persiste sus metadatos.
     * Utiliza la lógica de "sharding" (generarRutaSharded) para determinar
     * la ruta de almacenamiento físico.
     * @param files Los archivos (MultipartFile[]) subido.
     * @param idDocumento El ID del Documento al que pertenece.
     * @return La entidad Archivo guardada.
     */
    @Transactional
    public List<Archivo> subirArchivos(MultipartFile[] files, Integer idDocumento) {
        Documento documento = documentoRepo.findById(idDocumento)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se puede subir archivo: Documento no encontrado con ID: " + idDocumento));
        
        String rutaFragmentada = generarRutaSharded(idDocumento);
        List<Archivo> archivosGuardados = new ArrayList<>();
        
        for (MultipartFile file : files) {
            String nombreOriginal = file.getOriginalFilename();
            String nombreArchivo = truncarNombreArchivo(nombreOriginal);
            String rutaRelativa = rutaFragmentada + "/" + nombreArchivo;

            // Guarda el archivo físico
            almacenamientoService.save(file, rutaRelativa);
            logService.info("Archivo físico guardado en: " + rutaRelativa);

            // Crea la entidad de metadatos
            Archivo nuevoArchivo = Archivo.builder()
                    .documento(documento)
                    .nombre(nombreArchivo)
                    .url(rutaRelativa)
                    .build();

            // Guarda la entidad en la BD y la añade a la lista
            archivosGuardados.add(archivoRepo.save(nuevoArchivo));
        }
        
        return archivosGuardados; // Devuelve la lista de archivos creados
    }

    /**
     * Carga un archivo como un Recurso (Resource) para su descarga.
     * @param idArchivo El ID del archivo a descargar.
     * @return El archivo como un objeto Resource.
     */
    @Transactional(readOnly = true)
    public Resource descargarArchivo(Integer idArchivo) {
        logService.info("Solicitud de descarga para archivo ID: " + idArchivo);
        Archivo archivo = obtenerPorId(idArchivo); // Valida que existe
        return almacenamientoService.loadAsResource(archivo.getUrl());
    }

    /**
     * Borra un archivo permanentemente, tanto del sistema de archivos como de la base de datos.
     * @param idArchivo El ID del archivo a borrar.
     */
    @Transactional
    public void borrarArchivo(Integer idArchivo) {
        logService.info("Iniciando borrado de archivo ID: " + idArchivo);
        Archivo archivo = obtenerPorId(idArchivo);
        
        almacenamientoService.delete(archivo.getUrl());
        logService.info("Archivo físico borrado de: " + archivo.getUrl());
        
        archivoRepo.delete(archivo);
        logService.info("Registro de archivo ID: " + idArchivo + " eliminado de la BD.");
    }

    /**
     * Genera una ruta de almacenamiento fragmentada (sharded) basada en el ID del documento.
     * Convierte un ID (ej: 123456) en una ruta estructurada (ej: "documentos/000/123/456")
     * para evitar directorios con un número excesivo de archivos (hot directories).
     * @param idDocumento El ID del documento.
     * @return Una ruta relativa (ej: "documentos/000/123/456").
     */
    private String generarRutaSharded(Integer idDocumento) {
        // Convierte el ID en un String, rellenando con ceros a la izquierda.
        // "%09d" significa: "formatear un entero (d) con 9 dígitos, rellenando con ceros (0)".
        // Ej: 123456 -> "000123456"
        String idRelleno = String.format("%09d", idDocumento);

        // Fragmenta el string en subcarpetas (ej: 3 niveles de 3 caracteres)
        String nivel1 = idRelleno.substring(0, 3);
        String nivel2 = idRelleno.substring(3, 6);
        String nivel3 = idRelleno.substring(6, 9);

        // Une las partes para formar la ruta. Usamos String.join para un código limpio.
        // El resultado es: "documentos/000/123/456"
        return String.join("/", "documentos", nivel1, nivel2, nivel3);
    }
    /**
         * Recorta el nombre del archivo a 60 caracteres, preservando la extensión.
         */
        private String truncarNombreArchivo(String nombreOriginal) {
            if (nombreOriginal == null || nombreOriginal.length() <= 60) {
                return nombreOriginal;
            }

            String extension = "";
            String nombreBase = nombreOriginal;
            
            // Buscamos la extensión
            int dotIndex = nombreOriginal.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = nombreOriginal.substring(dotIndex);
                nombreBase = nombreOriginal.substring(0, dotIndex);
            }
            // Calculamos cuánto espacio nos queda para el nombre (60 - longitud extensión)
            int maxLongitudBase = 60 - extension.length();
            
            // Si la extensión es larguísima (raro) o no deja espacio, cortamos a lo bruto
            if (maxLongitudBase <= 0) {
                return nombreOriginal.substring(0, 60);
            }

            // Cortamos el nombre base y le pegamos la extensión
            return nombreBase.substring(0, maxLongitudBase) + extension;
        }
}
