package com.FinZen.models.DTOS;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class IngresosDto {
    
    private String nombre;
    private Long idPresupuesto;
    private BigDecimal monto;
    private String fecha;
    private String fuente;


}
