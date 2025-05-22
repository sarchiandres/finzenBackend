package com.FinZen.models.Entities;

import java.math.BigDecimal;
import java.time.LocalDate;


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
@Table(name = "META")
@Data
public class Meta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMeta;

    @Column(name = "titulo" ,length = 150)
    private String titulo;

    @Column(name= "descripcion")
    private String descripcion;

    @Column(name="fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name="fecha_limite")
    private LocalDate fechaLimite;

    @Column(name="en_progreso")
    private Boolean enProgreso;

    @Column(name="valor")
    private BigDecimal valor;

    @Column(name = "estado",columnDefinition = "ENUM('CREADO', 'INICIADO', 'TERMINADO')")
    private String estado;

    @ManyToOne
    @JoinColumn(name="id_cuenta")
    @JsonIgnore
    private Cuenta cuenta;

  
}
