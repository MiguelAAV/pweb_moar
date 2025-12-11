package com.example.pweb_backend.dto;

import lombok.Data;

@Data
public class ClienteRegisterRequest {

    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    private String password;

    // Dirección
    private String calle;
    private String numeroCasa;
    private String numeroDepto;
    private String comuna;
    private String region;
}
