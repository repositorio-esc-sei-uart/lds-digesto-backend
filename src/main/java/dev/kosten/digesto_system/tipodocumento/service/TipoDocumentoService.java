package dev.kosten.digesto_system.tipodocumento.service;

import dev.kosten.digesto_system.documento.repository.DocumentoRepository;
import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import dev.kosten.digesto_system.tipodocumento.repository.TipoDocumentoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para la gestión de TipoDocumento.
 * @author micael
 * @author Quique
 */

@Service
@RequiredArgsConstructor
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final DocumentoRepository documentoRepository;

    /**
     * Recupera una lista de todos los Tipos de Documento.
     * @return una Lista de entidades TipoDocumento.
     */
    @Transactional(readOnly = true)
    public List<TipoDocumento> listarTodos() {
        return tipoDocumentoRepository.findAll();
    }
    
    /**
     * Busca y devuelve un TipoDocumento por su clave primaria (ID).
     * @param id La clave primaria del tipo a buscar.
     * @return La entidad TipoDocumento correspondiente.
     * @throws RecursoNoEncontradoException si no se encuentra ningún TipoDocumento con ese ID.
     */
    @Transactional(readOnly = true) // <-- 10. AÑADIR
    public TipoDocumento obtenerPorId(Integer id) {
        return tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoDocumento no encontrado con ID: " + id));
    }

    /**
     * Crea y persiste un nuevo TipoDocumento.
     * Valida que no exista otro TipoDocumento con el mismo nombre.
     * @param tipoDocumento La entidad TipoDocumento a crear.
     * @return La entidad TipoDocumento persistida.
     * @throws RecursoDuplicadoException si ya existe un TipoDocumento con ese nombre.
     */
    @Transactional // <-- 10. AÑADIR
    public TipoDocumento crearTipoDocumento(TipoDocumento tipoDocumento) {
        Optional<TipoDocumento> existente = tipoDocumentoRepository.findByNombre(tipoDocumento.getNombre());
        if (existente.isPresent()) {
            throw new RecursoDuplicadoException("Ya existe un TipoDocumento con el nombre: " + tipoDocumento.getNombre());
        }
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    /**
     * Actualiza un TipoDocumento existente.
     * @param id El ID del TipoDocumento a actualizar.
     * @param datosNuevos La entidad TipoDocumento con los datos a modificar.
     * @return La entidad TipoDocumento actualizada.
     * @throws RecursoNoEncontradoException si el ID no existe.
     * @throws RecursoDuplicadoException si se intenta renombrar a un nombre que ya existe.
     */
    @Transactional
    public TipoDocumento actualizarTipoDocumento(Integer id, TipoDocumento datosNuevos) {
        Optional<TipoDocumento> existenteConMismoNombre = tipoDocumentoRepository.findByNombre(datosNuevos.getNombre());
        if (existenteConMismoNombre.isPresent() && !existenteConMismoNombre.get().getIdTipoDocumento().equals(id)) {
            throw new RecursoDuplicadoException("El nombre '" + datosNuevos.getNombre() + "' ya está en uso por otro TipoDocumento.");
        }
        TipoDocumento tipoExistente = obtenerPorId(id);
        tipoExistente.setNombre(datosNuevos.getNombre());
        tipoExistente.setDescripcion(datosNuevos.getDescripcion());
        return tipoDocumentoRepository.save(tipoExistente);
    }

    /**
     * Elimina permanentemente un TipoDocumento.
     * @param id El ID del TipoDocumento a eliminar.
     * @throws RecursoNoEncontradoException si el ID no existe.
     * @throws RecursoDuplicadoException si el tipo está en uso por documentos.
     */
    @Transactional // <-- 10. AÑADIR
    public void eliminarTipoDocumento(Integer id) {
        TipoDocumento tipoExistente = obtenerPorId(id);
        boolean enUso = documentoRepository.existsByTipoDocumento(tipoExistente);
        if (enUso) {
            throw new RecursoDuplicadoException(
                "No se puede eliminar el tipo '" + tipoExistente.getNombre() + "' porque está siendo utilizado por documentos."
            );
        }
        
        tipoDocumentoRepository.delete(tipoExistente);
    }
}
