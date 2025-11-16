/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import dev.kosten.digesto_system.authentication.AuthService.AuthenticationException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            // Intenta iniciar sesión. Si falla, authService.login() lanza una excepción.
            String token = authService.login(dto.getEmail(), dto.getPassword());

            // Si tiene éxito, devuelve 200 OK con el token
            return ResponseEntity.ok().body(Map.of("token", token));

        } catch (AuthenticationException e) {
            
            String errorMessage = e.getMessage();
            
            // 🛑 1. CASO USUARIO INACTIVO: Devolver 403 Forbidden 🛑
            if ("Usuario inactivo".equals(errorMessage)) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN) // HTTP 403
                    .body(Map.of("message", errorMessage)); // {"message": "Usuario inactivo"}
            } 
            
            // 🛑 2. CASO CREDENCIALES INVÁLIDAS: Devolver 401 Unauthorized 🛑
            else if ("Credenciales inválidas".equals(errorMessage)) {
                return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // HTTP 401
                    .body(Map.of("message", errorMessage)); // {"message": "Credenciales inválidas"}
            } 
            
            // Fallback genérico de autenticación
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) 
                .body(Map.of("message", "Fallo en la autenticación"));

        }
    }
}