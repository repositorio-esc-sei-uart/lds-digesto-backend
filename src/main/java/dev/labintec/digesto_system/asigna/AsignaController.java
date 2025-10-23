/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.asigna;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que gestiona las asignaciones de {@link Permiso} a {@link Rol}.
 * 
 * Expone endpoints para asignar permisos a roles, listar todas las asignaciones,
 * filtrar asignaciones por rol o permiso, y eliminar asignaciones específicas.
 * 
 * Todos los métodos devuelven objetos {@link AsignaDTO} o listas de ellos,
 * manteniendo separada la entidad de la capa de presentación.
 * 
 * La URL base para los endpoints es "/api/v1/asignaciones".
 * 
 */
@RestController
@RequestMapping("/api/v1/asignaciones")
public class AsignaController {

    @Autowired
    private AsignaService service;

    /**
     * Asigna un permiso a un rol.
     * 
     * @param dto objeto {@link AsignaDTO} con los IDs del rol y permiso
     * @return {@link ResponseEntity} con el {@link AsignaDTO} creado
     */
    @PostMapping
    public ResponseEntity<AsignaDTO> asignar(@RequestBody AsignaDTO dto) {
        return ResponseEntity.ok(service.asignarPermisoARol(dto));
    }

    /**
     * Lista todas las asignaciones rol-permiso.
     * 
     * @return {@link ResponseEntity} con la lista de {@link AsignaDTO}
     */
    @GetMapping
    public ResponseEntity<List<AsignaDTO>> listar() {
        return ResponseEntity.ok(service.listarAsignaciones());
    }

    /**
     * Lista todas las asignaciones de un rol específico.
     * 
     * @param idRol identificador del rol
     * @return {@link ResponseEntity} con la lista de {@link AsignaDTO} del rol
     */
    @GetMapping("/rol/{idRol}")
    public ResponseEntity<List<AsignaDTO>> obtenerPorRol(@PathVariable Integer idRol) {
        return ResponseEntity.ok(service.obtenerPorRol(idRol));
    }

    /**
     * Lista todas las asignaciones de un permiso específico.
     * 
     * @param idPermiso identificador del permiso
     * @return {@link ResponseEntity} con la lista de {@link AsignaDTO} del permiso
     */
    @GetMapping("/permiso/{idPermiso}")
    public ResponseEntity<List<AsignaDTO>> obtenerPorPermiso(@PathVariable Integer idPermiso) {
        return ResponseEntity.ok(service.obtenerPorPermiso(idPermiso));
    }

    /**
     * Elimina una asignación específica entre un rol y un permiso.
     * 
     * @param idRol identificador del rol
     * @param idPermiso identificador del permiso
     * @return {@link ResponseEntity} con estado 204 (No Content) si se eliminó correctamente
     */
    @DeleteMapping("/rol/{idRol}/permiso/{idPermiso}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idRol, @PathVariable Integer idPermiso) {
        service.eliminar(idRol, idPermiso);
        return ResponseEntity.noContent().build();
    }
    
}

