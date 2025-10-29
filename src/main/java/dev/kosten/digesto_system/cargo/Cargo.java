/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.cargo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Entidad que representa un cargo dentro del sistema.
 * Contiene la información básica del cargo, como su nombre y descripción.
 * 
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "cargo")
public class Cargo {
    
    /**
     * Identificador único del cargo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCargo")
    private Integer idCargo;
    
    /**
     * Nombre del cargo.
     * Debe ser único y no nulo.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    /**
     * Descripción breve del cargo.
     */
    @Column(name = "descripcion")
    private String descripcion;
    
}
