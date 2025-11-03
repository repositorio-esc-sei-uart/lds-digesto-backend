package dev.kosten.digesto_system.estado.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad de persistencia que representa un Estado de documento.
 * Mapea la tabla estado de la base de datos y define las
 * categorías de vigencia de un documento (ej. "Vigente", "Derogado").
 * @author micael
 * @author Quique
 */
@Entity
@Table(name = "estado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstado")
    private Integer idEstado;

    @Column(name = "nombre", length = 45, unique = true, nullable = true)
    private String nombre;

    @Column(name = "descripcion", length = 60) 
    private String descripcion;
}
