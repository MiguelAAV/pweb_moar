// src/main/java/com/example/pweb_backend/dto/AdminUserRequest.java
package com.example.pweb_backend.dto;

import lombok.Data;

@Data
public class AdminUserRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password; // opcional en update
    private String telefono;
}
