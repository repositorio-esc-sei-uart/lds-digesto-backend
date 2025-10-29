/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;



/**
 * Clase de configuración de la aplicación.
 * 
 * Contiene configuraciones generales como:
 * - Encriptación de contraseñas.
 * - Configuración global de CORS.
 * - Inicialización de datos al arrancar la aplicación.
 * 
 */

@Configuration
public class AppConfig {

    
    /**
     * Configura el codificador de contraseñas para la aplicación.
     * 
     * @return un objeto PasswordEncoder usando BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
    /**
     * Configura las reglas de CORS para toda la API.
     * Permite que se puedan hacer peticiones desde cualquier origen
     * y con métodos GET, POST, PUT y DELETE.
     * 
     * @return un objeto WebMvcConfigurer con la configuración de CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }


}