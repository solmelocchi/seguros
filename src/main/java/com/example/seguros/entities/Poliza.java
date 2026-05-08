package com.example.seguros.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "poliza")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_vigencia", nullable = false)
    private LocalDate fechaVigencia;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "productor", nullable = false)
    private String productor = "MELO";

    @Column(name = "cliente", nullable = false)
    private String cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_compania", nullable = false)
    private Compania compania;

    @Column(name = "ramo", nullable = false)
    private String ramo;

    @Column(name = "codigo_organizador")
    private String codigoOrganizador = "MENDOZA BROKER SRL";

    @Column(name = "codigo_productor")
    private String codigoProductor;

    @Column(name = "numero_poliza", nullable = false)
    private String numeroPoliza;

    @Column(name = "comision_total")
    private Double comisionTotal;

    @Column(name = "comision_comercial")
    private Double comisionComercial;

    @Column(name = "comision_broker")
    private Double comisionBroker;
}