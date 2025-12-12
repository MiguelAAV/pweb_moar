package com.example.pweb_backend.service;

import com.example.pweb_backend.dto.LoginRequest;
import com.example.pweb_backend.dto.RegisterRequest;
import com.example.pweb_backend.dto.UserResponse;
import com.example.pweb_backend.model.User;
import com.example.pweb_backend.repository.UserRepository;
import com.example.pweb_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;   // usa el bean de SecurityConfig
    private final JwtService jwtService;            // mismo servicio JWT

    @Override
    public UserResponse register(RegisterRequest request) {
        // Validaciones básicas
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        if (userRepository.existsByRut(request.getRut())) {
            throw new RuntimeException("El RUT ya está registrado");
        }

        // Crear entidad User
        User u = new User();
        u.setNombre(request.getNombre());
        u.setApellido(request.getApellido());
        u.setRut(request.getRut());

        u.setCalle(request.getCalle());
        u.setNumeroCasa(request.getNumeroCasa());
        u.setNumeroDepto(request.getNumeroDepto());
        u.setComuna(request.getComuna());

        u.setTelefono(request.getTelefono());

        u.setEmail(request.getEmail());
        u.setPassword(passwordEncoder.encode(request.getPassword()));

        // Todos los registrados son CLIENTE
        u.setRole("CLIENTE");
        u.setFotoPerfilUrl(null);

        userRepository.save(u);

        // 🔥 Generar JWT real
        String token = jwtService.generateToken(u.getEmail(), u.getRole());

        return toResponse(u, token);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        User u = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), u.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 🔥 Ahora sí, JWT real (no "token-1")
        String token = jwtService.generateToken(u.getEmail(), u.getRole());

        return toResponse(u, token);
    }

    // ====== Helper privado para mapear User -> UserResponse ======
    private UserResponse toResponse(User u, String token) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setNombre(u.getNombre());
        r.setApellido(u.getApellido());
        r.setEmail(u.getEmail());
        r.setRut(u.getRut());
        r.setCalle(u.getCalle());
        r.setNumeroCasa(u.getNumeroCasa());
        r.setNumeroDepto(u.getNumeroDepto());
        r.setComuna(u.getComuna());
        r.setTelefono(u.getTelefono());
        r.setFotoPerfilUrl(u.getFotoPerfilUrl());
        r.setRole(u.getRole());
        r.setToken(token);
        return r;
    }
}

