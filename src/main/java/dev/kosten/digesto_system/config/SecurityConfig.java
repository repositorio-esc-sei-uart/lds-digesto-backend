package dev.kosten.digesto_system.config;

import dev.kosten.digesto_system.authentication.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de Spring Security.
 * Define CORS, CSRF, políticas de sesión y filtros de autenticación.
 * @author Esteban
 * @author Quique
 */
@Configuration
public class SecurityConfig {
    
    /** Filtro de JWT que valida la autenticación en cada petición */
    @Autowired
    private JwtFilter jwtFilter;
    
    /**
     * Configura la cadena de filtros de seguridad.
     * 
     * @param http Objeto HttpSecurity que permite configurar la seguridad de Spring
     * @return SecurityFilterChain configurado con JWT y roles
     * @throws Exception
     */  
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // Configuración de CORS
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
                corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfig.setAllowedHeaders(java.util.List.of("*"));
                corsConfig.setAllowCredentials(true);
                return corsConfig;
            }))
            
            // Deshabilita CSRF porque estamos usando JWT
            .csrf(csrf -> csrf.disable())
            
            // Configuración de rutas y permisos
            // Configuración de roles y endpoints
            .authorizeHttpRequests(auth -> auth
                    
                // Endpoints públicos
                .requestMatchers("/api/v1/auth/**").permitAll()
                // El Home público usa los tipos de documento para filtrar
                .requestMatchers(HttpMethod.GET, "/api/v1/tipos-documento/**").permitAll()
                    
                // Endpoints solo Administrador
                .requestMatchers("/api/v1/usuarios/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/v1/roles/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/v1/cargos/**").hasRole("ADMINISTRADOR")
                //.requestMatchers("/api/v1/sectores/**").hasRole("ADMINISTRADOR") se menciona en sectores
                .requestMatchers("/api/v1/estadosU/**").hasRole("ADMINISTRADOR")   

                // Endpoints Administrador + Editor
                .requestMatchers("/api/v1/documentos/**").hasAnyRole("ADMINISTRADOR","EDITOR")
                .requestMatchers("/api/v1/archivos/**").hasAnyRole("ADMINISTRADOR","EDITOR")   
                .requestMatchers("/api/v1/palabras-clave/**").hasAnyRole("ADMINISTRADOR","EDITOR")
                .requestMatchers("/api/v1/estados/**").hasAnyRole("ADMINISTRADOR","EDITOR")
                    
                // Sectores:
                // GET (leer) permitido para Admin y Editor (para el dropdown del formulario)
                .requestMatchers(HttpMethod.GET, "/api/v1/sectores/**").hasAnyRole("ADMINISTRADOR", "EDITOR")
                // POST, PUT, DELETE (CRUD) permitido SOLO para Admin
                .requestMatchers("/api/v1/sectores/**").hasRole("ADMINISTRADOR")  
                    

                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )

            // Stateless (sin sesión)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Agrega nuestro filtro de JWT antes del filtro de autenticación de Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            .build();
    }
}

