/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.authentication;

import dev.kosten.digesto_system.usuario.Usuario;
import dev.kosten.digesto_system.usuario.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filtro de seguridad que intercepta cada petición HTTP y valida el token JWT.
 * Extrae la información del usuario y su rol para setear la autenticación en Spring Security.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    
    /**
     * Clave secreta usada para firmar y verificar los JWT.
     * Se inyecta desde el application.properties.
     */
    @Value("${jwt.secret}")
    private String secret;
    
    
    /**
     * Método que se ejecuta en cada petición HTTP.
     * Verifica el token JWT y establece la autenticación en el contexto de seguridad.
     *
     * @param request  Objeto HttpServletRequest con la petición entrante
     * @param response Objeto HttpServletResponse para la respuesta
     * @param filterChain Cadena de filtros de Spring Security
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        
        // Si no hay token o no empieza con "Bearer", se deja pasar la petición sin autenticación
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7); // Extrae el token sin "Bearer "
        
        try {
            // Decodifica y valida el JWT
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(jwt)
                .getBody();

            // Extraer el rol desde el claim "rol"
            Object rolObj = claims.get("rol");

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (rolObj instanceof Map<?, ?> rolMap) {
                Object nombreRol = rolMap.get("nombre");
                if (nombreRol != null) {
                    // Convierte el rol a mayúsculas y agrega el prefijo "ROLE_"
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + nombreRol.toString().toUpperCase()));
                }
            }

            // Crear el token de autenticación con los roles
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            // Si el token es inválido o expiró, responde con 401 Unauthorized
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
            return;
        }
        // Continúa con el resto de la cadena de filtros
        filterChain.doFilter(request, response);
    }
}



