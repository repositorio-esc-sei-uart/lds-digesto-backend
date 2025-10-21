/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.usuario;


import dev.labintec.digesto_system.cargo.Cargo;
import dev.labintec.digesto_system.estadoU.EstadoU;
import dev.labintec.digesto_system.sector.Sector;
import dev.labintec.digesto_system.rol.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 
 * Clase encargada de realizar la conversión entre objetos 
 * {@link Usuario} (entidad del modelo) y {@link UsuarioDTO} (objeto de transferencia de datos).
 * 
 * Permite transformar la información recibida desde la capa de presentación 
 * en entidades persistibles, y viceversa, sin exponer directamente 
 * los objetos del modelo a las capas externas.
 * 
 * Esta clase se anota con {@link Component} para que pueda ser gestionada 
 * automáticamente por el contenedor de Spring y utilizada mediante inyección de dependencias.
 * 
 */
@Component
public class UsuarioMapper {

    /**
     * Convierte un objeto {@link UsuarioDTO} en una entidad {@link Usuario}.
     * 
     * Este método toma los datos del DTO junto con las entidades relacionadas 
     * (Rol, Sector, EstadoU y Cargo) que ya deben estar cargadas previamente, 
     * y construye un objeto {@link Usuario} listo para persistir en la base de datos.
     * 
     * @param dto el objeto de transferencia que contiene los datos del usuario
     * @param rol la entidad {@link Rol} asociada al usuario
     * @param sector la entidad {@link Sector} asociada al usuario
     * @param estado la entidad {@link EstadoU} asociada al usuario
     * @param cargo la entidad {@link Cargo} asociada al usuario
     * @return un objeto {@link Usuario} con los datos mapeados desde el DTO
     */
    public static Usuario toEntity(UsuarioDTO dto, Rol rol, Sector sector, EstadoU estado, Cargo cargo) {
        Usuario u = new Usuario();
        u.setIdUsuario(dto.getIdUsuario());
        u.setDni(dto.getDni());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setLegajo(dto.getLegajo());
        u.setRol(rol);
        u.setSector(sector);
        u.setEstado(estado);
        u.setCargo(cargo);
        return u;
    }

    /**
     * Convierte una entidad {@link Usuario} en un objeto {@link UsuarioDTO}.
     * 
     * Este método se utiliza generalmente para enviar información del usuario 
     * desde la base de datos hacia la capa de presentación o respuesta HTTP.
     * 
     * ⚠️ En entornos de producción se recomienda **no exponer la contraseña**
     * por motivos de seguridad.
     * 
     * @param u la entidad {@link Usuario} obtenida desde la base de datos
     * @return un objeto {@link UsuarioDTO} con los datos mapeados desde la entidad
     */
    public static UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(u.getIdUsuario());
        dto.setDni(u.getDni());
        dto.setEmail(u.getEmail());
        //dto.setPassword(u.getPassword()); // ⚠️ En producción conviene no exponerla
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setLegajo(u.getLegajo());

        // Se asignan los datos de las relaciones si existen
        if (u.getRol() != null) {
            dto.setIdRol(u.getRol().getIdRol());
            dto.setNombreRol(u.getRol().getNombre());
        }
        if (u.getSector() != null) {
            dto.setIdSector(u.getSector().getIdSector());
            dto.setNombreSector(u.getSector().getNombre());
        }
        if (u.getEstado() != null) {
            dto.setIdEstado(u.getEstado().getIdEstadoU());
            dto.setNombreEstado(u.getEstado().getNombre());
        }
        if (u.getCargo() != null) {
            dto.setIdCargo(u.getCargo().getIdCargo());
            dto.setNombreCargo(u.getCargo().getNombre());
        }

        return dto;
    }
    
}


