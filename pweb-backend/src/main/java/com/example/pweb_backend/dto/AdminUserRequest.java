package com.example.pweb_backend.dto;

import lombok.Data;

@Data
public class AdminUserRequest {

    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String password; // opcional al actualizar
    private Boolean enabled; // opcional al actualizar
}

