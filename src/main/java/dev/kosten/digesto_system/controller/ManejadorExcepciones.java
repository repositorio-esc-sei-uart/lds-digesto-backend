/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.controller;

import dev.kosten.digesto_system.exception.RecursoDuplicadoException;
import dev.kosten.digesto_system.exception.RecursoNoEncontradoException;
import dev.kosten.digesto_system.log.LogService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador global de excepciones para la aplicación Digesto.
 * Centraliza la lógica para convertir excepciones específicas y genéricas
 * en respuestas HTTP estandarizadas y consistentes para el cliente.
 * Utiliza LogService para registrar detalles de los errores.
 * @author avila
 * @author Quique
 */
@ControllerAdvice   // Indica que esta clase manejará excepciones globalmente
public class ManejadorExcepciones {

    @Autowired // Inyecta tu servicio de logging
    private LogService logService;

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
        manejarRecursoNoEncontrado(RecursoNoEncontradoException e, HttpServletRequest request) {
            
            logService.warn(String.format("Recurso no encontrado solicitado en [%s]: %s", request.getRequestURI(), e.getMessage()));
            
            Map<String, Object> error = new HashMap();
            error.put("timestamp", LocalDateTime.now());        // Se agrega la fecha y hora del error
            error.put("status", HttpStatus.NOT_FOUND.value());  // Código de estado HTTP 404
            error.put("error", "Recurso no encontrado.");       // Mensaje genérico de error
            error.put("message", e.getMessage());               // Mensaje específico de la excepción
            error.put("path", request.getRequestURI());         // Ruta donde se produjo el error
            
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
     
    /**
     * Maneja la excepción RecursoDuplicadoException y devuelve una respuesta estructurada.
     * Se genera un objeto JSON con información detallada del error, incluyendo
     * fecha, código HTTP, descripción y la ruta donde ocurrió la excepción.
     * @param e excepción de recurso no encontrado.
     * @param request solicitud HTTP que originó la excepción.
     * @return respuesta estructurada con detalles del error y código HTTP 409.
     */
    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>>
        manejarRecursoDuplicado(RecursoDuplicadoException e, HttpServletRequest request){
            
            logService.warn(String.format("Intento de crear recurso duplicado en [%s]: %s", request.getRequestURI(), e.getMessage()));
            
            Map<String, Object> error = new HashMap();
            error.put("timestamp", LocalDateTime.now());        // Se agrega la fecha y hora del error
            error.put("status", HttpStatus.CONFLICT.value());   // Código de estado HTTP 409
            error.put("error", "Recurso duplicado.");           // Mensaje genérico de error
            error.put("message", e.getMessage());               // Mensaje específico de la excepción
            error.put("path", request.getRequestURI());         // Ruta donde se produjo el error
                
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

    /**
     * Maneja cualquier otra excepción no capturada explícitamente.
     * Para errores inesperados del servidor, como la indisponibilidad de un servicio.
     * @param e La excepción genérica capturada.
     * @param request solicitud HTTP que originó la excepción.
     * @return respuesta estructurada con detalles del error y código HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
        manejarErrorGenerico(Exception e, HttpServletRequest request) {

        logService.error(String.format("Error inesperado procesando la solicitud [%s]", request.getRequestURI()), e);

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // Código 500
        error.put("error", "Error Interno del Servidor");
        error.put("message", "Ocurrió un error inesperado. El servicio podría no estar disponible.");
        error.put("path", request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
