/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.unidadEjecutora;

import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona las operaciones de negocio relacionadas con la entidad {@link unidadEjecutora}.
 * 
 * Proporciona métodos para listar, crear, obtener y eliminar unidadEjecutoras.
 * Implementa validaciones para evitar duplicados y manejar excepciones personalizadas.
 * @author Matias
 */
@Service
public class UnidadEjecutoraService {

    @Autowired
    private UnidadEjecutoraRepository unidadEjecutoraRepo;

    
    /**
     * Obtiene la lista completa de unidadEjecutoras registradas.
     * 
     * @return lista de objetos {@link UnidadEjecutora}
     */
    public List<UnidadEjecutora> listarUnidadEjecutoras() {
        return unidadEjecutoraRepo.findAll();
    }

    /**
     * Crea un nueva unidadEjecutora en el sistema.
     * 
     * @param nuevaUnidadEjecutora entidad {@link UnidadEjecutora} a registrar
     * @return el {@link UnidadEjecutora} guardado en la base de datos
     * @throws RecursoDuplicadoException si ya existe un unidadEjecutora con el mismo nombre
     */
    public UnidadEjecutora crearUnidadEjecutora(UnidadEjecutora nuevaUnidadEjecutora) {
        
        Optional<UnidadEjecutora> opcional = unidadEjecutoraRepo.findByNombre(nuevaUnidadEjecutora.getNombre());
        
        if(opcional.isPresent()){
            throw new RecursoDuplicadoException("Ya existe un UnidadEjecutora con ese nombre");
        }
        
        return unidadEjecutoraRepo.save(nuevaUnidadEjecutora);
    }

    /**
     * Obtiene un unidadEjecutora según su identificador.
     * 
     * @param id identificador de la unidadEjecutora
     * @return el {@link UnidadEjecutora} correspondiente
     * @throws RecursoNoEncontradoException si no se encuentra el unidadEjecutora con el ID indicado
     */
    public UnidadEjecutora obtenerPorId(Integer id) {
        
        Optional<UnidadEjecutora> opcional = unidadEjecutoraRepo.findById(id);
        
        if(!opcional.isPresent()){
            throw new RecursoNoEncontradoException("No existe UnidadEjecutora con id: " + id);
        }
        
        return opcional.get();
    }



    /**
     * Elimina un unidadEjecutora según su identificador.
     * 
     * @param id identificador de la unidadEjecutora a eliminar
     * @throws RecursoNoEncontradoException si el unidadEjecutora no existe
     */
    public void eliminarPorID(Integer id) {
        
        Optional<UnidadEjecutora> opcional = unidadEjecutoraRepo.findById(id);
        
        if(opcional.isPresent()){
            unidadEjecutoraRepo.deleteById(id);
        }
        else{
            throw new RecursoNoEncontradoException("No existe UnidadEjecutora con id: " + id);
        }
        
    }
}

