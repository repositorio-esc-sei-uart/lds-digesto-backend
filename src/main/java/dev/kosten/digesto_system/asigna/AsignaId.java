/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.asigna;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa la clave primaria compuesta de la entidad {@link Asigna}.
 * 
 * Combina el ID del {@link Rol} y el ID del {@link Permiso} para formar una PK única.
 * Debe implementar {@link Serializable} y sobrescribir los métodos {@code equals} y {@code hashCode}
 * para que JPA pueda manejar correctamente la identidad de la entidad.
 * 
 */
@Getter
@Setter
@Embeddable
public class AsignaId implements Serializable {
    
    /**
     * Identificador del rol.
     */
    @Column(name = "rol_idRol")
    private Integer rolIdRol;
    
    /**
     * Identificador del permiso.
     */
    @Column(name = "permiso_idpermiso")
    private Integer permisoIdpermiso;
    
    /**
     * Constructor vacío necesario para JPA.
     */
    public AsignaId() {}
    
    /**
     * Constructor con parámetros que inicializa la clave compuesta.
     * 
     * @param rolIdRol identificador del rol
     * @param permisoIdpermiso identificador del permiso
     */
    public AsignaId(Integer rolIdRol, Integer permisoIdpermiso) {
        this.rolIdRol = rolIdRol;
        this.permisoIdpermiso = permisoIdpermiso;
    }

    // equals y hashCode obligatorios
    
     /**
     * Compara esta clave compuesta con otro objeto para determinar igualdad.
     * 
     * @param o objeto a comparar
     * @return {@code true} si ambos objetos representan la misma clave compuesta
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AsignaId)) return false;
        AsignaId that = (AsignaId) o;
        return Objects.equals(rolIdRol, that.rolIdRol) &&
               Objects.equals(permisoIdpermiso, that.permisoIdpermiso);
    }
    
    /**
     * Calcula el código hash basado en los IDs de rol y permiso.
     * 
     * @return código hash de la clave compuesta
     */
    @Override
    public int hashCode() {
        return Objects.hash(rolIdRol, permisoIdpermiso);
    }
}
