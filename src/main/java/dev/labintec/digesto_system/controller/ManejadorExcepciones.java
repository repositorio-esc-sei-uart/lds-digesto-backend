/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.labintec.digesto_system.controller;



import dev.labintec.digesto_system.exception.RecursoDuplicadoException;
import dev.labintec.digesto_system.exception.RecursoNoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author avila
 */

@ControllerAdvice
public class ManejadorExcepciones {
    
    /**
     * Maneja la excepción RecursoNoEncontradoException y devuelve una respuesta estructurada.
     * Se genera un objeto JSON con información detallada del error, incluyendo
     * fecha, código HTTP, descripción y la ruta donde ocurrió la excepción.
     * @param e excepción de recurso no encontrado.
     * @param request solicitud HTTP que originó la excepción.
     * @return respuesta estructurada con detalles del error y código HTTP 404.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class) // Intercepta excepciones de tipo RecursoNoEncontradoException
    public ResponseEntity<Map<String, Object>> 
        maanejarRecursoNoEncontrado(RecursoNoEncontradoException e, HttpServletRequest request) {
            Map<String, Object> error = new HashMap();
            error.put("timestamp", LocalDateTime.now());       // Se agrega la fecha y hora del error
            error.put("status", HttpStatus.NOT_FOUND.value()); // Código de estado HTTP 404
            error.put("error", "Recurso no encontrado.");      // Mensaje genérico de error
            error.put("message", e.getMessage());              // Mensaje específico de la excepción
            error.put("path", request.getRequestURI());        // Ruta donde se produjo el error
            
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
     
    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>>
            manejarRecursoDuplicado(RecursoDuplicadoException e, HttpServletRequest request){
                
            Map<String, Object> error = new HashMap();
            error.put("timestamp", LocalDateTime.now());       // Se agrega la fecha y hora del error
            error.put("status", HttpStatus.CONFLICT.value()); // Código de estado HTTP 404
            error.put("error", "Recurso duplicado.");      // Mensaje genérico de error
            error.put("message", e.getMessage());              // Mensaje específico de la excepción
            error.put("path", request.getRequestURI());        // Ruta donde se produjo el error
            
                
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
                
        }
}
