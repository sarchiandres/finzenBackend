package com.FinZen.models.DTOS;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GastosDto {
    

    private String nombre;
    private Long   IdCategoria;
    private String fecha;
    private String descripcion;
    private BigDecimal monto;
    private Long idPresupuesto;
    
  
}
