/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author micae
 */
@Service
public class DocumentoService {
    
    // --- Inyecciones (Asegúrate de tenerlas todas) ---
    @Autowired private DocumentoRepository documentoRepo;
    @Autowired private TipoDocumentoRepository tipoDocumentoRepo;
    @Autowired private EstadoRepository estadoRepo;
    @Autowired private SectorRepository sectorRepo;
    @Autowired private PalabraClaveRepository palabraClaveRepo;


    // --- Métodos de Lectura (Sin cambios) ---
    public List<Documento> listarTodos() {
        return documentoRepo.findAll();
    }

    public Documento obtenerPorId(Integer id) {
        return documentoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Documento no encontrado con ID: " + id));
    }


    // --- Método de Creación (CON LA VALIDACIÓN AÑADIDA) ---
    @Transactional // Buena práctica para operaciones que tocan varias tablas
    public Documento crearDocumento(DocumentoDTO dto) {

        // --- VALIDACIÓN DE numDocumento ÚNICO ---
        documentoRepo.findByNumDocumento(dto.getNumDocumento()).ifPresent(doc -> {
            throw new RecursoDuplicadoException("Ya existe un documento con el número: " + dto.getNumDocumento());
        });

        // --- BÚSQUEDA DE ENTIDADES RELACIONADAS (CON orElseThrow CORREGIDO) ---
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
        
        // --- Búsqueda de Referencias (ESTA LÓGICA TE FALTABA) ---
        Set<Documento> referencias = new HashSet<>(); // Inicializar siempre
         if (dto.getIdsReferencias() != null && !dto.getIdsReferencias().isEmpty()) {
             List<Documento> referenciasEncontradas = documentoRepo.findAllById(dto.getIdsReferencias()); 
             referencias.addAll(referenciasEncontradas); 
         }

        // --- Construcción de la Nueva Entidad ---
        Documento nuevoDocumento = new Documento();
        nuevoDocumento.setTitulo(dto.getTitulo());
        nuevoDocumento.setResumen(dto.getResumen());
        nuevoDocumento.setNumDocumento(dto.getNumDocumento()); // Usa String
        nuevoDocumento.setFechaCreacion(new Date());

        // --- Asignación de TODAS las Relaciones ---
        nuevoDocumento.setTipoDocumento(tipo);
        nuevoDocumento.setEstado(estado);
        nuevoDocumento.setSector(sector);
        nuevoDocumento.setPalabrasClave(etiquetas);
        nuevoDocumento.setReferencias(referencias); // <-- ESTA ASIGNACIÓN FALTABA

        // --- Guardado en BD ---
        return documentoRepo.save(nuevoDocumento);
    }

    // --- Método de Actualización (CON LA VALIDACIÓN AÑADIDA y REEMPLAZO SEGURO DE COLECCIONES) ---
    @Transactional // Muy importante para actualizaciones
    public Documento actualizarDocumento(Integer id, DocumentoDTO dto) {

        
        // --- VALIDACIÓN DE numDocumento ÚNICO ---
        documentoRepo.findByNumDocumento(dto.getNumDocumento()).ifPresent(doc -> {
            if (!doc.getIdDocumento().equals(id)) { // Si existe Y NO es el mismo doc
                throw new RecursoDuplicadoException("El número de documento '" + dto.getNumDocumento() + "' ya está en uso.");
            }
        });

        // --- BÚSQUEDA DEL DOCUMENTO EXISTENTE ---
        // (Usa el método obtenerPorId que ya maneja la excepción si no existe)
        Documento docExistente = obtenerPorId(id);

        // --- BÚSQUEDA DE NUEVAS RELACIONES (CON orElseThrow CORREGIDO) ---
        TipoDocumento tipo = tipoDocumentoRepo.findById(dto.getIdTipoDocumento())
                // Reemplazaste '...' con el código completo
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo de documento no existe con ID: " + dto.getIdTipoDocumento()));

        Estado estado = estadoRepo.findById(dto.getIdEstado())
                // Reemplazaste '...' con el código completo
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no existe con ID: " + dto.getIdEstado()));

        Sector sector = sectorRepo.findById(dto.getIdSector())
                // Reemplazaste '...' con el código completo
                .orElseThrow(() -> new RecursoNoEncontradoException("Sector no existe con ID: " + dto.getIdSector()));

        // --- ACTUALIZACIÓN SEGURA DE COLECCIONES (Palabras Clave) ---
        Set<PalabraClave> nuevasEtiquetasSet = new HashSet<>();
        if (dto.getIdsPalabrasClave() != null && !dto.getIdsPalabrasClave().isEmpty()) {
            nuevasEtiquetasSet.addAll(palabraClaveRepo.findAllById(dto.getIdsPalabrasClave()));
        }
        docExistente.setPalabrasClave(nuevasEtiquetasSet); // Reemplaza la colección

        // --- ACTUALIZACIÓN SEGURA DE COLECCIONES (Referencias) ---
         Set<Documento> nuevasReferenciasSet = new HashSet<>();
         if (dto.getIdsReferencias() != null && !dto.getIdsReferencias().isEmpty()) {
             nuevasReferenciasSet.addAll(documentoRepo.findAllById(dto.getIdsReferencias()));
         }
         docExistente.setReferencias(nuevasReferenciasSet); // Reemplaza la colección

        // --- ACTUALIZACIÓN DE CAMPOS SIMPLES ---
        docExistente.setTitulo(dto.getTitulo());
        docExistente.setResumen(dto.getResumen());
        docExistente.setNumDocumento(dto.getNumDocumento()); // Usa String

        // --- ACTUALIZACIÓN DE RELACIONES SIMPLES ---
        docExistente.setTipoDocumento(tipo);
        docExistente.setEstado(estado);
        docExistente.setSector(sector);

        // --- GUARDADO EN BD ---
        return documentoRepo.save(docExistente);
    }

    // --- Método de Borrado (Sin cambios) ---
    @Transactional
    public void eliminarDocumento(Integer id) {
        Documento docExistente = obtenerPorId(id);
        documentoRepo.delete(docExistente);
    }
    
}
