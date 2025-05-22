package com.FinZen.models.DTOS;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class DeudaDto {
    
    private String nombre;
    private BigDecimal monto;
    private BigDecimal montoPagado;
    private LocalDate fechaVencimiento;
    private String estado;
    private Long idCuenta;
}
