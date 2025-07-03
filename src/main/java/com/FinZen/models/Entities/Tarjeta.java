package com.FinZen.models.Entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tarjetas")
@Data
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarjeta")
    private Long idTarjeta;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTarjeta tipo;

    @Column(name = "banco")
    private String banco;

    @Column(name = "numero_ultimos_digitos")
    private String numeroUltimosDigitos;

    @Column(name = "saldo_actual", nullable = false)
    private BigDecimal saldoActual;

    @Column(name = "limite_credito")
    private BigDecimal limiteCredito;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "tarjeta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Presupuesto> presupuestos = new ArrayList<>();

    @OneToMany(mappedBy = "tarjeta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ingresos> ingresos = new ArrayList<>();

    public enum TipoTarjeta {
        CREDITO, DEBITO
    }
}