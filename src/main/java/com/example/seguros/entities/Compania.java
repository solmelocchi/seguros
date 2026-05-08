package com.example.seguros.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "compania")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Compania {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "codigo_productor")
    private String codigoProductor;
}
