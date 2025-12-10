package com.example.pweb_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "rut")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== DATOS DE CUENTA =====
    private String email;

    // Contraseña encriptada con BCrypt
    private String password;

    // Rol del usuario: SUPER_ADMIN, ADMIN, CLIENTE, etc.
    private String role;

    // Para “eliminar” usuarios sin borrar de la BD
    private Boolean enabled = true; // true = activo, false = desactivado

    // ===== DATOS PERSONALES =====
    private String nombre;
    private String apellido;
    private String rut;

    // Dirección
    private String calle;
    private String numeroCasa;
    private String numeroDepto;
    private String comuna;

    // Contacto
    private String telefono;

    // Foto de perfil (URL)
    private String fotoPerfilUrl;
}


