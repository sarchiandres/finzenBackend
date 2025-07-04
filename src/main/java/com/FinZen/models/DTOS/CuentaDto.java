package com.FinZen.models.DTOS;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CuentaDto {

    private Long idUsuario;
    private String nombre;
    private BigDecimal monto;

}