package com.example.pweb_backend.controller;

import com.example.pweb_backend.dto.ClienteRegisterRequest;
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
@Tag(name = "Auth", description = "Login y Registro con BCrypt")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // ================================
    //  REGISTRO CLIENTE COMPLETO
    // ================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ClienteRegisterRequest req) {

        // Validación de campos obligatorios
        if (req.getNombre() == null || req.getApellido() == null ||
                req.getEmail() == null || req.getPassword() == null ||
                req.getCalle() == null || req.getNumeroCasa() == null ||
                req.getComuna() == null || req.getRegion() == null
        ) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Faltan campos obligatorios."));
        }

        // Validar email único
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "El email ya está registrado."));
        }

        // Crear usuario CLIENTE
        User u = new User();
        u.setNombre(req.getNombre());
        u.setApellido(req.getApellido());
        u.setEmail(req.getEmail());
        u.setTelefono(req.getTelefono());
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        // Dirección
        u.setCalle(req.getCalle());
        u.setNumeroCasa(req.getNumeroCasa());
        u.setNumeroDepto(req.getNumeroDepto());
        u.setComuna(req.getComuna());
        u.setRegion(req.getRegion());

        u.setRole("CLIENTE");
        u.setEnabled(true);

        // Campos opcionales
        u.setRut(null);
        u.setFotoPerfilUrl(null);

        userRepository.save(u);

        return ResponseEntity.ok(Map.of(
                "message", "Registro completado con éxito",
                "id", u.getId(),
                "email", u.getEmail(),
                "nombre", u.getNombre(),
                "apellido", u.getApellido(),
                "role", u.getRole()
        ));
    }

    // ================================
    //  LOGIN (NO CREA USUARIOS)
    // ================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String password = req.get("password");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        Map<String, Object> body = new java.util.HashMap<>();
        body.put("id", user.getId());
        body.put("email", user.getEmail());
        body.put("nombre", user.getNombre());
        body.put("apellido", user.getApellido());
        body.put("role", user.getRole());
        body.put("enabled", user.getEnabled());
        body.put("telefono", user.getTelefono());
        body.put("calle", user.getCalle());
        body.put("numeroCasa", user.getNumeroCasa());
        body.put("numeroDepto", user.getNumeroDepto());
        body.put("comuna", user.getComuna());
        body.put("region", user.getRegion());

        return ResponseEntity.ok(body);
    }

}

