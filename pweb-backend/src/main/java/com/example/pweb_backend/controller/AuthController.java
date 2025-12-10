package com.example.pweb_backend.controller;

import com.example.pweb_backend.model.User;
import com.example.pweb_backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Auth", description = "Login simple con BCrypt")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // SOLO REGISTRO (para crear usuarios nuevos)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String password = req.get("password");
        String role = req.getOrDefault("role", "CLIENT");

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "El email ya está registrado"));
        }

        User u = new User();
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password)); // SIEMPRE ENCRIPTADO
        u.setRole(role);

        userRepository.save(u);

        return ResponseEntity.ok(Map.of(
                "message", "Usuario registrado",
                "email", u.getEmail(),
                "role", u.getRole()
        ));
    }

    // LOGIN - JAMÁS CREA USUARIOS
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String password = req.get("password");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        // comparamos el password plano con el hash guardado
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "role", user.getRole()
        ));
    }
}
