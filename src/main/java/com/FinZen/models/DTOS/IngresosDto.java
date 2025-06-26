package com.FinZen.models.DTOS;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class IngresosDto {
    
    private String nombre;
    private Long idCuenta;
    private Long idTarjeta;
    private Long idInversion;
    private BigDecimal monto;
    private String fecha;
    private String descripcion;


}
