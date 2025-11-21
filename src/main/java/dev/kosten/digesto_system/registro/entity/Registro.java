/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.registro.entity;

import dev.kosten.digesto_system.documento.entity.Documento;
import dev.kosten.digesto_system.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad que representa un registro de auditoría.
 * Mapea la tabla 'registro' de la base de datos.
 * Vincula un Documento con el Usuario que realizó una operación (crear, editar, borrar)
 * y la fecha en que lo hizo.
 * @author Quique
 * @author Matias
 */
@Entity
@Table(name = "registro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Registro {

    /**
     * Identificador único del registro (Clave Primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRegistro")
    @EqualsAndHashCode.Include // El ID define la igualdad
    @ToString.Include // El ID se muestra en el toString
    private Integer idRegistro;

    /**
     * Fecha en que se realizó la operación (carga, edición, borrado).
     * Mapeado como DATE en SQL.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "fechaCarga")
    private Date fechaCarga;
    /**
     * palabra que simboliza que operacion se realizo (carga, edición, borrado).
     */
    @Column(name = "tipoOperacion", length = 45, nullable = false)
    private String tipoOperacion;
    /**
     * (FK) El Usuario que realizó la operación.
     * Relación obligatoria (nullable = false).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioResponsable_idUsuario", nullable = false)
    private Usuario usuarioResponsable;

    /**
     * (FK) El Documento que fue afectado por la operación.
     * Relación opcional (nullable = true)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentoAfectado_idDocumento", nullable = true)
    private Documento documentoAfectado;
     /**
     * (FK) El usuario que fue afectado por la operación.
     * Relación opcional (nullable = true).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioAfectado_idUsuario", nullable = true)
    private Usuario usuarioAfectado;
}
