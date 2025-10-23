/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.documento.entity;

import dev.labintec.digesto_system.archivo.entity.Archivo;
import dev.labintec.digesto_system.estado.entity.Estado;
import dev.labintec.digesto_system.palabraclave.entity.PalabraClave;
import dev.labintec.digesto_system.sector.Sector;

import dev.labintec.digesto_system.tipodocumento.entity.TipoDocumento;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 *
 * @author micae
 */

/**
 * Entidad que representa la tabla 'documento' en la base de datos.
 * Es el molde principal para un Documento Normativo (ODN).
 */
@Entity
@Table(name = "documento")
@Getter
@Setter
@NoArgsConstructor
public class Documento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDocumento") // Buena práctica añadir el name
    private Integer idDocumento;

    @Column(name = "titulo",length = 60) // Script dice VARCHAR(60)
    private String titulo;

    @Column(name = "resumen",length = 145) // Script dice VARCHAR(145)
    private String resumen;

    @Temporal(TemporalType.DATE)
    @Column(name = "fechaCreacion") // Buena práctica añadir el name
    private Date fechaCreacion;

    // Correcto: String y unique basado en el último script
    @Column(name = "numDocumento", length = 45, unique = true) 
    private String numDocumento;

    // --- Relaciones ---
    @ManyToOne(fetch = FetchType.LAZY)
    // Script dice: tipoDocumento_idTipoDocumento (Correcto)
    @JoinColumn(name = "tipoDocumento_idTipoDocumento", nullable = false) 
    private TipoDocumento tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    // Script dice: estado_idestado (Correcto)
    @JoinColumn(name = "estado_idestado", nullable = false) 
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
     // Script dice: sector_idSector (Correcto)
    @JoinColumn(name = "sector_idSector", nullable = false)
    private Sector sector;

    // La relación @OneToMany no necesita @JoinColumn aquí,
    // se define en Archivo.java con mappedBy = "documento". (Correcto)
    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Archivo> archivos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "etiqueta",
        // Script dice: documento_idDocumento (Correcto)
        joinColumns = @JoinColumn(name = "documento_idDocumento"), 
        // Script dice: palabraClave_idPalabraClave (Correcto)
        inverseJoinColumns = @JoinColumn(name = "palabraClave_idPalabraClave") 
    )
    private Set<PalabraClave> palabrasClave = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "referencia",
        // Script dice: documento_idDocumentoOrigen (Correcto)
        joinColumns = @JoinColumn(name = "documento_idDocumentoOrigen"),
        // Script dice: documento_idDocumentoReferencial (Correcto)
        inverseJoinColumns = @JoinColumn(name = "documento_idDocumentoReferencial")
    )
    private Set<Documento> referencias = new HashSet<>();
}
