/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author micae
 */
@Entity
@Table(name = "archivo")
@Getter
@Setter
@NoArgsConstructor
public class Archivo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArchivo") // Buena práctica
    private Integer idArchivo;

    // Script dice UNIQUE NULL
    @Column(name = "nombre", length = 45, unique = true) 
    private String nombre;

    // Script dice VARCHAR(255) NULL
    @Column(name = "url", length = 255) 
    private String url; 

    @ManyToOne(fetch = FetchType.LAZY)
    // Script dice: documento_idDocumento (Correcto)
    @JoinColumn(name = "documento_idDocumento", nullable = false) 
    private Documento documento;
}
