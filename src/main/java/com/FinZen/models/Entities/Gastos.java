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
@Table(name = "GASTO")
@Data
public class Gastos {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_gasto")
    private Long idGasto;

    @Column(name = "monto", columnDefinition = "DECIMAL(15,2) NOT NULL")
    private BigDecimal monto;


    @Column(name = "fecha", columnDefinition = "DATE NOT NULL")
    private String fecha;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @ManyToOne
    @JoinColumn(name="id_presupuesto",nullable = false)
    @JsonIgnore
    private Presupuesto presupuesto;

    @ManyToOne
    @JoinColumn(name="id_categoria")
    @JsonIgnore
    private CategoriaGasto categoria;
}
