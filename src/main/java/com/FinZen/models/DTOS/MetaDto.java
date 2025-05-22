package com.FinZen.models.DTOS;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class MetaDto {
    private String titulo;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaLimite;
    private Boolean enProgreso;
    private BigDecimal valor;
    private String estado;
    private Long idCuenta;
    
}

