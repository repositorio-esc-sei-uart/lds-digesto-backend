package dev.kosten.digesto_system.registro.repository;

import dev.kosten.digesto_system.registro.entity.Registro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad de auditoría {@link Registro}.
 * @author Quique
 */
@Repository
public interface RegistroRepository extends JpaRepository <Registro, Integer> {

    /**
    * Busca todos los registros, trayendo las relaciones
    * de Usuario y Documento en la misma consulta para evitar N+1.
     * @return 
    */
    @Query("SELECT r FROM Registro r JOIN FETCH r.usuarioResponsable u JOIN FETCH r.documentoAfectado d ORDER BY r.fechaCarga DESC")
    List<Registro> findAllWithDetails();

    /**
    * Busca todos los registros de un documento Afectado, trayendo las relaciones.
     * @param idDocumento
     * @return 
    */
    @Query("SELECT r FROM Registro r JOIN FETCH r.usuarioResponsable u JOIN FETCH r.documentoAfectado d WHERE d.idDocumento = :idDocumento ORDER BY r.fechaCarga DESC")
    List<Registro> findByDocumentoIdDocumentoWithDetails(@Param("idDocumento") Integer idDocumento);
    
    /**
    * Busca todos los registros de un usuario Responsable, trayendo las relaciones.
     * @param idUsuario
     * @return 
    */
    @Query("SELECT r FROM Registro r JOIN FETCH r.usuarioResponsable u JOIN FETCH r.documentoAfectado d WHERE u.idUsuario = :idUsuario ORDER BY r.fechaCarga DESC")
    List<Registro> findByUsuarioIdUsuarioWithDetails(@Param("idUsuario") Integer idUsuario);

    // No se usan
    /**
     * Busca todos los registros de auditoría asociados a un ID de documento Afectado.
     * @param idDocumento El ID del documentoAfectado a consultar.
     * @return Una lista de Registros.
     */
    // List<Registro> findByDocumentoIdDocumento(Integer idDocumento);
    
    /**
     * Busca todos los registros asociados a un ID de usuario Responsable.
     * @param idUsuario El ID del usuarioResponsable (editor) a consultar.
     * @return Una lista de Registros.
     */
    // List<Registro> findByUsuarioIdUsuario(Integer idUsuario);
}
