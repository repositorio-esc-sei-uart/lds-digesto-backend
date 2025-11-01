package dev.kosten.digesto_system.archivo.entity;

import dev.kosten.digesto_system.documento.entity.Documento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un archivo físico (.PDF) asociado a un Documento.
 * Cada registro en esta tabla es un puntero a un archivo en el sistema
 * y está obligatoriamente vinculado a una entidad Documento.
 * @author micael
 * @author Quique
 */
@Entity
@Table(name = "archivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Archivo {

    /**
     * Identificador único del archivo (Clave Primaria).
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArchivo") // Buena práctica
    private Integer idArchivo;

    /**
     * Nombre original del archivo (ej. "anexo_I.pdf").
     */
    @Column(name = "nombre", length = 45, nullable = false) 
    private String nombre;

    /**
     * Ruta relativa o URL donde se almacena el archivo físico.
     * Esta ruta es utilizada por el FileStorageService y el frontend.
     * (ej. "/uploads/documentos/1/anexo_I.pdf")
     */
    @Column(name = "url", length = 255, nullable = false) 
    private String url; 

    /**
     * (FK) El Documento "padre" al que pertenece este archivo.
     * Es una relación obligatoria (nullable = false).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_idDocumento", nullable = false) 
    private Documento documento;
}
