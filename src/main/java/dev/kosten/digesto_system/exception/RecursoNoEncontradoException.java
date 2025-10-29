/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author micae
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Código 404 automáticamente cuando se lanza esta excepción.
public class RecursoNoEncontradoException extends RuntimeException{

    public RecursoNoEncontradoException(String message) {
        super(message);
        // Constructor que recibe un mensaje personalizado (por ejemplo, "Usuario 5 no existe")
        // y lo pasa al constructor de la clase base (RuntimeException),
        // lo que permite acceder luego a ese mensaje con getMessage().
    }
    
    //Excepción personalizada que representa un recurso no encontrado.
}
