
package com.example.pweb_backend.dto;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private String nombre;
    private String apellido;
    private String email;
    private String rut;

    private String calle;
    private String numeroCasa;
    private String numeroDepto;
    private String comuna;

    private String telefono;

    private String fotoPerfilUrl;

    // rol del usuario: "ADMIN", "CLIENTE", etc.
    private String role;

    // token para autenticación (más adelante será JWT real)
    private String token;
}
