package com.FinZen.models.Entities;


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

@Data
@Entity
@Table(name = "periodo_mensual")
public class PeriodoMesual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo")
    private Long idPeriodo;

    @Column(name = "mes", nullable = false)
    private int mes;

    @Column(name = "anio", nullable = false)
    private int anio;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuarios usuario;
    
    
}
