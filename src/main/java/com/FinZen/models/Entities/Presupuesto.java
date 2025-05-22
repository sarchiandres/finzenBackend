package com.FinZen.models.Entities;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "PRESUPUESTO")
@Data
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_presupuesto")
    private Long idPresupuesto;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "monto_asignado", columnDefinition = "DECIMAL(15,2) NOT NULL")
    private BigDecimal montoAsignado;


    @ManyToOne
    @JoinColumn(name = "id_cuenta", referencedColumnName = "id_cuenta")
    @JsonIgnore
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "id_categoriapresupuesto", nullable = false)
    @JsonIgnore
    private CategoriaPresupuesto categoria;


    @OneToMany(mappedBy = "presupuesto" ,cascade = CascadeType.ALL, orphanRemoval = true,
        fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Gastos> gastos = new ArrayList<>();


    @OneToMany(mappedBy = "presupuesto" ,cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Ingresos> ingresos = new ArrayList<>();
    
}
