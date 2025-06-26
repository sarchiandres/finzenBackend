package com.FinZen.models.Entities;


import java.math.BigDecimal;

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
@Table(name = "INGRESO")
@Data
public class Ingresos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingreso")
    private Long idIngreso;

    
    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "monto", columnDefinition = "DECIMAL(15,2) NOT NULL")
    private BigDecimal monto;

    @Column(name = "fecha", columnDefinition = "DATE NOT NULL")
    private String fecha;

    @Column(name = "descripcion", length = 100)
    private String descripcion;


    @ManyToOne
    @JoinColumn(name="id_cuenta",nullable = false,referencedColumnName = "id_cuenta")
    @JsonIgnore
    private Cuenta cuenta ;

    @ManyToOne
    @JoinColumn(name="id_tarjeta",nullable = false,referencedColumnName = "id_tarjeta")
    @JsonIgnore
    private Tarjeta tarjeta ;


     @ManyToOne
    @JoinColumn(name="id_inversion",nullable = false,referencedColumnName = "id_inversion")
    @JsonIgnore
    private Inversion inversion ;
}
