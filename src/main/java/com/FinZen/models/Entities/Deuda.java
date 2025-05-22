package com.FinZen.models.Entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name= "DEUDA")
@Data
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDeuda;

    @Column(name="nombre")
    private String nombre;

    @Column (name="monto")
    private BigDecimal monto;

    @Column(name="monto_pagado")
    private BigDecimal montoPagado;

    @Column(name="fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name="estado", columnDefinition = "ENUM('PENDIENTE', 'PAGADA')")
    private String estado;

     @Column(name="fecha_creacion")
     private LocalDateTime fechaCreacion;


     @ManyToOne
     @JoinColumn(name="id_cuenta",nullable = false)
     @JsonIgnore
     private Cuenta cuenta;
    
}
