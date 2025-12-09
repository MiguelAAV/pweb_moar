package com.example.pweb_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String brand;

    @Column(length = 1000)
    private String descripcion;

    private Integer precio; // en CLP
    private Integer stock;

    // NUEVO CAMPO: URL de la imagen
    private String imageUrl;

    // NUEVO CAMPO Valores esperados: "LIBERADO" o "NO LIBERADO"
    private String liberado;
}


