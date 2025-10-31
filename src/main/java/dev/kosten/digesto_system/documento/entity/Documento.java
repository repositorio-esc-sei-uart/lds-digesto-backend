package dev.kosten.digesto_system.documento.entity;

import dev.kosten.digesto_system.archivo.entity.Archivo;
import dev.kosten.digesto_system.estado.entity.Estado;
import dev.kosten.digesto_system.palabraclave.entity.PalabraClave;
import dev.kosten.digesto_system.sector.Sector;
import dev.kosten.digesto_system.tipodocumento.entity.TipoDocumento;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidad que representa la tabla 'documento' en la base de datos.
 * Es el molde principal para un Documento Normativo (ODN).
 * @author micael
 * @author Quique
 */
@Entity
@Table(name = "documento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documento {

    // --- Bloque de Identificación y Atributos Principales ---

    /**
     * Identificador único del documento (Clave Primaria).
     * Generado automáticamente por la base de datos (Auto-Incremental).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDocumento")
    private Integer idDocumento;

    /**
     * Título principal y descriptivo del documento.
     */
    @Column(name = "titulo",length = 60)
    private String titulo;

    /**
     * Breve descripción o sumario del contenido del documento.
     */
    @Column(name = "resumen",length = 145)
    private String resumen;

    /**
     * Fecha de creación o sanción del documento.
     * Mapeado como java.util.Date, almacenado como DATE en SQL.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "fechaCreacion")
    private Date fechaCreacion;

    /**
     * Número o código identificatorio único (ej. "RES-2024-001").
     * Se marca como 'unique = true' para evitar duplicados a nivel de BD.
     */
    @Column(name = "numDocumento", length = 45, unique = true) 
    private String numDocumento;

    // --- Bloque de Relaciones @ManyToOne (Clasificadores / FK) ---

    /**
     * (FK) El Tipo de Documento al que pertenece.
     * Es una relación obligatoria (nullable = false).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipoDocumento_idTipoDocumento", nullable = false) 
    private TipoDocumento tipoDocumento;

    /**
     * (FK) El Estado actual del documento (ej. "Vigente", "Derogado").
     * Es una relación obligatoria (nullable = false).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_idestado", nullable = false) 
    private Estado estado;

    /**
     * (FK) El Sector que emitió el documento.
     * Es una relación obligatoria (nullable = false).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_idSector", nullable = false)
    private Sector sector;

    // --- Bloque de Relaciones @OneToMany ---

    /**
     * Lista de archivos físicos adjuntos a este documento.
     * La relación es gestionada por el campo "documento" en la entidad Archivo.
     * Se usa CascadeType.ALL y orphanRemoval=true para que los archivos
     * se borren automáticamente cuando se elimina este documento.
     */
    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Archivo> archivos = new ArrayList<>();;

    // --- Bloque de Relaciones @ManyToMany (Tablas Pivote) ---

    /**
     * Conjunto de Palabras Clave asociadas a este documento.
     * Mapeado a través de la tabla intermedia "etiqueta".
     * Se usa un Set para garantizar que no haya palabras clave duplicadas.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "etiqueta",
        joinColumns = @JoinColumn(name = "documento_idDocumento"), 
        inverseJoinColumns = @JoinColumn(name = "palabraClave_idPalabraClave") 
    )
    @Builder.Default
    private Set<PalabraClave> palabrasClave = new HashSet<>();

    /**
     * Conjunto de Documentos a los que este documento "hace referencia".
     * Mapeado a través de la tabla intermedia "referencia" (lado Origen).
     * (Este documento es el documento_idDocumentoOrigen).
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "referencia",
        joinColumns = @JoinColumn(name = "documento_idDocumentoOrigen"),
        inverseJoinColumns = @JoinColumn(name = "documento_idDocumentoReferencial")
    )
    @Builder.Default
    private Set<Documento> referencias = new HashSet<>();

    /**
     * (Relación Inversa) Conjunto de Documentos que "referencian a este".
     * Mapeado a través de la tabla intermedia "referencia" (lado Referencial).
     * Le dice a JPA que "referencias" es el dueño de esta relación.
     */
    @ManyToMany(mappedBy = "referencias", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Documento> referenciadoPor = new HashSet<>();
}
