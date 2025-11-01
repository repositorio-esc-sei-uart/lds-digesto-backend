package dev.kosten.digesto_system.documento.service;

import dev.kosten.digesto_system.archivo.entity.Archivo;
import dev.kosten.digesto_system.archivo.service.ArchivoService;
import dev.kosten.digesto_system.documento.dto.DocumentoDTO;
import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.documento.repository.DocumentoRepository;
import dev.kosten.digesto_system.estado.entity.Estado;
import dev.kosten.digesto_system.estado.repository.EstadoRepository;
import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.log.LogService;
import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;
import dev.kosten.digesto_system.palabraclave.repository.PalabraClaveRepository;
import dev.kosten.digesto_system.sector.Sector;
import dev.kosten.digesto_system.sector.SectorRepository;
import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import dev.kosten.digesto_system.tipodocumento.repository.TipoDocumentoRepository;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio de negocio para la entidad Documento.
 * Contiene toda la lógica de negocio para crear, actualizar, leer y eliminar
 * documentos, asegurando la integridad de los datos y las relaciones.
 * @author micael
 * @author Quique
 */
@Service
@RequiredArgsConstructor
public class DocumentoService {
    
    // --- Dependencias Inyectadas por Constructor ---
    private final DocumentoRepository documentoRepo;
    private final TipoDocumentoRepository tipoDocumentoRepo;
    private final EstadoRepository estadoRepo;
    private final SectorRepository sectorRepo;
    private final PalabraClaveRepository palabraClaveRepo;

    // --- INYECTAR Services ---
    private final ArchivoService archivoService;
    private final LogService logService;
    
    // --- Métodos de Lectura ---

    /**
     * Devuelve una lista de todas las entidades Documento.
     * @return Lista de Documento.
     */
    @Transactional(readOnly = true)
    public List<Documento> listarTodos() {
        logService.info("Solicitud para listar todos los documentos.");
        return documentoRepo.findAll();
    }

    /**
     * Busca un Documento por su ID.
     * @param id El ID del documento a buscar.
     * @return La entidad Documento encontrada.
     * @param id El ID del documento a buscar.
     * @return RecursoNoEncontradoException si el documento no existe.
     */
    @Transactional(readOnly = true)
    public Documento obtenerPorId(Integer id) {
        logService.info("Buscando documento con ID: " + id);
        return documentoRepo.findById(id)
            .orElseThrow(() -> {
                logService.warn("Intento de búsqueda de documento no existente con ID: " + id);
                return new RecursoNoEncontradoException("Documento no encontrado con ID: " + id);
            });
    }

    // --- Método de Creación ---
    /**
     * Crea una nueva entidad Documento a partir de un DTO.
     * 1. Valida la unicidad del 'numDocumento'.
     * 2. Resuelve todas las entidades relacionadas (FKs).
     * 3. Persiste el nuevo documento.
     * @param dto El DocumentoDTO con los datos para la creación.
     * @return La entidad Documento persistida.
     * @throws RecursoDuplicadoException si 'numDocumento' ya existe.
     * @throws RecursoNoEncontradoException si algún ID de catálogo (Estado, Sector, etc.) no existe.
     */
    @Transactional // Buena práctica para operaciones que tocan varias tablas
    public Documento crearDocumento(DocumentoDTO dto) {
        logService.info("Iniciando creación de documento: " + dto.getNumDocumento());

        // --- VALIDACIÓN DE numDocumento ÚNICO ---
        documentoRepo.findByNumDocumento(dto.getNumDocumento()).ifPresent(doc -> {
            logService.warn("Intento de crear documento duplicado por numDocumento: " + dto.getNumDocumento());
            throw new RecursoDuplicadoException("Ya existe un documento con el número: " + dto.getNumDocumento());
        });

        // --- BÚSQUEDA DE ENTIDADES RELACIONADAS ---
        TipoDocumento tipo = tipoDocumentoRepo.findById(dto.getIdTipoDocumento())
            .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de documento no existe con ID: " + dto.getIdTipoDocumento()));

        Estado estado = estadoRepo.findById(dto.getIdEstado())
            .orElseThrow(() -> new RecursoNoEncontradoException("Estado no existe con ID: " + dto.getIdEstado()));
 
        Sector sector = sectorRepo.findById(dto.getIdSector())
            .orElseThrow(() -> new RecursoNoEncontradoException("Sector no existe con ID: " + dto.getIdSector()));
        
        // --- Búsqueda de Palabras Clave ---
        Set<PalabraClave> etiquetas = new HashSet<>();
        if (dto.getIdsPalabrasClave() != null && !dto.getIdsPalabrasClave().isEmpty()) {
            etiquetas.addAll(palabraClaveRepo.findAllById(dto.getIdsPalabrasClave()));
        }
 
        Set<Documento> referencias = new HashSet<>();
        if (dto.getIdsReferencias() != null && !dto.getIdsReferencias().isEmpty()) {
            List<Documento> referenciasEncontradas = documentoRepo.findAllById(dto.getIdsReferencias());
            referencias.addAll(referenciasEncontradas);
        }

        // --- Construcción de la Nueva Entidad ---
        Documento nuevoDocumento = Documento.builder()
            .titulo(dto.getTitulo())
            .resumen(dto.getResumen())
            .numDocumento(dto.getNumDocumento())
            .fechaCreacion(dto.getFechaCreacion())
            .tipoDocumento(tipo)
            .estado(estado)
            .sector(sector)
            .palabrasClave(etiquetas)
            .referencias(referencias)
            .build();

        // --- Guardado en BD ---
        Documento documentoGuardado = documentoRepo.save(nuevoDocumento);
        logService.info("Documento creado exitosamente con ID: " + documentoGuardado.getIdDocumento());
        return documentoGuardado;
    }

    // --- Método de Actualización ---
    /**
     * Actualiza un documento existente.
     * Actualiza un documento existente.
     * 1. Valida unicidad de 'numDocumento' (excluyendo el ID actual).
     * 2. Busca el documento existente.
     * 3. Resuelve las nuevas relaciones (FKs).
     * 4. Actualiza las colecciones (@ManyToMany) de forma segura.
     * 5. Actualiza los campos simples.
     * 6. Persiste los cambios.
     * @param id El ID del documento a actualizar.
     * @param dto El DocumentoDTO con los nuevos datos.
     * @return La entidad Documento actualizada.
     */
    @Transactional
    public Documento actualizarDocumento(Integer id, DocumentoDTO dto) {
        logService.info("Iniciando actualización de documento ID: " + id);
        
        // VALIDACIÓN DE numDocumento ÚNICO ---
        Optional<Documento> docConMismoNumero = documentoRepo.findByNumDocumento(dto.getNumDocumento());

        if (docConMismoNumero.isPresent() && !docConMismoNumero.get().getIdDocumento().equals(id)) {
            // Solo lanza error si el ID del documento encontrado es DIFERENTE al que estamos editando
            logService.warn("Conflicto de unicidad al actualizar documento ID: " + id + ". numDocumento " +
                dto.getNumDocumento() + " ya existe en ID: " + docConMismoNumero.get().getIdDocumento());
            throw new RecursoDuplicadoException("El número de documento " + dto.getNumDocumento() + " ya está en uso por otro documento.");
        }

        // --- BÚSQUEDA DEL DOCUMENTO EXISTENTE ---
         Documento docExistente = obtenerPorId(id);

        // --- BÚSQUEDA DE NUEVAS RELACIONES ---
        TipoDocumento tipo = tipoDocumentoRepo.findById(dto.getIdTipoDocumento())
            .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de documento no existe con ID: " + dto.getIdTipoDocumento()));

        Estado estado = estadoRepo.findById(dto.getIdEstado())
            .orElseThrow(() -> new RecursoNoEncontradoException("Estado no existe con ID: " + dto.getIdEstado()));

        Sector sector = sectorRepo.findById(dto.getIdSector())
            .orElseThrow(() -> new RecursoNoEncontradoException("Sector no existe con ID: " + dto.getIdSector()));

        // --- ACTUALIZACIÓN SEGURA DE COLECCIONES (Palabras Clave) ---
        // Se limpia la colección existente y se añaden las nuevas.
        Set<PalabraClave> nuevasEtiquetasSet = new HashSet<>();
        if (dto.getIdsPalabrasClave() != null && !dto.getIdsPalabrasClave().isEmpty()) {
            nuevasEtiquetasSet.addAll(palabraClaveRepo.findAllById(dto.getIdsPalabrasClave()));
        }
        docExistente.getPalabrasClave().clear(); // Limpia las viejas
        docExistente.getPalabrasClave().addAll(nuevasEtiquetasSet); // Añade las nuevas

        // --- ACTUALIZACIÓN SEGURA DE COLECCIONES (Referencias) ---
        Set<Documento> nuevasReferenciasSet = new HashSet<>();
        if (dto.getIdsReferencias() != null && !dto.getIdsReferencias().isEmpty()) {
            nuevasReferenciasSet.addAll(documentoRepo.findAllById(dto.getIdsReferencias()));
        }
        docExistente.getReferencias().clear(); // Limpia las viejas
        docExistente.getReferencias().addAll(nuevasReferenciasSet); // Añade las nuevas

        // --- ACTUALIZACIÓN DE CAMPOS SIMPLES ---
        docExistente.setTitulo(dto.getTitulo());
        docExistente.setResumen(dto.getResumen());
        docExistente.setNumDocumento(dto.getNumDocumento());
        docExistente.setFechaCreacion(dto.getFechaCreacion());

        // --- ACTUALIZACIÓN DE RELACIONES SIMPLES ---
        docExistente.setTipoDocumento(tipo);
        docExistente.setEstado(estado);
        docExistente.setSector(sector);

        // --- GUARDADO EN BD ---
        Documento actualizado = documentoRepo.save(docExistente);
        logService.info("Documento ID: " + id + " actualizado exitosamente.");
        return actualizado;
    }

    // --- Método de Borrado ---

    /**
     * Elimina un documento por su ID.
     * Primero verifica que exista.
     * @param id El ID del documento a eliminar.
     * @throws RecursoNoEncontradoException si el documento no existe.
    */
    @Transactional
    public void eliminarDocumento(Integer id) {
        logService.info("Iniciando eliminación de documento ID: " + id);
        Documento docExistente = obtenerPorId(id); // Valida que existe
        
        logService.debug("Borrando " + docExistente.getArchivos().size() + " archivos físicos asociados...");

        // Copiamos la lista para evitar ConcurrentModificationException
        List<Archivo> archivosABorrar = new ArrayList<>(docExistente.getArchivos());

        for (Archivo archivo : archivosABorrar) {
             archivoService.borrarArchivo(archivo.getIdArchivo()); // Llama al servicio que SÍ borra del disco
        }
         
        documentoRepo.delete(docExistente);
        logService.info("Documento ID: " + id + " eliminado exitosamente.");
    }
}
