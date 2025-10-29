/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.rol;

import dev.kosten.digesto_system.rol.RolDTO;
import dev.kosten.digesto_system.rol.RolMapper;
import dev.kosten.digesto_system.rol.Rol;
import dev.kosten.digesto_system.rol.RolService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para el recurso {@link Rol}.
 * 
 * Expone endpoints para gestionar los roles a través de la API,
 * incluyendo operaciones de listado, creación, obtención por ID, obtención por nombre y eliminación.
 * 
 * Utiliza {@link RolService} para la lógica de negocio
 * y {@link RolMapper} para la conversión entre entidades y DTOs.
 * 
 * Permite consultar roles tanto por su identificador como por su nombre.
 * 
 */
@RestController
@RequestMapping("/api/v1/roles")
public class RolController {

    @Autowired
    private RolService servicio;

    /**
     * Devuelve una lista con todos los roles registrados.
     * 
     * @return lista de objetos {@link RolDTO}
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RolDTO> listarRoles() {
        List<Rol> roles = servicio.listarRoles();
        List<RolDTO> dtos = new ArrayList();
        
        for(Rol rol : roles){
            dtos.add(RolMapper.toDTO(rol));
        }
        
        return dtos;
    }

    /**
     * Obtiene un rol según su identificador.
     * 
     * @param id identificador del rol
     * @return el {@link RolDTO} correspondiente
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RolDTO obtenerPorId(@PathVariable Integer id) {
        Rol rol = servicio.obtenerPorId(id);
        
        return RolMapper.toDTO(rol);
    }

    /**
     * Crea un nuevo rol.
     * 
     * @param dto objeto {@link RolDTO} con los datos del nuevo rol
     * @return el {@link RolDTO} creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RolDTO crearRol(@Valid @RequestBody RolDTO dto) {
        Rol nuevoRol = servicio.crearRol(RolMapper.toEntity(dto));
        
        return RolMapper.toDTO(nuevoRol);
    }

    /**
     * Elimina un rol según su identificador.
     * 
     * @param id identificador del rol a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminarRol(@PathVariable Integer id) {
        Rol rol = servicio.obtenerPorId(id);
        
        servicio.eliminarPorID(id);
    }
    
    /**
     * Obtiene un rol según su nombre.
     * 
     * @param nombreRol nombre del rol
     * @return el {@link RolDTO} correspondiente
     */
    @GetMapping("/nombres/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    public RolDTO obtenerPorNombre(@PathVariable String nombreRol){
        Rol rol = servicio.obtenerPorNombre(nombreRol);
        
        return RolMapper.toDTO(rol);
    }
}

