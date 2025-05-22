package com.FinZen.models.DTOS;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PresupuestoDto {
    
    private String nombre;
    private BigDecimal montoAsignado;
    private Long idCuenta;
    private Long idCategoriaPresupuesto;
    
}
