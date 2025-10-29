/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.permiso;

import dev.kosten.digesto_system.permiso.PermisoDTO;
import dev.kosten.digesto_system.permiso.PermisoMapper;
import dev.kosten.digesto_system.permiso.Permiso;
import dev.kosten.digesto_system.permiso.PermisoService;
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
 * Controlador REST para el recurso {@link Permiso}.
 * 
 * Expone endpoints para gestionar los permisos a través de la API,
 * incluyendo operaciones de listado, creación, obtención por ID y eliminación.
 * 
 * Utiliza {@link PermisoService} para la lógica de negocio
 * y {@link PermisoMapper} para la conversión entre entidades y DTOs.
 * 
 */
@RestController
@RequestMapping("/api/v1/permisos")
public class PermisoController {

    @Autowired
    private PermisoService servicio;

    /**
     * Devuelve una lista con todos los permisos registrados.
     * 
     * @return lista de objetos {@link PermisoDTO}
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PermisoDTO> listarPermisos() {
        List<Permiso> permisos = servicio.listarPermisos();
        List<PermisoDTO> dtos = new ArrayList<>();
        for (Permiso permiso : permisos) {
            dtos.add(PermisoMapper.toDTO(permiso));
        }
        return dtos;
    }

    /**
     * Obtiene un permiso según su identificador.
     * 
     * @param id identificador del permiso
     * @return el {@link PermisoDTO} correspondiente
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PermisoDTO obtenerPorId(@PathVariable Integer id) {
        Permiso permiso = servicio.obtenerPorId(id);
        return PermisoMapper.toDTO(permiso);
    }

    /**
     * Crea un nuevo permiso.
     * 
     * @param dto objeto {@link PermisoDTO} con los datos del nuevo permiso
     * @return el {@link PermisoDTO} creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PermisoDTO crearPermiso(@Valid @RequestBody PermisoDTO dto) {
        Permiso permiso = servicio.crearPermiso(PermisoMapper.toEntity(dto));
        
        return PermisoMapper.toDTO(permiso);
    }

    /**
     * Elimina un permiso según su identificador.
     * 
     * @param id identificador del permiso a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPermiso(@PathVariable Integer id) {
        Permiso permiso = servicio.obtenerPorId(id);
        
        servicio.eliminarPorID(id);
    }
}

