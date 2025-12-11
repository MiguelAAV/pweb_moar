package com.example.pweb_backend.controller;

import com.example.pweb_backend.dto.AdminUserRequest;
import com.example.pweb_backend.model.User;
import com.example.pweb_backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
@Tag(name = "Admin Users", description = "Gestión de cuentas ADMIN por el SUPER_ADMIN")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    // LISTAR ADMINS
    @GetMapping
    public List<User> listAdmins() {
        return userRepository.findAll().stream()
                .filter(u -> "ADMIN".equalsIgnoreCase(u.getRole()))
                .toList();
    }

    // CREAR ADMIN
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody AdminUserRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "El correo ya está registrado")
            );
        }

        if (req.getPassword() == null || req.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "La contraseña es obligatoria para crear un admin")
            );
        }

        User u = new User();
        u.setNombre(req.getNombre());
        u.setApellido(req.getApellido());
        u.setEmail(req.getEmail());
        u.setTelefono(req.getTelefono());
        u.setRole("ADMIN");
        u.setEnabled(true);
        u.setPassword(encoder.encode(req.getPassword()));

        // campos opcionales para tu modelo
        u.setRut(null);
        u.setCalle(null);
        u.setNumeroCasa(null);
        u.setNumeroDepto(null);
        u.setComuna(null);
        u.setFotoPerfilUrl(null);

        userRepository.save(u);
        return ResponseEntity.ok(u);
    }

    // ACTUALIZAR ADMIN COMPLETO (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(
            @PathVariable Long id,
            @RequestBody AdminUserRequest req
    ) {
        User u = userRepository.findById(id).orElse(null);

        if (u == null || !"ADMIN".equalsIgnoreCase(u.getRole())) {
            return ResponseEntity.notFound().build();
        }

        // Actualizar campos básicos
        u.setNombre(req.getNombre());
        u.setApellido(req.getApellido());
        u.setTelefono(req.getTelefono());

        // Permitir cambiar contraseña opcionalmente
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(encoder.encode(req.getPassword()));
        }

        userRepository.save(u);
        return ResponseEntity.ok(u);
    }

    // ACTIVAR / DESACTIVAR ADMIN (PATCH)
    @PatchMapping("/{id}/enabled")
    public ResponseEntity<?> toggleEnabled(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body
    ) {
        User u = userRepository.findById(id).orElse(null);

        if (u == null || !"ADMIN".equalsIgnoreCase(u.getRole())) {
            return ResponseEntity.notFound().build();
        }

        Boolean enabled = body.get("enabled");
        if (enabled == null) enabled = true;

        u.setEnabled(enabled);
        userRepository.save(u);

        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "enabled", u.getEnabled()
        ));
    }

    // 5) ELIMINAR ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        User u = userRepository.findById(id).orElse(null);

        if (u == null || !"ADMIN".equalsIgnoreCase(u.getRole())) {
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(u);

        return ResponseEntity.noContent().build(); // 204 OK sin contenido
    }
}

