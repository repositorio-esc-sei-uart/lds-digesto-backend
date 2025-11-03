package dev.kosten.digesto_system.estado.repository;

import dev.kosten.digesto_system.estado.entity.Estado;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad Estado.
 * Proporciona métodos CRUD estándar heredados de JpaRepository
 * y consultas personalizadas para la gestión de estados de documentos.
 * @author micael
 * @author Quique
 */
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer>{
    /**
     * Busca un Estado por su nombre único.
     * Este método es crucial para las validaciones de negocio en la capa de servicio,
     * permitiendo comprobar si un estado con un nombre específico ya existe
     * antes de una operación de creación o actualización.
     * @param nombre El nombre exacto del estado a buscar.
     * @return Un Optional que contiene el Estado si se encuentra,
     * o un Optional vacío si no existe.
     */
    Optional<Estado> findByNombre(String nombre);
}
