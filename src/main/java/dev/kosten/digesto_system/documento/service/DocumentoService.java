package dev.kosten.digesto_system.documento.service;

import dev.kosten.digesto_system.archivo.entity.Archivo;
import dev.kosten.digesto_system.archivo.service.ArchivoService;
import dev.kosten.digesto_system.documento.dto.DocumentoDTO;
import dev.kosten.digesto_system.documento.dto.DocumentoMapper;
import dev.kosten.digesto_system.documento.dto.DocumentoTablaDTO;
import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.documento.repository.DocumentoRepository;
import dev.kosten.digesto_system.documento.specification.DocumentoSpecification;
import dev.kosten.digesto_system.estado.entity.Estado;
import dev.kosten.digesto_system.estado.repository.EstadoRepository;
import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.log.LogService;
import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;
import dev.kosten.digesto_system.palabraclave.repository.PalabraClaveRepository;
import dev.kosten.digesto_system.registro.entity.Registro;
import dev.kosten.digesto_system.registro.repository.RegistroRepository;
import dev.kosten.digesto_system.sector.Sector;
import dev.kosten.digesto_system.sector.SectorRepository;
import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import dev.kosten.digesto_system.tipodocumento.repository.TipoDocumentoRepository;
import dev.kosten.digesto_system.usuario.Usuario;
import dev.kosten.digesto_system.usuario.UsuarioRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    
    // --- Dependencias Inyectadas ---
    private final DocumentoRepository documentoRepo;
    private final TipoDocumentoRepository tipoDocumentoRepo;
    private final EstadoRepository estadoRepo;
    private final SectorRepository sectorRepo;
    private final PalabraClaveRepository palabraClaveRepo;
    private final UsuarioRepository usuarioRepository;
    private final RegistroRepository registroRepository;

    // --- INYECTAR Services ---
    private final ArchivoService archivoService;
    private final LogService logService;
    
    private final DocumentoMapper documentoMapper;
    
    // --- Métodos de Lectura ---

    /**
     * Devuelve una lista de todas las entidades Documento.
     * @return Lista de Documento.
     */
    @Transactional(readOnly = true)
    public List<DocumentoTablaDTO> listarTodos() {
        logService.info("Solicitud para listar todos los documentos.");
        List<Documento> documentos = documentoRepo.findAll();
        return documentos.stream()
            .map(documentoMapper::toTablaDTO)
            .collect(Collectors.toList());
    }

    /**
     * Devuelve una página de documentos con paginación.
     * @param pageable Configuración de paginación
     * @return Page de DocumentoTablaDTO
     */
    @Transactional(readOnly = true)
    public Page<DocumentoTablaDTO> listarPaginado(Pageable pageable) {
        logService.info("Solicitud para listar documentos paginados.");
        Page<Documento> documentos = documentoRepo.findAll(pageable);
        return documentos.map(documentoMapper::toTablaDTO);
    }

    /**
     * Devuelve una página de documentos filtrados por tipo de documento.
     * @param idTipoDocumento ID del tipo de documento a filtrar
     * @param pageable Configuración de paginación
     * @return Page de DocumentoTablaDTO filtrados
     */
    @Transactional(readOnly = true)
    public Page<DocumentoTablaDTO> listarPorTipo(Integer idTipoDocumento, Pageable pageable) {
        logService.info("Solicitud para listar documentos filtrados por tipo: " + idTipoDocumento);
        Page<Documento> documentos = documentoRepo.findByTipoDocumento_IdTipoDocumento(idTipoDocumento, pageable);
        return documentos.map(documentoMapper::toTablaDTO);
    }

    /**
     * Busca documentos con filtros opcionales combinados.
     * Soporta búsqueda por texto y filtro por tipo simultáneamente.
     * @param searchTerm Término de búsqueda (busca en título, resumen, numDocumento)
     * @param idTipoDocumento ID del tipo (null = todos los tipos)
     * @param pageable Configuración de paginación
     * @return Page de DocumentoTablaDTO
     */
    @Transactional(readOnly = true)
    public Page<DocumentoTablaDTO> buscarConFiltros(
            String searchTerm, 
            Integer idTipoDocumento, 
            Pageable pageable) {

        logService.info("Búsqueda con filtros - searchTerm: " + searchTerm + ", idTipo: " + idTipoDocumento);

        Specification<Documento> spec = crearSpecification(searchTerm, idTipoDocumento);

        // Agrega filtro de tipo si existe
        if (idTipoDocumento != null) {
            spec = spec.and(DocumentoSpecification.conTipoDocumento(idTipoDocumento));
        }

        Page<Documento> documentos = documentoRepo.findAll(spec, pageable);
        return documentos.map(documentoMapper::toTablaDTO);
    }

    /**
     * Método helper para construir la Specification
     */
    private Specification<Documento> crearSpecification(String searchTerm, Integer idTipoDocumento) {
        Specification<Documento> spec = null;

        // Agrega búsqueda si existe
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            spec = DocumentoSpecification.conTerminoDeBusqueda(searchTerm);
        }

        // Agrega filtro de tipo si existe
        if (idTipoDocumento != null) {
            Specification<Documento> tipoSpec = DocumentoSpecification.conTipoDocumento(idTipoDocumento);
            spec = (spec == null) ? tipoSpec : spec.and(tipoSpec);
        }

        // Si no hay filtros, retorna una spec que devuelve todos
        return spec;
    }

    /**
     * Busca un Documento por su ID.
     * @param id El ID del documento a buscar.
     * @return La entidad Documento encontrada.
     * @param id El ID del documento a buscar.
     * @return RecursoNoEncontradoException si el documento no existe.
     */
    @Transactional(readOnly = true)
    public DocumentoDTO obtenerPorIdComoDTO(Integer id) {
        logService.info("Buscando documento con ID: " + id);
        Documento documento = buscarDocumentoPorId(id);
        return documentoMapper.toDTO(documento);
    }

    /**
     * Método PRIVADO que devuelve la entidad (para uso interno del servicio)
     * Este método NO hace mapeo, solo busca la entidad
     */
    private Documento buscarDocumentoPorId(Integer id) {
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
     * @param userEmail
     * @return La entidad Documento persistida.
     */
    @Transactional // Buena práctica para operaciones que tocan varias tablas
    public Documento crearDocumento(DocumentoDTO dto, String userEmail) {
        logService.info("Iniciando creación de documento: " + dto.getNumDocumento() + " por " + userEmail);

        // --- VALIDACIÓN DE numDocumento ÚNICO ---
        documentoRepo.findByNumDocumento(dto.getNumDocumento()).ifPresent(doc -> {
            logService.warn("Intento de crear documento duplicado por numDocumento: " + dto.getNumDocumento());
            throw new RecursoDuplicadoException("Ya existe un documento con el número: " + dto.getNumDocumento());
        });

        // --- VALIDACIÓN DE título ÚNICO ---
        documentoRepo.findByTitulo(dto.getTitulo()).ifPresent(doc -> {
            logService.warn("Intento de crear documento duplicado por titulo: " + dto.getTitulo());
            throw new RecursoDuplicadoException("Ya existe un documento con el título: " + dto.getTitulo());
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

        Usuario usuario = usuarioRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + userEmail));

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

        // --- Guardado de documento en BD ---
        Documento documentoGuardado = documentoRepo.save(nuevoDocumento);
        
        // --- Guardado de registro de crear en BD ---
        Registro registroCreacion = Registro.builder()
            .fechaCarga(new Date())
            .usuario(usuario)
            .documento(documentoGuardado)
            // (Añadir un campo "tipoOperacion" = "CREAR")
            .build();
        registroRepository.save(registroCreacion);
        logService.info("Documento creado exitosamente con ID: " + documentoGuardado.getIdDocumento() + " por " + userEmail);
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
     * @param userEmail
     * @return La entidad Documento actualizada.
     */
    @Transactional
    public Documento actualizarDocumento(Integer id, DocumentoDTO dto, String userEmail) {
        logService.info("Iniciando actualización de documento ID: " + id + " por " + userEmail);
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario autenticado no encontrado: " + userEmail));
        // VALIDACIÓN DE numDocumento ÚNICO ---
        Optional<Documento> docConMismoNumero = documentoRepo.findByNumDocumento(dto.getNumDocumento());

        if (docConMismoNumero.isPresent() && !docConMismoNumero.get().getIdDocumento().equals(id)) {
            // Solo lanza error si el ID del documento encontrado es DIFERENTE al que estamos editando
            logService.warn("Conflicto de unicidad al actualizar documento ID: " + id + ". numDocumento " +
                dto.getNumDocumento() + " ya existe en ID: " + docConMismoNumero.get().getIdDocumento());
            throw new RecursoDuplicadoException("El número de documento " + dto.getNumDocumento() + " ya está en uso por otro documento.");
        }

        // --- VALIDACIÓN DE Título ÚNICO ---
        Optional<Documento> docConMismoTitulo = documentoRepo.findByTitulo(dto.getTitulo());
        if (docConMismoTitulo.isPresent() && !docConMismoTitulo.get().getIdDocumento().equals(id)) {
            logService.warn("Conflicto de unicidad al actualizar titulo ID: " + id);
            throw new RecursoDuplicadoException("El título '" + dto.getTitulo() + "' ya está en uso por otro documento.");
        }
        
        // --- BÚSQUEDA DEL DOCUMENTO EXISTENTE ---
         Documento docExistente = buscarDocumentoPorId(id);

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
        // Forzamos la inicialización de las colecciones ANTES de salir del método @Transactional
        // Simplemente "tocando" las colecciones, Hibernate las cargará.
        actualizado.getArchivos().size(); 
        actualizado.getPalabrasClave().size();
        actualizado.getReferencias().size();
        actualizado.getReferenciadoPor().size();
        // --- --- Guardado de registro de actualización en BD --- ---
        Registro registroEdicion = Registro.builder()
                .fechaCarga(new Date()) // Fecha de la edición
                .usuario(usuario)
                .documento(actualizado)
                // (Añadir un campo "tipoOperacion" = "EDITAR")
                .build();
        registroRepository.save(registroEdicion);
        logService.info("Documento ID: " + id + " actualizado y registrado por " + userEmail);
        return actualizado;
    }

    // --- Método de Borrado ---

    /**
     * Elimina un documento por su ID.
     * Primero verifica que exista.
     * @param id El ID del documento a eliminar.
     * @param userEmail El email del usuario que realiza la eliminación.
     * @throws RecursoNoEncontradoException si el documento no existe.
    */
    @Transactional
    public void eliminarDocumento(Integer id, String userEmail) {
        logService.info("Iniciando eliminación de documento ID: " + id + " por " + userEmail);
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario autenticado no encontrado: " + userEmail));
        Documento docExistente = buscarDocumentoPorId(id); // Valida que existe

        Registro registroEliminacion = Registro.builder()
            .fechaCarga(new Date()) // Fecha de la eliminación
            .usuario(usuario)
            .documento(docExistente)
            // (Añadir un campo "tipoOperacion" = "ELIMINAR")
            .build();
        registroRepository.save(registroEliminacion);

        logService.debug("Borrando " + docExistente.getArchivos().size() + " archivos físicos asociados...");

        // Copiamos la lista para evitar ConcurrentModificationException
        List<Archivo> archivosABorrar = new ArrayList<>(docExistente.getArchivos());

        for (Archivo archivo : archivosABorrar) {
             archivoService.borrarArchivo(archivo.getIdArchivo()); // Llama al servicio que SÍ borra del disco
        }
         
        documentoRepo.delete(docExistente);
        logService.info("Documento ID: " + id + " eliminado exitosamente.");
    }

    /**
     * Cuenta documentos agrupados por tipo.
     * @return Map con idTipoDocumento -> cantidad
     */
    @Transactional(readOnly = true)
    public Map<Integer, Long> contarPorTipo() {
        logService.info("Contando documentos por tipo");

        List<TipoDocumento> tipos = tipoDocumentoRepo.findAll();
        Map<Integer, Long> conteos = new HashMap<>();

        for (TipoDocumento tipo : tipos) {
            Long count = documentoRepo.countByTipoDocumento_IdTipoDocumento(tipo.getIdTipoDocumento());
            conteos.put(tipo.getIdTipoDocumento(), count);
        }

        return conteos;
    }
}
