/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.usuario;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link Usuario}.
 * 
 * Permite realizar operaciones de acceso a la base de datos relacionadas con los usuarios,
 * como buscar, guardar, actualizar y eliminar.
 * 
 * Además, incluye un método para buscar un usuario por su correo electrónico.
 * 
 * Autor: Esteban  
 * Versión: 1.0
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(Integer dni);
    Optional<Usuario> findByLegajo(Integer legajo);
}



