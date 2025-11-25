package dev.kosten.digesto_system.registro.repository;

import dev.kosten.digesto_system.registro.entity.Registro;
import dev.kosten.digesto_system.usuario.Usuario; // <--- ¡IMPORTANTE! AGREGAR ESTE IMPORT
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
     * Busca todos los registros.
     * CAMBIO IMPORTANTE: Usamos LEFT JOIN FETCH para las entidades que pueden ser nulas.
     */
    @Query("SELECT r FROM Registro r " +
           "JOIN FETCH r.usuarioResponsable ur " +       // El responsable SIEMPRE existe (Inner Join)
           "LEFT JOIN FETCH r.documentoAfectado da " +   // Puede ser NULL (Left Join)
           "LEFT JOIN FETCH r.usuarioAfectado ua " +     // Puede ser NULL (Left Join)
           "ORDER BY r.fechaCarga DESC")
    List<Registro> findAllWithDetails();

    /**
     * Busca registros por Documento Afectado.
     */
    @Query("SELECT r FROM Registro r " +
           "JOIN FETCH r.usuarioResponsable ur " +
           "JOIN FETCH r.documentoAfectado da " +        // Aquí sí es JOIN normal porque filtramos por ID de documento
           "LEFT JOIN FETCH r.usuarioAfectado ua " +
           "WHERE da.idDocumento = :idDocumento ORDER BY r.fechaCarga DESC")
    List<Registro> findByDocumentoIdDocumentoWithDetails(@Param("idDocumento") Integer idDocumento);
    
    /**
     * Busca registros por Usuario Responsable (Para mostrar en la tabla).
     */
    @Query("SELECT r FROM Registro r " +
           "JOIN FETCH r.usuarioResponsable ur " +
           "LEFT JOIN FETCH r.documentoAfectado da " +
           "LEFT JOIN FETCH r.usuarioAfectado ua " +
           "WHERE ur.idUsuario = :idUsuario ORDER BY r.fechaCarga DESC")
    List<Registro> findByUsuarioIdUsuarioWithDetails(@Param("idUsuario") Integer idUsuario);

    // --- MÉTODOS PARA DESVINCULAR (NECESARIOS PARA BORRADO FÍSICO) ---

    /**
     * Busca registros donde el usuario dado es el RESPONSABLE.
     * Spring Data JPA genera la query automáticamente por el nombre del método.
     */
    List<Registro> findByUsuarioResponsable(Usuario usuario);

    /**
     * Busca registros donde el usuario dado es el AFECTADO.
     * Spring Data JPA genera la query automáticamente por el nombre del método.
     */
    List<Registro> findByUsuarioAfectado(Usuario usuario);
}