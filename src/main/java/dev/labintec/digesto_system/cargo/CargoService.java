/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.cargo;

import dev.labintec.digesto_system.cargo.Cargo;
import dev.labintec.digesto_system.cargo.CargoRepository;
import dev.labintec.digesto_system.exception.RecursoDuplicadoException;
import dev.labintec.digesto_system.exception.RecursoNoEncontradoException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona las operaciones de negocio relacionadas con la entidad {@link Cargo}.
 * 
 * Proporciona métodos para listar, crear, obtener y eliminar cargos.
 * Implementa validaciones para evitar duplicados y manejar excepciones personalizadas.
 * 
 */
@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepo;
    
    
    /**
     * Obtiene la lista completa de cargos registrados.
     * 
     * @return lista de objetos {@link Cargo}
     */
    public List<Cargo> listarCargos() {
        return cargoRepo.findAll();
    }
    
    /**
     * Crea un nuevo cargo en el sistema.
     * 
     * @param nuevoCargo entidad {@link Cargo} a registrar
     * @return el {@link Cargo} guardado en la base de datos
     * @throws RecursoDuplicadoException si ya existe un cargo con el mismo nombre
     */
    public Cargo crearCargo(Cargo nuevoCargo) {
        
        Optional<Cargo> opcional = cargoRepo.findByNombre(nuevoCargo.getNombre());
        
        if(opcional.isPresent()){
            throw new RecursoDuplicadoException("Ya existe un cargo con ese nombre");
        }
        
        return cargoRepo.save(nuevoCargo);
    }
    
    /**
     * Obtiene un cargo según su identificador.
     * 
     * @param id identificador del cargo
     * @return el {@link Cargo} correspondiente
     * @throws RecursoNoEncontradoException si no se encuentra el cargo con el ID indicado
     */
    public Cargo obtenerPorId(Integer id) {
        
        Optional<Cargo> opcional = cargoRepo.findById(id);
        
        if(!opcional.isPresent()){
            throw new RecursoNoEncontradoException("Cargo Inexistente");
        }
        
        return opcional.get();
    }

    /**
     * Elimina un cargo según su identificador.
     * 
     * @param id identificador del cargo a eliminar
     * @throws RecursoNoEncontradoException si el cargo no existe
     */
    public void eliminarPorID(Integer id) {
        
        Optional<Cargo> opcional = cargoRepo.findById(id);
        
        if(opcional.isPresent()){
            cargoRepo.deleteById(id);
        }
        else{
            throw new RecursoNoEncontradoException("No se encontro el cargo con id: " + id);
        }
    }
    
}


