/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.permiso;

import dev.kosten.digesto_system.permiso.Permiso;
import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona las operaciones de negocio relacionadas con la entidad {@link Permiso}.
 * 
 * Proporciona métodos para listar, crear, obtener y eliminar permisos.
 * Implementa validaciones para evitar duplicados y manejar excepciones personalizadas.
 * 
 * Permite obtener permisos tanto por ID como por nombre.
 * 
 */
@Service
public class PermisoService {

    @Autowired
    private PermisoRepository permisoRepo;


    /**
     * Obtiene la lista completa de permisos registrados.
     * 
     * @return lista de objetos {@link Permiso}
     */
    public List<Permiso> listarPermisos() {
        return permisoRepo.findAll();
    }

    /**
     * Crea un nuevo permiso en el sistema.
     * 
     * @param nuevoPermiso entidad {@link Permiso} a registrar
     * @return el {@link Permiso} guardado en la base de datos
     * @throws RecursoDuplicadoException si ya existe un permiso con el mismo nombre
     */
    public Permiso crearPermiso(Permiso nuevoPermiso) {
        
        Optional<Permiso> opcional = permisoRepo.findByNombre(nuevoPermiso.getNombre());
        
        if(opcional.isPresent()){
            throw new RecursoDuplicadoException("Ya existe un Permiso con ese nombre");
        }
        
        return permisoRepo.save(nuevoPermiso);
    }

    /**
     * Obtiene un permiso según su identificador.
     * 
     * @param id identificador del permiso
     * @return el {@link Permiso} correspondiente
     * @throws RecursoNoEncontradoException si no se encuentra un permiso con ese ID
     */
    public Permiso obtenerPorId(Integer id) {
        
        Optional<Permiso> opcional = permisoRepo.findById(id);
        
        if(!opcional.isPresent()){
            throw new RecursoNoEncontradoException("No existe un Permiso con ese id");
        }
        
        return opcional.get();
    }
    
    /**
     * Obtiene un permiso según su nombre.
     * 
     * @param nombre nombre del permiso
     * @return el {@link Permiso} correspondiente
     * @throws RecursoNoEncontradoException si no se encuentra un permiso con ese nombre
     */
    public Permiso obtenerPorNombre(String nombre){
        
        Optional<Permiso> opcional = permisoRepo.findByNombre(nombre);
        
        if(!opcional.isPresent()){
            throw new RecursoNoEncontradoException("No existe un permiso con ese nombre");
        }
        
        return opcional.get();
    }
    
    /**
     * Elimina un permiso según su identificador.
     * 
     * @param id identificador del permiso a eliminar
     * @throws RecursoNoEncontradoException si no existe un permiso con ese ID
     */
    public void eliminarPorID(Integer id) {
        
        Optional<Permiso> opcional = permisoRepo.findById(id);
        
        if(opcional.isPresent()){
            permisoRepo.deleteById(id);
        }
        else{
            throw new RecursoNoEncontradoException("No existe un permiso con ese id");
        }
    }
    
}

