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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher; // Para validación de email más robusta
import java.util.regex.Pattern; // Para validación de email más robusta
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository userRepo;

    @Value("${jwt.secret}")
    private String secret;
    
    // Define una constante para el nombre del estado activo
    private static final String ESTADO_ACTIVO = "Activo"; 
    
    // Patrón de RegEx para una validación de email más robusta
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // Usaremos una excepción runtime simple para el manejo de errores
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
    
    /**
     * Determina si el string de entrada tiene el formato de un correo electrónico.
     * @param identifier String que puede ser email o legajo.
     * @return true si parece un email.
     */
    private boolean isEmail(String identifier) {
        if (identifier == null) return false;
        Matcher matcher = EMAIL_PATTERN.matcher(identifier);
        return matcher.matches();
    }

    /**
     * Procesa el login usando un identificador genérico (Email o Legajo) y contraseña.
     * @param identifier Email o Legajo del usuario.
     * @param password Contraseña plana del usuario.
     * @return JWT Token si la autenticación es exitosa.
     */
    public String login(String identifier, String password) { // Modificado: Recibe 'identifier'
        Optional<Usuario> userOptional;

        // 1. Lógica de Búsqueda: Determinar si buscar por Email o por Legajo
        if (isEmail(identifier)) {
            // Si el formato es de Email
            userOptional = userRepo.findByEmail(identifier);
        } else {
            // Si no tiene formato de Email, asumimos que es un Legajo
            userOptional = userRepo.findByLegajo(identifier); 
        }

        // 2. Verificación de Credenciales y Existencia
        if (userOptional.isEmpty() || !BCrypt.checkpw(password, userOptional.get().getPassword())) {
            // Credenciales inválidas (oculta si falló por email/legajo o contraseña)
            throw new AuthenticationException("Credenciales inválidas");
        }

        Usuario user = userOptional.get();

        // 3. Verificación de Estado
        if (!ESTADO_ACTIVO.equalsIgnoreCase(user.getEstado().getNombre())) {
            throw new AuthenticationException("Usuario inactivo");
        }
        
        // 4. Generación de JWT
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        String token = Jwts.builder()
            .setSubject(user.getEmail())
            .claim("idUsuario", user.getIdUsuario())
            .claim("nombre", user.getNombre())
            .claim("email", user.getEmail())
            .claim("legajo", user.getLegajo()) // Nuevo: Añadido el legajo al payload del token
            .claim("rol", user.getRol())
            .claim("estadoU", user.getEstado().getNombre()) 
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return token;
    }
}