package com.FinZen.models.DTOS;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GastosResponseDto {
    private Long idGasto;
    private String nombre;
    private BigDecimal monto;
    private String descripcion;
    private String fecha;
    private Long idCategoria;
    private String nombreCategoria;
    private Long idPresupuesto;
    private String nombrePresupuesto;

    public GastosResponseDto() {}

    public GastosResponseDto(Long idGasto, String nombre, BigDecimal monto, String descripcion, String fecha,
                             Long idCategoria, String nombreCategoria, Long idPresupuesto, String nombrePresupuesto) {
        this.idGasto = idGasto;
        this.nombre = nombre;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.idPresupuesto = idPresupuesto;
        this.nombrePresupuesto = nombrePresupuesto;
    }

    
    }




