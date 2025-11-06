package dev.kosten.digesto_system.registro.repository;

import dev.kosten.digesto_system.registro.entity.Registro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad de auditoría {@link Registro}.
 * @author Quique
 */
@Repository
public interface RegistroRepository extends JpaRepository <Registro, Integer> {
    
    /**
     * Busca todos los registros de auditoría asociados a un ID de documento.
     * @param idDocumento El ID del documento a consultar.
     * @return Una lista de Registros.
     */
    List<Registro> findByDocumentoIdDocumento(Integer idDocumento);
    
    /**
     * Busca todos los registros asociados a un ID de usuario.
     * @param idUsuario El ID del usuario (editor) a consultar.
     * @return Una lista de Registros.
     */
    List<Registro> findByUsuarioIdUsuario(Integer idUsuario);
}
