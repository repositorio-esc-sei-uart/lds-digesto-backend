/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.asigna;

import dev.kosten.digesto_system.permiso.Permiso;
import dev.kosten.digesto_system.rol.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;


/**
 * Entidad que representa la asignación de permisos a roles.
 * 
 * Cada registro indica que un {@link Rol} tiene un {@link Permiso} asociado.
 * Utiliza una clave primaria compuesta {@link AsignaId} que combina el ID del rol y del permiso.
 * 
 * Mapeada a la tabla "asigna" en la base de datos.
 * 
 * Permite gestionar relaciones muchos a muchos entre roles y permisos de manera explícita.
 * 
 */
@Entity
@Data
@Table(name = "asigna")

public class Asigna {
    
    /**
     * Clave primaria compuesta que combina rol y permiso.
     */
    @EmbeddedId
    private AsignaId id;
    
    /**
     * Rol asociado a este registro.
     * Relación muchos a uno con la entidad {@link Rol}.
     */
    @ManyToOne
    @MapsId("rolIdRol") // vincula con el campo de la PK compuesta
    @JoinColumn(name = "rol_idRol", nullable = false)
    private Rol rol;
    
    /**
     * Permiso asociado a este registro.
     * Relación muchos a uno con la entidad {@link Permiso}.
     */
    @ManyToOne
    @MapsId("permisoIdpermiso")
    @JoinColumn(name = "permiso_idpermiso", nullable = false)
    private Permiso permiso;
    
    /**
     * Constructor vacío necesario para JPA.
     */
    public Asigna() {}
    
    /**
     * Constructor que crea una asignación entre un rol y un permiso.
     * 
     * @param rol rol a asignar
     * @param permiso permiso a asignar
     */
    public Asigna(Rol rol, Permiso permiso) {
        this.rol = rol;
        this.permiso = permiso;
        this.id = new AsignaId(rol.getIdRol(), permiso.getIdPermiso());
    }

}




