/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.asigna;

import dev.labintec.digesto_system.asigna.AsignaDTO;
import dev.labintec.digesto_system.asigna.AsignaMapper;
import dev.labintec.digesto_system.asigna.Asigna;
import dev.labintec.digesto_system.asigna.AsignaId;
import dev.labintec.digesto_system.permiso.Permiso;
import dev.labintec.digesto_system.rol.Rol;
import dev.labintec.digesto_system.exception.RecursoDuplicadoException;
import dev.labintec.digesto_system.exception.RecursoNoEncontradoException;
import dev.labintec.digesto_system.asigna.AsignaRepository;
import dev.labintec.digesto_system.permiso.PermisoRepository;
import dev.labintec.digesto_system.rol.RolRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona la asignación de {@link Permiso} a {@link Rol}.
 * 
 * Proporciona métodos para asignar permisos a roles, listar asignaciones
 * y eliminar relaciones específicas. Implementa validaciones para evitar
 * duplicados y manejar correctamente los errores de inexistencia.
 * 
 * Utiliza {@link AsignaRepository}, {@link RolRepository}, {@link PermisoRepository}
 * y {@link AsignaMapper} para la conversión entre entidades y DTOs.
 * 
 */
@Service
public class AsignaService {
    
    @Autowired
    private AsignaRepository asignaRepo;

    @Autowired
    private RolRepository rolRepo;

    @Autowired
    private PermisoRepository permisoRepo;

    @Autowired
    private AsignaMapper mapper;
    
    
    /**
     * Asigna un permiso a un rol, validando existencia y evitando duplicados.
     * 
     * @param dto objeto {@link AsignaDTO} con los IDs del rol y permiso
     * @return {@link AsignaDTO} creado
     * @throws RecursoNoEncontradoException si el rol o el permiso no existen
     * @throws RecursoDuplicadoException si la asignación ya existe
     */
    public AsignaDTO asignarPermisoARol(AsignaDTO dto) {
        // 1. Validar existencia de Rol
        Rol rol = rolRepo.findById(dto.getIdRol())
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado"));

        // 2. Validar existencia de Permiso
        Permiso permiso = permisoRepo.findById(dto.getIdPermiso())
                .orElseThrow(() -> new RecursoNoEncontradoException("Permiso no encontrado"));

        // 3. Construir la clave compuesta
        AsignaId id = new AsignaId(dto.getIdRol(), dto.getIdPermiso());

        // 4. Validar duplicado
        if (asignaRepo.existsById(id)) {
            throw new RecursoDuplicadoException("El rol ya tiene asignado este permiso");
        }

        // 5. Crear entidad Asigna y setear relaciones
        Asigna asigna = new Asigna();
        asigna.setId(id);
        asigna.setRol(rol);
        asigna.setPermiso(permiso);

        // 6. Guardar y devolver DTO
        return mapper.toDTO(asignaRepo.save(asigna));
    }
    
    
    /**
     * Lista todas las asignaciones rol-permiso.
     * 
     * @return lista de {@link AsignaDTO}
     */
    public List<AsignaDTO> listarAsignaciones() {
        return asignaRepo.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * Lista todas las asignaciones de un rol específico.
     * 
     * @param idRol identificador del rol
     * @return lista de {@link AsignaDTO} correspondientes al rol
     */
    public List<AsignaDTO> obtenerPorRol(Integer idRol) {
        return asignaRepo.findByRolIdRol(idRol)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
    
    
    /**
     * Lista todas las asignaciones de un permiso específico.
     * 
     * @param idPermiso identificador del permiso
     * @return lista de {@link AsignaDTO} correspondientes al permiso
     */
    public List<AsignaDTO> obtenerPorPermiso(Integer idPermiso) {
        return asignaRepo.findByPermisoIdPermiso(idPermiso)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
    
    
    /**
     * Elimina una asignación específica entre rol y permiso.
     * 
     * @param idRol identificador del rol
     * @param idPermiso identificador del permiso
     * @throws RecursoNoEncontradoException si la asignación no existe
     */
    public void eliminar(Integer idRol, Integer idPermiso) {
        AsignaId id = new AsignaId(idRol, idPermiso);

        if (!asignaRepo.existsById(id)) {
            throw new RecursoNoEncontradoException("Asignación no encontrada");
        }

        asignaRepo.deleteById(id);
    }
}

