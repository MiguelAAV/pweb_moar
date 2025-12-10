package com.example.pweb_backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    // Datos personales
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

    // Cuenta
    private String email;
    private String password;
    private String confirmPassword;
}
