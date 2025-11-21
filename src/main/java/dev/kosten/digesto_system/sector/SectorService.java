/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.sector;

import dev.kosten.digesto_system.sector.Sector;
import dev.kosten.digesto_system.sector.SectorRepository;
import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona las operaciones de negocio relacionadas con la entidad {@link Sector}.
 * 
 * Proporciona métodos para listar, crear, obtener y eliminar sectores.
 * Implementa validaciones para evitar duplicados y manejar excepciones personalizadas.
 * 
 */
@Service
public class SectorService {

    @Autowired
    private SectorRepository sectorRepo;

    
    /**
     * Obtiene la lista completa de sectores registrados.
     * 
     * @return lista de objetos {@link Sector}
     */
    public List<Sector> listarSectores() {
        return sectorRepo.findAll();
    }

    /**
     * Crea un nuevo sector en el sistema.
     * 
     * @param nuevoSector entidad {@link Sector} a registrar
     * @return el {@link Sector} guardado en la base de datos
     * @throws RecursoDuplicadoException si ya existe un sector con el mismo nombre
     */
    public Sector crearSector(Sector nuevoSector) {
        
        Optional<Sector> opcional = sectorRepo.findByNombre(nuevoSector.getNombre());
        
        if(opcional.isPresent()){
            throw new RecursoDuplicadoException("Ya existe un Sector con ese nombre");
        }
        
        return sectorRepo.save(nuevoSector);
    }

    /**
     * Obtiene un sector según su identificador.
     * 
     * @param id identificador del sector
     * @return el {@link Sector} correspondiente
     * @throws RecursoNoEncontradoException si no se encuentra el sector con el ID indicado
     */
    public Sector obtenerPorId(Integer id) {
        
        Optional<Sector> opcional = sectorRepo.findById(id);
        
        if(!opcional.isPresent()){
            throw new RecursoNoEncontradoException("No existe Sector con id: " + id);
        }
        
        return opcional.get();
    }



    /**
     * Elimina un sector según su identificador.
     * 
     * @param id identificador del sector a eliminar
     * @throws RecursoNoEncontradoException si el sector no existe
     */
    public void eliminarPorID(Integer id) {
        
        Optional<Sector> opcional = sectorRepo.findById(id);
        
        if(opcional.isPresent()){
            sectorRepo.deleteById(id);
        }
        else{
            throw new RecursoNoEncontradoException("No existe Sector con id: " + id);
        }
        
    }
}

