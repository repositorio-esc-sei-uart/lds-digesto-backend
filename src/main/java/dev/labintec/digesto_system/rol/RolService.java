/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.rol;

import dev.labintec.digesto_system.exception.RecursoDuplicadoException;
import dev.labintec.digesto_system.exception.RecursoNoEncontradoException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona las operaciones de negocio relacionadas con la entidad {@link Rol}.
 * 
 * Proporciona métodos para listar, crear, obtener y eliminar roles.
 * Implementa validaciones para evitar duplicados y manejar excepciones personalizadas.
 * 
 * Permite obtener roles tanto por ID como por nombre.
 * 
 */
@Service
public class RolService {

    @Autowired
    private RolRepository rolRepo;


    /**
     * Obtiene la lista completa de roles registrados.
     * 
     * @return lista de objetos {@link Rol}
     */
    public List<Rol> listarRoles() {
        return rolRepo.findAll();
    }

    /**
     * Crea un nuevo rol en el sistema.
     * 
     * @param nuevoRol entidad {@link Rol} a registrar
     * @return el {@link Rol} guardado en la base de datos
     * @throws RecursoDuplicadoException si ya existe un rol con el mismo nombre
     */
    public Rol crearRol(Rol nuevoRol) {
        
        Optional<Rol> opcional = rolRepo.findByNombre(nuevoRol.getNombre());
        
        if(opcional.isPresent()){
            throw new RecursoDuplicadoException("Ya existe un Rol con ese nombre");
        }
        
        return rolRepo.save(nuevoRol);
    }

    /**
     * Obtiene un rol según su identificador.
     * 
     * @param id identificador del rol
     * @return el {@link Rol} correspondiente
     * @throws RecursoNoEncontradoException si no se encuentra el rol con el ID indicado
     */
    public Rol obtenerPorId(Integer id) {
        
        Optional<Rol> opcional = rolRepo.findById(id);
        
        if(!opcional.isPresent()){
            throw new RecursoNoEncontradoException("No existe un Rol con id: " +id);
        }
        
        return opcional.get();
    }
    
    /**
     * Obtiene un rol según su nombre.
     * 
     * @param nombreRol nombre del rol
     * @return el {@link Rol} correspondiente
     * @throws RecursoNoEncontradoException si no se encuentra un rol con ese nombre
     */
    public Rol obtenerPorNombre(String nombreRol){
        
        Optional<Rol> opcional = rolRepo.findByNombre(nombreRol);
        
        if(!opcional.isPresent()){
            throw new RecursoNoEncontradoException("No existe un rol con ese nombre");
        }
        
        return opcional.get();
    }

    /**
     * Elimina un rol según su identificador.
     * 
     * @param id identificador del rol a eliminar
     * @throws RecursoNoEncontradoException si el rol no existe
     */
    public void eliminarPorID(Integer id) {
        
        Optional<Rol> opcional = rolRepo.findById(id);
        
        if(opcional.isPresent()){
            rolRepo.deleteById(id);
        }
        else{
            throw new RecursoNoEncontradoException("Rol no encontrado");
        }
    }
    
}

