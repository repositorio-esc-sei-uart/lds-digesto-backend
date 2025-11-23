package dev.kosten.digesto_system.unidadEjecutora;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un unidadEjecutora dentro de el sistema.
 * Contiene la información básica de la unidadEjecutora, como su nombre y descripción.
 * 
 * Mapeada a la tabla "unidadEjecutora" en la base de datos.
 * @author Matias
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "UnidadEjecutora")

public class UnidadEjecutora {
    
    /**
     * Identificador único de la UnidadEjecutora.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUnidadEjecutora")
    private Integer idUnidadEjecutora;
    
    /**
     * Nombre de la unidadEjecutora.
     * Debe ser único y no nulo.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    /**
     * Descripción breve de la UnidadEjecutora.
     */
    @Column(name = "descripcion")
    private String descripcion;
    
     /**
     * siglas que simbolizan a que UnidadEjecutora pertence el documento.
     */
    @Column(name = "nomenclatura", length = 10, nullable = false)
    private String nomenclatura;
}

