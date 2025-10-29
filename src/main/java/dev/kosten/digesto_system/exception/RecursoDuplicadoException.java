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
@ResponseStatus(HttpStatus.CONFLICT)
public class RecursoDuplicadoException extends RuntimeException{

    public RecursoDuplicadoException(String message) {
        super(message);
        // Constructor que recibe un mensaje de error y lo pasa al constructor de la clase padre (RuntimeException).
        // Esto permite personalizar el mensaje que se mostrará cuando se lance la excepción.
    }
    
    //Excepción personalizada que representa un recurso duplicado.
    // Se usa cuando se intenta crear un recurso que ya existe, por ejemplo, un usuario con el mismo username.
}
