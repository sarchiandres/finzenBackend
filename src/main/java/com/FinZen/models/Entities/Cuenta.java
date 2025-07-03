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
@Table(name = "cuenta")
@Data
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Long idCuenta;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "monto", columnDefinition = "DECIMAL(15,2) NOT NULL")
    private BigDecimal monto;

    @Column(name = "monto_libre", columnDefinition = "DECIMAL(15,2) NOT NULL")
    private BigDecimal montoLibre;

    @Column(name="monto_ocupado")
    private BigDecimal montoOcupado;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuarios usuarios;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Presupuesto> presupuestos = new ArrayList<>();

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Meta> metas = new ArrayList<>();

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Deuda> deudas = new ArrayList<>();

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Ingresos> ingresos = new ArrayList<>();
}
