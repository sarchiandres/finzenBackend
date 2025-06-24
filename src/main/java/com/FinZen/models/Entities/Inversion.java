package com.FinZen.models.Entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "INVERSIONES")
@Data
public class Inversion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inversion")
    private Long idInversion;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_inversion", nullable = false)
    private TipoInversion tipoInversion;

    @Column(name = "plataforma", nullable = false)
    private String plataforma;

    @Column(name = "valor_inicial", nullable = false)
    private BigDecimal valorInicial;

    @Column(name = "valor_actual", nullable = false)
    private BigDecimal valorActual;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "inversion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Presupuesto> presupuestos = new ArrayList<>();

    @OneToMany(mappedBy = "inversion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ingresos> ingresos = new ArrayList<>();

    public enum TipoInversion {
        ACCIONES, FONDOS, CRYPTO
    }
}