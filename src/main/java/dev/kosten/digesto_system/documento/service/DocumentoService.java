package dev.kosten.digesto_system.documento.service;

import dev.kosten.digesto_system.documento.dto.DocumentoDTO;
import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.documento.repository.DocumentoRepository;
import dev.kosten.digesto_system.estado.entity.Estado;
import dev.kosten.digesto_system.estado.repository.EstadoRepository;
import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;
import dev.kosten.digesto_system.palabraclave.repository.PalabraClaveRepository;
import dev.kosten.digesto_system.sector.Sector;
import dev.kosten.digesto_system.sector.SectorRepository;
import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import dev.kosten.digesto_system.tipodocumento.repository.TipoDocumentoRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio de negocio para la entidad Documento.
 * @author micae
 */
@Service
@RequiredArgsConstructor
public class DocumentoService {
    
    /**
     * Repositorios inyectados automáticamente por el constructor
     * al ser declarados como 'final'.
     */
    private final DocumentoRepository documentoRepo;
    private final TipoDocumentoRepository tipoDocumentoRepo;
    private final EstadoRepository estadoRepo;
    private final SectorRepository sectorRepo;
    private final PalabraClaveRepository palabraClaveRepo;
    //@Autowired private DocumentoRepository documentoRepo;
    //@Autowired private TipoDocumentoRepository tipoDocumentoRepo;
    //@Autowired private EstadoRepository estadoRepo;
    //@Autowired private SectorRepository sectorRepo;
    //@Autowired private PalabraClaveRepository palabraClaveRepo;


    // --- Métodos de Lectura ---

    /**
     * Devuelve una lista de todas las entidades Documento.
     * @return Lista de Documento.
     */
    public List<Documento> listarTodos() {
        return documentoRepo.findAll();
    }

    /**
     * Busca un Documento por su ID.
     * @param id El ID del documento a buscar.
     * @return La entidad {@link Documento} encontrada.
     * @throws RecursoNoEncontradoException si el documento no existe.
     */
    public Documento obtenerPorId(Integer id) {
        return documentoRepo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Documento no encontrado con ID: " + id));
    }

    // --- Método de Creación ---
    /**
     * Crea una nueva entidad Documento a partir de un DTO.
     * Valida la unicidad del 'numDocumento' y busca todas las entidades relacionadas
     * por sus IDs antes de persistir.
     * @param dto El DocumentoDTO con los datos para la creación.
     * @return La entidad Documento persistida.
     * @throws RecursoDuplicadoException si 'numDocumento' ya existe.
     * @throws RecursoNoEncontradoException si algún ID de catálogo (Estado, Sector, etc.) no existe.
     */
    @Transactional // Buena práctica para operaciones que tocan varias tablas
    public Documento crearDocumento(DocumentoDTO dto) {

        // --- VALIDACIÓN DE numDocumento ÚNICO ---
        documentoRepo.findByNumDocumento(dto.getNumDocumento()).ifPresent(doc -> {
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
        return documentoRepo.save(nuevoDocumento);
    }

    // --- Método de Actualización ---
    /**
     * Actualiza un documento existente.
     * Busca el documento por ID, valida la unicidad del 'numDocumento' y
     * actualiza todos los campos y relaciones con los datos del DTO.
     * @param id El ID del documento a actualizar.
     * @param dto El DocumentoDTO con los nuevos datos.
     * @return La entidad Documento actualizada.
     */
    @Transactional
    public Documento actualizarDocumento(Integer id, DocumentoDTO dto) {

        
        // --- VALIDACIÓN DE numDocumento ÚNICO ---
        documentoRepo.findByNumDocumento(dto.getNumDocumento()).ifPresent(doc -> {
            // Solo lanza error si el 'numDocumento' encontrado pertenece a OTRO documento
            if (!doc.getIdDocumento().equals(id)) {
                throw new RecursoDuplicadoException("El número de documento '" + dto.getNumDocumento() + "' ya está en uso.");
            }
        });

        // --- BÚSQUEDA DEL DOCUMENTO EXISTENTE ---
        // (Usa el método obtenerPorId que ya maneja la excepción si no existe)
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
        return documentoRepo.save(docExistente);
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
        Documento docExistente = obtenerPorId(id); // Reutiliza la validación
        documentoRepo.delete(docExistente);
    }
}
