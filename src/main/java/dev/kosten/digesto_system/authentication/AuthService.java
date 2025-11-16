/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.kosten.digesto_system.authentication;

import dev.kosten.digesto_system.usuario.Usuario;
import dev.kosten.digesto_system.usuario.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository userRepo;

    @Value("${jwt.secret}")
    private String secret;
    
    // Define una constante para el nombre del estado activo
    private static final String ESTADO_ACTIVO = "Activo"; // Asegúrate de que esto coincida con el valor real

    // Usaremos una excepción runtime simple para el manejo de errores
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }

    public String login(String email, String password) {
        Optional<Usuario> userOptional = userRepo.findByEmail(email);
        
        if (userOptional.isEmpty() || !BCrypt.checkpw(password, userOptional.get().getPassword())) {
            // Credenciales inválidas
            throw new AuthenticationException("Credenciales inválidas");
        }

        Usuario user = userOptional.get();

        // 🛑 PASO CLAVE: VERIFICAR EL ESTADO DE ACTIVIDAD
        if (!ESTADO_ACTIVO.equalsIgnoreCase(user.getEstado().getNombre())) {
            // Usuario inactivo: Lanzar una excepción específica
            throw new AuthenticationException("Usuario inactivo");
        }
        
        // Si llega aquí, las credenciales son válidas Y el usuario está activo.
        
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        String token = Jwts.builder()
            // ... (rest of JWT building logic remains the same)
            .setSubject(user.getEmail())
            .claim("idUsuario", user.getIdUsuario())
            .claim("nombre", user.getNombre())
            .claim("email", user.getEmail())
            .claim("rol", user.getRol())
            .claim("estadoU", user.getEstado().getNombre()) // Siempre será "Activo" aquí
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return token;
    }
}

