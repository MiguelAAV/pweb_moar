package com.example.pweb_backend.service;

import com.example.pweb_backend.dto.LoginRequest;
import com.example.pweb_backend.dto.RegisterRequest;
import com.example.pweb_backend.dto.UserResponse;
import com.example.pweb_backend.model.User;
import com.example.pweb_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Encriptador de contraseñas (bcrypt)
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        u.setPassword(passwordEncoder.encode(request.getPassword())); // guarda hash

        u.setRole("CLIENTE");              // todos los registrados son CLIENTE por defecto
        u.setFotoPerfilUrl(null);          // más adelante podrás permitir que lo edite

        userRepository.save(u);

        // Por ahora no usamos token real, lo dejamos vacío
        return toResponse(u, "");
    }

    @Override
    public UserResponse login(LoginRequest request) {
        User u = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), u.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // TODO: luego aquí generaremos un JWT real
        String fakeToken = "token-" + u.getId();

        return toResponse(u, fakeToken);
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
