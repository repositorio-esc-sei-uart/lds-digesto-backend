package dev.kosten.digesto_system.tipodocumento.repository;

import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author micae
 */
@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer>{

    /**
     * Busca un TipoDocumento por su nombre único.
     * @param nombre El nombre exacto del tipo a buscar.
     * @return Un {@link Optional} que contiene el {@link TipoDocumento} si se encuentra.
     */
    public Optional<TipoDocumento> findByNombre(String nombre);
}
