package com.example.pweb_backend.controller;

import com.example.pweb_backend.dto.ClienteRegisterRequest;
import com.example.pweb_backend.model.User;
import com.example.pweb_backend.repository.UserRepository;
import com.example.pweb_backend.security.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Auth", description = "Login y Registro con JWT + BCrypt")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // usa el bean de SecurityConfig
    private final JwtService jwtService;

    // ================================
    //  REGISTRO CLIENTE
    // ================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ClienteRegisterRequest req) {

        if (req.getNombre() == null || req.getApellido() == null ||
                req.getEmail() == null || req.getPassword() == null ||
                req.getCalle() == null || req.getNumeroCasa() == null ||
                req.getComuna() == null || req.getRegion() == null) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Faltan campos obligatorios."));
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "El email ya está registrado."));
        }

        User u = new User();
        u.setNombre(req.getNombre());
        u.setApellido(req.getApellido());
        u.setEmail(req.getEmail());
        u.setTelefono(req.getTelefono());
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        u.setCalle(req.getCalle());
        u.setNumeroCasa(req.getNumeroCasa());
        u.setNumeroDepto(req.getNumeroDepto());
        u.setComuna(req.getComuna());
        u.setRegion(req.getRegion());

        u.setRole("CLIENTE");
        u.setEnabled(true);
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
    //  LOGIN CON JWT
    // ================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String rawPassword = req.get("password");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }

        // Generar JWT
        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);          // 👈 IMPORTANTE para el front
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


